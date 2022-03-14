package com.mardi2020.MyBox.user;

import com.mardi2020.MyBox.file.File;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final MongoTemplate mongoTemplate;

    private static final String collectionName = "User";

    @Transactional
    public void registerUser(User user) {
        mongoTemplate.insert(user, collectionName);
    }

    public UserLoginDto findUserLogin(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, UserLoginDto.class, collectionName);
    }


    public User getUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, User.class, collectionName);
    }

    @Transactional
    public void updateUserCurrentSize(String email, Long size) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Update update = new Update();
        update.set("currentSize", size);
        mongoTemplate.updateFirst(query, update, User.class, collectionName);
    }

    @Transactional
    public void subtractFileSize(Long filesize, String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Update update = new Update();
        update.set("currentSize", filesize);
        mongoTemplate.updateFirst(query, update, User.class, collectionName);
    }

    @Transactional
    public void updatePassword(String password, String email) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("email").is(email));
        update.set("password", password);
        mongoTemplate.updateFirst(query, update, User.class, collectionName);
    }

    public User checkDuplicatedEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, User.class, collectionName);
    }

    public User checkDuplicatedUserName(String userName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(userName));
        return mongoTemplate.findOne(query, User.class, collectionName);
    }

    @Transactional
    public void deleteUser(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        mongoTemplate.remove(query, collectionName);
    }

    @Transactional
    public void updateUserInfo(String email, UserUpdateDto user) {
        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("email").is(email));

        update.set("password", user.getPassword());
        update.set("userName", user.getUserName());

        mongoTemplate.updateFirst(query, update, User.class, collectionName);
    }
}
