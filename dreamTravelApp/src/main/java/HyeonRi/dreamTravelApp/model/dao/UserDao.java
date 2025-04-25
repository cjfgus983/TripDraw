package HyeonRi.dreamTravelApp.model.dao;

import HyeonRi.dreamTravelApp.model.dto.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

    User findById(Long id);
    User findByEmail(String email);
    void insertUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
}
