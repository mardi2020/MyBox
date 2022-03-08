package com.mardi2020.MyBox.user;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
public class UserJoinDto {

    @Id private ObjectId id;

    private String email;

    private String password;

    private String userName;

    private String createdDate;

    private Long maxSize; // 유저에게 할당된 최대 용량

    private Long currentSize = 0L; // 유저가 사용중인 용량

}
