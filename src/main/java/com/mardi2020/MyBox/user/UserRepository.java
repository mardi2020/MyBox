package com.mardi2020.MyBox.user;

import com.mardi2020.MyBox.file.File;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final MongoTemplate mongoTemplate;

    private static final String collectionName = "User";

    public void registerUser(UserJoinDto user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(user.getEmail()));
        query.addCriteria(Criteria.where("password").is(user.getPassword()));
        query.addCriteria(Criteria.where("userName").is(user.getUserName()));
        query.addCriteria(Criteria.where("createdDate").is(user.getCreatedDate()));
        query.addCriteria(Criteria.where("maxSize").is(user.getMaxSize()));
        query.addCriteria(Criteria.where("currentSize").is(user.getCurrentSize()));

        mongoTemplate.insert(user, collectionName);
    }

    public UserLoginDto findUserLogin(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        return mongoTemplate.findOne(query, UserLoginDto.class, collectionName);
    }

    public void createUserRootDirectory(File file) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fileName").is(file.getFileName()));
        query.addCriteria(Criteria.where("userId").is(file.getUserId()));
        query.addCriteria(Criteria.where("createdDate").is(file.getCreatedDate()));
        query.addCriteria(Criteria.where("isRoot").is(file.isRoot()));
        query.addCriteria(Criteria.where("originalFileName").is(file.getFileName()));
        query.addCriteria(Criteria.where("isDirectory").is(file.isDirectory()));

        mongoTemplate.insert(file, "File");
    }
    public File getRootId(String userId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("originalFileName").is("cloud"));

        return mongoTemplate.findOne(query, File.class, "File");
    }

    public void addParentId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        Update update = new Update();
        ArrayList<String> list = new ArrayList<>();
        list.add(id);
        update.set("parent", list);
        mongoTemplate.updateFirst(query, update, File.class, "File");
    }

    public User getUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, User.class, collectionName);
    }

    public void updateUserCurrentSize(String email, Long size) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Update update = new Update();
        update.set("currentSize", size);
        mongoTemplate.updateFirst(query, update, User.class, collectionName);
    }

}
