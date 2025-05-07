package HyeonRi.TripDrawApp.service;

import HyeonRi.TripDrawApp.domain.LoginType;
import HyeonRi.TripDrawApp.dto.UserRegisterRequest;
import HyeonRi.TripDrawApp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 신규 회원가입 처리
     */
    @Transactional
    public void register(UserRegisterRequest req) {
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

}
