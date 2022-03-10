package com.mardi2020.MyBox.user;

import lombok.Data;

@Data
public class UpdatePwUserDto {

    private String email;

    private String passwordBefore;

    private String passwordAfter;
}
