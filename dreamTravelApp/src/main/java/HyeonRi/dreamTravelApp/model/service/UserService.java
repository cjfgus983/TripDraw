package HyeonRi.dreamTravelApp.model.service;

import HyeonRi.dreamTravelApp.model.dto.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    void insertUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
    List<User> findAll(); // 선택적으로 전체 목록 조회 추가
}
