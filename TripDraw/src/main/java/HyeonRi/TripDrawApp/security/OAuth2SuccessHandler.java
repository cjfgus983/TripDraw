package HyeonRi.TripDrawApp.security;

import HyeonRi.TripDrawApp.domain.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Value("${app.frontend.url}")
    private String frontendUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication auth) throws IOException {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) auth.getPrincipal();
        String email     = oauthUser.getAttribute("email");
        // 카카오 로그인 사용자라면 OAuth2User 의 attributes 에 loginType 이 없으니
        // 직접 “KAKAO” 를 지정하거나, DB 조회 후 꺼내도 됩니다.
        LoginType loginType = LoginType.KAKAO;

        // JwtUtil 에도 로그인 타입을 받도록 메서드 시그니처를 바꿔야 합니다.
        String token = jwtUtil.generateAccessToken(email, loginType);

        // SPA 에 토큰 + 타입을 전달하고 싶으면 쿼리스트링에 추가로 붙여도 되고,
        // 클레임에 넣어 두면 프론트에서 parsing 할 수 있습니다.
        res.sendRedirect(frontendUrl + "/oauth-callback?token=" + token);
    }
}
