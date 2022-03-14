package com.mardi2020.MyBox.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String loginUserGet() {
        return "/user/login";
    }

    @GetMapping("/register")
    public String registerUserPage(Model model) {
        model.addAttribute("user", new UserJoinDto());
        return "/user/register";
    }

    @PostMapping("/register")
    public String registerUser(UserJoinDto user) throws Exception {
        userService.userJoin(user);
        userService.createUserRoot(user.getEmail(), user.getUserName());
        return "redirect:/";
    }

    @GetMapping("/findPassword")
    public String findPasswordGet() {
        return "/user/forgotPassword";
    }

    @PostMapping("/findPassword")
    public void findPasswordPost(@ModelAttribute User user, HttpServletResponse response) throws IOException {
        userService.findPassword(user, response);
    }

    @GetMapping("/updatePassword")
    public String updatePasswordGet() {
        return "/user/updatePassword";
    }

    @ResponseBody
    @PostMapping("/updatePassword")
    public void updatePasswordPost(@ModelAttribute UpdatePwUserDto user, HttpServletResponse response) throws IOException {
        userService.updatePassword(user, response);
    }
}
