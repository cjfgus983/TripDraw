// src/main/java/HyeonRi/TripDrawApp/mapper/UserMapper.java
package HyeonRi.TripDrawApp.mapper;

import HyeonRi.TripDrawApp.domain.LoginType;
import HyeonRi.TripDrawApp.domain.User;
import HyeonRi.TripDrawApp.dto.UserRegisterRequestDto;
import lombok.extern.java.Log;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface UserMapper {

    @Insert("""
        INSERT INTO `user` (
          email,
          password,
          name,
          phone_number,
          address, addressDetail,
          nickname, login_type
        ) VALUES (
          #{email},
          #{password},
          #{name},
          #{phoneNumber},
          #{address}, #{addressDetail},
          #{nickname}, #{loginType}
        )
    """)
    void insertUser(UserRegisterRequestDto request);

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

    /**
    * 비밀번호 변경 메서드
    * */
    @Update("""
            UPDATE user
            SET password = #{password}
            WHERE email = #{email}
            """)
    void updatePasswordByEmail(
            @Param("email") String email,
            @Param("password") String encodedPassword
    );


    // 이름+전화번호+로그인타입으로 조회
    @Select("""
      SELECT
        user_id        AS userId,
        email,
        password,
        name,
        phone_number   AS phoneNumber,
        address,
        nickname,
        login_type     AS loginType,
        role
      FROM user
      WHERE name = #{name}
        AND phone_number = #{phoneNumber}
        AND login_type = #{loginType}
      LIMIT 1
    """)
    Optional<User> findByNameAndPhoneNumberAndLoginType(
      @Param("name") String name,
      @Param("phoneNumber") String phoneNumber,
      @Param("loginType") LoginType loginType
    );



    /**
     * 프로필 업데이트: 닉네임, 주소, 전화번호, 비밀번호(암호화된) 변경
     */
    @Update("""
      <script>
        UPDATE `user`
        <set>
          nickname      = #{nickname},
          address       = #{address},
          addressDetail = #{addressDetail},
          phone_number  = #{phoneNumber}
          <if test="password != null">
            , password  = #{password}
          </if>
        </set>
        WHERE email = #{email}
      </script>
    """)
    void updateUser(User user);

    @Delete("""
        DELETE FROM `user`
        WHERE email = #{email}
        AND login_type = #{loginType}
    """)
    void deleteUser(@Param("email") String email, @Param("loginType") LoginType loginType);

    @Update("""
        UPDATE `user`
        SET nickname = #{nickname}
        WHERE email = #{email}
          AND login_type = #{loginType}
    """)
    int updateNicknameByEmailAndLoginType(
            @Param("email") String email,
            @Param("loginType") LoginType loginType,
            @Param("nickname") String nickname
    );
    
    /**
     * 사용자 ID로 닉네임 조회
     */
    @Select("SELECT nickname FROM `user` WHERE user_id = #{userId}")
    String findNicknameByUserId(@Param("userId") Long userId);
}
