package com.mardi2020.MyBox.file;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final MongoTemplate mongoTemplate;

    private final String collectionName = "File";

    public List<File> findFileAll() {
        return mongoTemplate.findAll(File.class, collectionName);
    }

    public List<File> findFolderAll(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isDirectory").is(true));
        query.addCriteria(Criteria.where("userId").is(userId));

        return mongoTemplate.find(query, File.class, collectionName);
    }

    public List<File> findFileAllByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(Sort.by(Sort.Direction.ASC, "path"));
        return mongoTemplate.find(query, File.class, collectionName);
    }

    @Transactional
    public void uploadFileToStorage(FileUploadDto fileUploadDto) {
        mongoTemplate.insert(fileUploadDto, collectionName);
    }

    @Transactional
    public void createFolder(FileUploadDto folder){
        mongoTemplate.insert(folder, collectionName);
    }

    @Transactional
    public void deleteFile(String objectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(objectId));
        mongoTemplate.findAndRemove(query, File.class, collectionName);
    }

    public File getFileById(String obejctId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(obejctId));
        return mongoTemplate.findOne(query, File.class, collectionName);
    }

    @Transactional
    public void updateFileSize(String objectId, long size) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(objectId));

        Update update = new Update();
        update.set("FileSize", size);
        mongoTemplate.updateFirst(query, update, File.class, collectionName);
    }

    public String findDuplicateFile(String fileName, String filePath) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fileName").is(fileName));
        query.addCriteria(Criteria.where("path").is(filePath));

        query.fields().include("id");
        return mongoTemplate.findOne(query, String.class, collectionName);
    }

    /**
     * 원하는 이름을 지정해 이름 수정
     * @param id 해당 도큐먼트의 id
     * @param fileName 파일 이름
     */
    @Transactional
    public void updateFileNameInDB(String id, String fileName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("fileName", fileName);
        mongoTemplate.updateFirst(query, update, File.class, collectionName);
    }

    /**
     * 바로 상위 폴더ㅇㅔ 추가된 파일, 폴더의 id 저장
     */
    @Transactional
    public void updateNearestParentChildList(String ParentId, List<String> children) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(ParentId));
        Update update = new Update();
        update.set("children", children);
        mongoTemplate.updateFirst(query, update, File.class, collectionName);
    }

    public File findFileByPathAndName(String path, String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fileName").is(name));
        query.addCriteria(Criteria.where("path").is(path));

        return mongoTemplate.findOne(query, File.class, collectionName);
    }

    public List<File> findFileInDir(String userId, String filePath) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("path").is(filePath));
        return mongoTemplate.find(query, File.class, collectionName);
    }

    @Transactional
    public void updateFilePathByFileId(String filePath, String fileId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fileId").is(fileId));
        Update update = new Update();
        update.set("path", filePath);
        mongoTemplate.updateFirst(query, update, File.class, collectionName);
    }

    @Transactional
    public void createUserRootDirectory(File file) {
        mongoTemplate.insert(file, collectionName);
    }

    public File getRootId(String userId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("userId").is(userId));

        return mongoTemplate.findOne(query, File.class, collectionName);
    }

    @Transactional
    public void addParentId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        Update update = new Update();
        ArrayList<String> list = new ArrayList<>();
        list.add(id);
        update.set("parent", list);
        mongoTemplate.updateFirst(query, update, File.class, collectionName);
    }
}
