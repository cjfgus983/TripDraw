package HyeonRi.TripDrawApp.mapper;

import HyeonRi.TripDrawApp.dto.UserRegisterRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("""
        INSERT INTO `user` (
            email, password, name, phone_number, address, is_kakao_login, role
        ) VALUES (
            #{email}, #{password}, #{name}, #{phoneNumber}, #{address}, #{isKakaoLogin}, #{role}
        )
    """)
    void insertUser(UserRegisterRequest request);
}
