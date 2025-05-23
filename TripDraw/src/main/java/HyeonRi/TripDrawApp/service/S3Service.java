// src/main/java/HyeonRi/TripDrawApp/service/S3Service.java
package HyeonRi.TripDrawApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3;
    @Value("${aws.s3.bucket-name}")
    private String bucket;

    /** 객체가 버킷에 있는지 확인 */
    public boolean exists(String key) {
        try {
            s3.headObject(HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            // (1) 키가 존재하지 않을 때 처리
            System.out.println("해당 키가 없습니다: " + e.awsErrorDetails().errorMessage());
            return false;
        } catch (S3Exception e) {
            // (2) 그 외의 S3 오류 처리
            System.err.println("S3 오류: " + e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    /** 파일 업로드 후 public URL 반환 */
    public String upload(MultipartFile file, String key) throws IOException {
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        System.out.println("업로드 까지 옴");

        s3.putObject(req, RequestBody.fromBytes(file.getBytes()));

        return s3.utilities()
                .getUrl(b -> b.bucket(bucket).key(key))
                .toExternalForm();
    }

    /** S3 에서 바이트로 다운로드 */
    public byte[] download(String key) {
        ResponseBytes<GetObjectResponse> resp = s3.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build());
        return resp.asByteArray();
    }

    /** 저장된 객체의 public URL을 생성 */
    public String getUrl(String key) {
        return s3.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(key))
                .toExternalForm();
    }
}
