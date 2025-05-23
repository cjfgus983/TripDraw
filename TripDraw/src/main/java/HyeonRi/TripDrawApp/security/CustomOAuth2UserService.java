package HyeonRi.TripDrawApp.security;

import HyeonRi.TripDrawApp.domain.User;
import HyeonRi.TripDrawApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User o = new DefaultOAuth2UserService().loadUser(req);
        Map<String,Object> attrs = o.getAttributes();
        String kakaoId = attrs.get("id").toString();
        Map<String,Object> account = (Map)attrs.get("kakao_account");
        String email   = (String)account.get("email");
        Map<String,Object> profile = (Map)account.get("profile");
        String nick    = (String)profile.get("nickname");
        String imgUrl  = (String)profile.get("profile_image_url");

        User u = userService.findOrCreateByKakao(email, nick);
        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("id",u.getUserId(),"email",u.getEmail(),"nickname",u.getNickname()),
                "email"
        );
    }
}
