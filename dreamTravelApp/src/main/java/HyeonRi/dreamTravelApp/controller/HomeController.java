package HyeonRi.dreamTravelApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import HyeonRi.dreamTravelApp.model.dto.User;
import HyeonRi.dreamTravelApp.model.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	UserService uService;

    @GetMapping({"/","/index"})
    public String index() {
        return "index"; // /WEB-INF/views/index.jsp or index.html
    }
    
    @GetMapping("user/user-signup")
    private String getSignup() {
    	return "user/user-signup";
    }
    
    @GetMapping("/login")
    private String getLogin() {
    	return "user/login-form";
    }
    
    @PostMapping("/login")
	public String doLogin(@ModelAttribute User user, HttpSession session, Model model) {
		User loginUser = uService.findByEmail(user.getEmail());
		if (loginUser != null && loginUser.getPassword().equals(user.getPassword())) {
			session.setAttribute("loginUser", loginUser);
			return "redirect:/index";
		} else {
			model.addAttribute("alertMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			return "index";
		}
	}

	/**
	 * <pre>
	 * /logout을 get 방식으로 요청했을 때 session을 만료 시키고 로그아웃 처리한다.
	 * </pre>
	 * 
	 * 다음 경로는 redirect 형태로 /index로 이동한다.
	 * 
	 * @param session
	 * @return
	 */

	@GetMapping("/logout")
	public String doLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/index";
	}

}
