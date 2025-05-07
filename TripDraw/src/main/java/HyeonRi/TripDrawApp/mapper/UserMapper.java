// src/main/java/HyeonRi/TripDrawApp/mapper/UserMapper.java
package HyeonRi.TripDrawApp.mapper;

import HyeonRi.TripDrawApp.domain.LoginType;
import HyeonRi.TripDrawApp.domain.User;
import HyeonRi.TripDrawApp.dto.UserRegisterRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserMapper {

    @Insert("""
        INSERT INTO `user` (
          email,
          password,
          name,
          phone_number,
          address,
          nickname, login_type
        ) VALUES (
          #{email},
          #{password},
          #{name},
          #{phoneNumber},
          #{address},
          #{nickname}, #{loginType}
        )
    """)
    void insertUser(UserRegisterRequest request);

    @Select("""
      SELECT COUNT(*) 
      FROM `user` 
      WHERE email = #{email}
        AND login_type = #{loginType}
    """)
    int countByEmailAndLoginType(
            @Param("email") String email,
            @Param("loginType") LoginType loginType
    );



    /*
    * 로그인 위한 메소드
    * 이메일과 타입이 일치하는 유저를 리턴
    * */
    @Select("""
      SELECT *
        FROM `user`
       WHERE email = #{email}
         AND login_type = #{loginType}
    """)
    Optional<User> findByEmailAndLoginType(
            @Param("email") String email,
            @Param("loginType") LoginType loginType
    );
}
