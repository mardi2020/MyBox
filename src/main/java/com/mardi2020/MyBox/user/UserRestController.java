package com.mardi2020.MyBox.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public ResponseEntity editUserInfoPOST(HttpSession session, @RequestBody UserUpdateDto user) {
        ResponseEntity<String> responseEntity = null;
        try {
            User target = (User) session.getAttribute("user");
            String email = target.getEmail();
            userService.editUserInfo(email, user);
            responseEntity = new ResponseEntity<>("EDIT INFO SUCCESS", HttpStatus.OK);

        } catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(HttpSession session) {
        ResponseEntity<String> responseEntity = null;

        try {
            User target = (User) session.getAttribute("user");
            String email = target.getEmail();
            userService.deleteUser(email);
            responseEntity = new ResponseEntity<>("DELETE "+ email +" SUCCESS", HttpStatus.OK);

        } catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> loginPOST(@RequestBody UserLoginDto userLoginDto,
                                       HttpSession session) {
        ResponseEntity<String> responseEntity = null;
        try {
            userService.loginUser(session, userLoginDto);

            responseEntity = new ResponseEntity<>("LOGIN SUCCESS", HttpStatus.OK);

        } catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerPOST(@RequestBody UserJoinDto userJoinDto) {
        ResponseEntity<String> responseEntity = null;
        try {
            userService.userJoin(userJoinDto);
            String email = userJoinDto.getEmail();
            String userName = userJoinDto.getUserName();
            userService.createUserRoot(email, userName);
            responseEntity = new ResponseEntity<>("REGISTER SUCCESS", HttpStatus.OK);

        }catch (Exception e){
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/password/find", method = RequestMethod.POST)
    public void findPasswordPOST(@RequestBody User user, HttpServletResponse response) {
        try {
            userService.findPassword(user, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    public void resetPasswordPOST(@RequestBody UpdatePwUserDto user, HttpServletResponse response) {
        try {
            userService.updatePassword(user, response);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
