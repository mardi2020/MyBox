package com.mardi2020.MyBox.user;

import com.mardi2020.MyBox.file.File;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public void userJoin(UserJoinDto user) {
        // password 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date time = new Date();
        String today = format.format(time);
        user.setCreatedDate(today);
        user.setMaxSize(1073741824L); // 1gb

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
        System.out.println("rootDirectory = " + rootDirectory);
        String id = rootDirectory.getId().toHexString();
        userRepository.addParentId(id);
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }
}
