package HyeonRi.dreamTravelApp.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import HyeonRi.dreamTravelApp.model.dto.SearchCondition;
import HyeonRi.dreamTravelApp.model.dto.User;
import HyeonRi.dreamTravelApp.model.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements ControllerHelper {

    private final UserService uService;

    @Value("${api.key_sgis_service_id}")
    private String key_sgis_service_id;
    @Value("${api.key_sgis_security}")
    private String key_sgis_security;

    @GetMapping("/user-detail")
    private String userDetail(@RequestParam String email, Model model) {
        try {
            User user = uService.findByEmail(email);
            model.addAttribute("user", user);
            model.addAttribute("key_sgis_service_id", key_sgis_service_id);
            model.addAttribute("key_sgis_security", key_sgis_security);
        } catch (DataAccessException e) {
            e.printStackTrace();
            model.addAttribute("alertMsg", e.getMessage());
        }
        return "user/user-detail";
    }

    @GetMapping("/user-list")
    private String userList(@ModelAttribute SearchCondition condition, Model model) {
        Map<String, String> keyMap = Map.of("1", "name", "2", "email");
        String key = condition.getKey();
        if (key != null) {
            condition.setKey(keyMap.getOrDefault(key, ""));
        }
//        try {
//            Page<User> page = uService.search(condition);
//            model.addAttribute("page", page);
//        } catch (DataAccessException e) {
//            e.printStackTrace();
//            model.addAttribute("alertMsg", e.getMessage());
//        }
        return "user/user-list";
    }

    @GetMapping("/user-modify-form")
    private String userModifyForm(@RequestParam User user, Model model, RedirectAttributes redirs) {
        try {
            User u = uService.updateUser(user);
            model.addAttribute("user", u);
            return "user/user-modify-form";
        } catch (DataAccessException e) {
            e.printStackTrace();
            redirs.addFlashAttribute("alertMsg", e.getMessage());
            return "redirect:/auth/member-detail?email=" + user.getEmail();
        }
    }

    @PostMapping("/user-modify")
    private String userModify(@ModelAttribute User user, HttpSession session, RedirectAttributes redirs) {
        try {
            uService.updateUser(user);
            if (((User) session.getAttribute("loginUser")).getEmail().equals(user.getEmail())) {
                session.setAttribute("loginUser", user);
            }
            return "redirect:/auth/member-detail?email=" + user.getEmail();
        } catch (DataAccessException e) {
            e.printStackTrace();
            redirs.addFlashAttribute("alertMsg", e.getMessage());
            return "redirect:/auth/member-detail?email=" + user.getEmail();
        }
    }

    @PostMapping("/user-delete")
    private String userDelete(@RequestParam User user, @RequestParam String email, HttpSession session,
            RedirectAttributes redirs) {
        try {
            uService.deleteUser(user);
            if (session.getAttribute("loginUser") instanceof User u && u.getId().equals(user.getId())) {
                session.invalidate();
                return "redirect:/";
            } else {
                redirs.addFlashAttribute("alertMsg", "삭제되었습니다.");
                return "redirect:/auth/member-list?currentPage=1";
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            redirs.addFlashAttribute("alertMsg", e.getMessage());
            return "redirect:/auth/member-detail?email=" + email;
        }
    }

//    @PostMapping("/profileajax")
//    @ResponseBody
//    private Object profileAjax(@RequestParam MultipartFile file, @RequestParam String email) {
//        try {
//            uService.updateProfile(email, file.getBytes());
//            return Map.of("img", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(file.getBytes()));
//        } catch (DataAccessException | IOException e) {
//            e.printStackTrace();
//            return Map.of("error", e.getMessage());
//        }
//    }
}
