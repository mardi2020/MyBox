package com.mardi2020.MyBox.user;

import com.mardi2020.MyBox.file.File;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${email}")
    private String hostSTMPid;

    @Value("${password}")
    private String hostSTMPPassword;

    private final Long MaxSize = 1073741824L;

    @Transactional
    public void userJoin(UserJoinDto user) {
        // password 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date time = new Date();
        String today = format.format(time);
        user.setCreatedDate(today);
        user.setMaxSize(MaxSize); // 1gb

        // email 유효성 검사
        Email email = new Email();
        boolean check = email.checkValidEmail(user.getEmail());
        if(check) {
            //db에 저장
            userRepository.registerUser(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 로그인을 하기 위해 가입된 user정보를 조회하는 메서드

        // db에서 가져오기
        UserLoginDto user = userRepository.findUserLogin(email);
        System.out.println("user = " + user);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if("admin".equals(email)){
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public void createUserRoot(String email) {
        File file = new File();
        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date time = new Date();
        String today = format.format(time);
        file.setCreatedDate(today);

        file.setUserId(email);
        file.setRoot(true);
        file.setDirectory(true);
        file.setFileName("cloud");
        file.setOriginalFileName("cloud");
        file.setPath("");
        userRepository.createUserRootDirectory(file);

        File rootDirectory = userRepository.getRootId(email);
        String id = rootDirectory.getId().toHexString();
        userRepository.addParentId(id);
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public void findPassword(User user, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        User check = userRepository.getUserByEmail(user.getEmail());
        PrintWriter out = response.getWriter();

        if(check == null) {
            out.print("등록되지 않은 아이디입니다.");
            out.close();
        }
        else {
            StringBuilder pw = new StringBuilder();
            for (int i = 0; i < 12; i++) {
                pw.append((char) ((Math.random() * 26) + 97));
            }
            user.setUserName(check.getUserName());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPw = passwordEncoder.encode(pw.toString());
            user.setPassword(pw.toString());
            userRepository.updatePassword(encodedPw, user.getEmail());
            sendEmail(user, "findpw");
            out.print("이메일로 임시 비밀번호를 발송하였습니다.");
            out.close();
        }
    }

    public void sendEmail(User user, String div) {
        String charSet = "utf-8";
        String hostSMTP = "smtp.gmail.com";
        String hostSMTPid = this.hostSTMPid;
        String hostSMTPpwd = this.hostSTMPPassword;

        String fromEmail = this.hostSTMPid;
        String fromName = "MyBox";
        String subject = "";
        String msg = "";

        if(div.equals("findpw")) {
            subject = "MyBox 임시 비밀번호 입니다.";
            msg += "<div align='center' style='border:1px solid black; font-family:verdana'>";
            msg += "<h3 style='color: blue;'>";
            msg += user.getUserName() + "님의 임시 비밀번호 입니다. 비밀번호를 변경하여 사용하세요.</h3>";
            msg += "<p>임시 비밀번호 : ";
            msg += user.getPassword() + "</p></div>";
        }

        String mail = user.getEmail();
        try {
            HtmlEmail email = new HtmlEmail();
            email.setDebug(true);
            email.setCharset(charSet);
            email.setHostName(hostSMTP);
            email.setSslSmtpPort("465");

            email.setAuthentication(hostSMTPid, hostSMTPpwd);
            email.setStartTLSEnabled(true);
            email.addTo(mail, charSet);
            email.setFrom(fromEmail, fromName, charSet);
            email.setSubject(subject);
            email.setHtmlMsg(msg);
            email.send();

        } catch(Exception e) {
            System.out.println("메일 발송에 실패했습니다." + e);
        }
    }

    public void updatePassword(UpdatePwUserDto user, HttpServletResponse response) throws IOException {
        String email = user.getEmail();
        String pwBefore = user.getPasswordBefore();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User targetUser = userRepository.getUserByEmail(email);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        if(passwordEncoder.matches(pwBefore, targetUser.getPassword())){
            String pw = user.getPasswordAfter();
            userRepository.updatePassword(passwordEncoder.encode(pw), email);

            out.print("비밀번호를 재설정하였습니다.");
        }
        else {
            out.print("기존 비밀번호가 틀립니다.");
        }
        out.close();
    }
}
