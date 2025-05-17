package HyeonRi.TripDrawApp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User{
    private Long      userId;
    private String    email;
    private String    password;
    private String    name;
    private String    phoneNumber;
    private String    address;
    private String    addressDetail;
    private String    nickname;
    private LoginType loginType;
    private String    role;
}
