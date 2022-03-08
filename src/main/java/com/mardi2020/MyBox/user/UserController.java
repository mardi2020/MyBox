package com.mardi2020.MyBox.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginUserPage() {
        return "/user/login";
    }


    @GetMapping("/register")
    public String registerUserPage(Model model) {
        model.addAttribute("user", new UserJoinDto());
        return "/user/register";
    }

    @PostMapping("/register")
    public String registerUser(UserJoinDto user) {
        userService.userJoin(user);
        User userdto = userService.getUserByEmail(user.getEmail());
        userService.createUserRoot(user.getEmail());
        return "redirect:/login";
    }
}
