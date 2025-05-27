package HyeonRi.TripDrawApp.service.board;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import HyeonRi.TripDrawApp.dto.board.free.FreeCommentDto;
import HyeonRi.TripDrawApp.dto.board.free.FreeDto;
import HyeonRi.TripDrawApp.dto.board.free.FreeImageDto;
import HyeonRi.TripDrawApp.mapper.UserMapper;
import HyeonRi.TripDrawApp.mapper.board.FreeMapper;
import HyeonRi.TripDrawApp.service.AiService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeService {

    private final FreeMapper freeMapper;
    private final UserMapper userMapper;
    private final AiService aiService;
    

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    @Transactional
    public Long createFreeWithImages(FreeDto dto, List<MultipartFile> images) throws IOException {

        // nickname 받아오기
        dto.setNickName(userMapper.findNicknameByUserId(dto.getUserId()));
        
        String summary3 = aiService.summarizeInSentences(dto.getContent(), 3);
        dto.setSummary(summary3);

        // 1) 글 먼저 저장
        freeMapper.insertFree(dto);
        Long freeId = dto.getFreeId();

        // 2) 업로드 디렉터리 준비
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3) 이미지가 있으면, 하나씩 디스크에 쓰고 DB 에 URL 기록
        if (images != null) {
            for (MultipartFile mf : images) {
                if (mf.isEmpty()) continue;
                String filename = UUID.randomUUID() + "_" + mf.getOriginalFilename();
                Path target = uploadPath.resolve(filename);
                mf.transferTo(target.toFile());

                // 클라이언트에서 /uploads/{filename} 으로 접근할 수 있게 URL 구성
                String url = "/uploads/" + filename;
                FreeImageDto imgDto = new FreeImageDto();
                imgDto.setFreeId(freeId);
                imgDto.setImageUrl(url);
                freeMapper.insertImage(imgDto);
            }
        }
        
        return freeId;
    }

    public List<FreeDto> getAllFree() {
        List<FreeDto> list = freeMapper.getAllFree();
        // 게시글마다 이미지 URL 불러와서 dto 에 세팅
        for (FreeDto dto : list) {
            List<String> urls = freeMapper.getImageUrlsByFreeId(dto.getFreeId());
            dto.setNickName(userMapper.findNicknameByUserId(dto.getUserId()));
            dto.setImageUrls(urls);
        }
        return list;
    }

    
    
    // 게시글
    public Long createFree(FreeDto dto) throws IOException {
        freeMapper.insertFree(dto);
        return dto.getFreeId();
    }

    public FreeDto getFree(Long id) throws IOException {
    	// 1) 조회수 증가
    	freeMapper.incrementViewCount(id);
    	
    	// 2) 게시글 기본 정보 + 작성자 nickname
        // 3) 이미지 URL 목록
    	FreeDto dto = freeMapper.getFreeById(id);
    	dto.setNickName(userMapper.findNicknameByUserId(dto.getUserId()));
    	List<String> urls = freeMapper.getImageUrlsByFreeId(dto.getFreeId());
        dto.setImageUrls(urls);
        // 3문장 요약
//        String summary3 = aiService.summarizeInSentences(dto.getContent(), 3);
//        dto.setSummary(summary3);
        return dto;
    }


    public void updateFree(FreeDto dto) {
        freeMapper.updateFree(dto);
    }

    public void deleteFree(Long id) {
        freeMapper.deleteFree(id);
    }

    // 댓글
    public void addComment(FreeCommentDto dto) {
        freeMapper.insertComment(dto);
    }

    public List<FreeCommentDto> getComments(Long freeId) {
        return freeMapper.getCommentsByFreeId(freeId);
    }

    public FreeCommentDto getComment(Long freeId, Long commentId) {
        return freeMapper.getComment(commentId, freeId);
    }

    public void updateComment(FreeCommentDto dto) {
        freeMapper.updateComment(dto);
    }

    public void deleteComment(Long freeId, Long commentId) {
        freeMapper.deleteComment(commentId, freeId);
    }

    // 이미지
    public void addImage(FreeImageDto dto) {
        freeMapper.insertImage(dto);
    }
}
