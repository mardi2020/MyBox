package com.mardi2020.MyBox.user;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
public class UserLoginDto {

    private String email;

    private String password;
}
