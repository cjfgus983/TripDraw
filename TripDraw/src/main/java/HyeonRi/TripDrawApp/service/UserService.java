package HyeonRi.TripDrawApp.service;

import HyeonRi.TripDrawApp.dto.UserRegisterRequest;
import HyeonRi.TripDrawApp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public void register(UserRegisterRequest request) {
        userMapper.insertUser(request);
    }
}
