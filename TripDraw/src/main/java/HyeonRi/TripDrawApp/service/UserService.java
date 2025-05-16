package HyeonRi.TripDrawApp.service;

import HyeonRi.TripDrawApp.domain.LoginType;
import HyeonRi.TripDrawApp.domain.User;
import HyeonRi.TripDrawApp.dto.UserRegisterRequestDto;
import HyeonRi.TripDrawApp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * 신규 회원가입 처리
     */
    @Transactional
    public void register(UserRegisterRequestDto req) {
        // 1) loginType 기본 처리
        if (req.getLoginType() == null) {
            req.setLoginType(LoginType.GENERAL);
        }

        // 2) 중복 검사
        int exists = userMapper.countByEmailAndLoginType(
                req.getEmail(), req.getLoginType()
        );
        if (exists > 0) {
            throw new DuplicateKeyException(
                    "이미 가입된 회원입니다. email=" + req.getEmail() +
                            ", loginType=" + req.getLoginType()
            );
        }
        // 3) 비밀번호 암호화
        req.setPassword(passwordEncoder.encode(req.getPassword()));

        // 4) INSERT (login_type, role 은 DB DEFAULT)
        userMapper.insertUser(req);
    }


    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        // LoginType.GENERAL 만 검사
        int count = userMapper.countByEmailAndLoginType(email, LoginType.GENERAL);
        // 카운트가 0 이면 사용 가능
        return count == 0;
    }




    /**
     * 이메일로 사용자 조회 (프로필용)
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userMapper
                .findByEmailAndLoginType(email, LoginType.GENERAL)
                .orElseThrow(() ->
                        new UsernameNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다: " + email)
                );

    }

    /**
    * 받은 이메일과 이름이 일치하는지 한번더 확인
    * */
    public boolean isNameEmailMatch(String name, String email) {
        return userMapper.findByEmailAndLoginType(email, LoginType.GENERAL)
                .map(u -> u.getName().equals(name))
                .orElse(false);
    }

    /**
     * 이름·전화번호 조합으로 회원 조회 후,
     * 일치하면 이메일을 반환
     *
     * 아이디 찾기
     */
     public Optional<String> findEmailByNameAndPhoneNumber(String name, String phoneNumber) {
         return userMapper
                 .findByNameAndPhoneNumberAndLoginType(name, phoneNumber, LoginType.GENERAL)
                 .map(User::getEmail);
     }

    public boolean isExistEmail(String email) {
        return userMapper.findByEmailAndLoginType(email, LoginType.GENERAL).isPresent();
    }

    // 임시 비밀번호 생성 및 저장 후 메일 발송
    public void resetPasswordAndSendEmail(String email) {
        // 1) 임시 비밀번호 생성
        String tempPw = generateRandomPassword(10);

        // 2) 암호화해서 DB에 저장
        String encoded = passwordEncoder.encode(tempPw);
        userMapper.updatePasswordByEmail(email, encoded);

        // 3) 이메일로 임시 비밀번호 발송
        String subject = "[TripDraw] 임시 비밀번호 안내";
        String body = "안녕하세요, TripDraw입니다.\n\n"
                + "요청하신 임시 비밀번호를 안내드립니다.\n"
                + "임시 비밀번호: " + tempPw + "\n\n"
                + "로그인 후 마이페이지에서 비밀번호를 꼭 변경해주세요.";
        emailService.sendNewPassword(email, subject, body);
        System.out.println("분명 임시 비번 전송");
    }

    // (유틸) 랜덤 10글자 비밀번호 생성
    private String generateRandomPassword(int length) {
        String charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(charPool.charAt(rnd.nextInt(charPool.length())));
        }
        return sb.toString();
    }
}
