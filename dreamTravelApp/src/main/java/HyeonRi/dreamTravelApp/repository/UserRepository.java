package HyeonRi.dreamTravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import HyeonRi.dreamTravelApp.model.dto.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email); // 이메일로 가입 여부 확인

    User findByEmail(String email);      // 이메일로 사용자 정보 조회
}

