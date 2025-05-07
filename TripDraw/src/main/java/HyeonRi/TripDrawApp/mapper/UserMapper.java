// src/main/java/HyeonRi/TripDrawApp/mapper/UserMapper.java
package HyeonRi.TripDrawApp.mapper;

import HyeonRi.TripDrawApp.domain.LoginType;
import HyeonRi.TripDrawApp.dto.UserRegisterRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
