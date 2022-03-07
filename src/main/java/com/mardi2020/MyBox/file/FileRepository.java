package com.mardi2020.MyBox.file;

import lombok.RequiredArgsConstructor;
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

    public List<File> findFileAll() {
        return mongoTemplate.findAll(File.class, "File");
    }

    public List<File> findFolderAll() {
        Query query = new Query();
        query.addCriteria(Criteria.where("isDirectory").is(true));

        return mongoTemplate.find(query, File.class, "File");
    }

    public List<File> findFileAllByUserId(String userId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(Sort.by(Sort.Direction.ASC, "path"));
        return mongoTemplate.find(query, File.class, "File");
    }

    @Transactional
    public void uploadFileToStorage(FileUploadDto fileUploadDto) {
        Query query = new Query();

        query.addCriteria(Criteria.where("fileName").is(fileUploadDto.getFileName()));
        query.addCriteria(Criteria.where("userId").is(fileUploadDto.getUserId()));
        query.addCriteria(Criteria.where("createdDate").is(fileUploadDto.getCreatedDate()));
        query.addCriteria(Criteria.where("fileSize").is(fileUploadDto.getFileSize()));
        query.addCriteria(Criteria.where("type").is(fileUploadDto.getType()));
        query.addCriteria(Criteria.where("path").is(fileUploadDto.getPath()));
        query.addCriteria(Criteria.where("parent").is(fileUploadDto.getParent()));

        mongoTemplate.insert(fileUploadDto, "File");
    }

    @Transactional
    public void createFolder(FileUploadDto folder){
        Query query = new Query();

        query.addCriteria(Criteria.where("fileName").is(folder.getFileName()));
        query.addCriteria(Criteria.where("userId").is(folder.getUserId()));

        query.addCriteria(Criteria.where("parent").is(folder.getParent()));

        query.addCriteria(Criteria.where("createdDate").is(folder.getCreatedDate()));
        query.addCriteria(Criteria.where("modified").is(folder.getModified()));
        query.addCriteria(Criteria.where("fileSize").is(folder.getFileSize()));
        query.addCriteria(Criteria.where("path").is(folder.getPath()));
        query.addCriteria(Criteria.where("isDirectory").is(folder.isDirectory()));

        mongoTemplate.insert(folder, "File");
    }

    public void deleteFile(String objectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(objectId));
        mongoTemplate.findAndRemove(query, File.class, "File");
    }

    public File getFileById(String obejctId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(obejctId));
        return mongoTemplate.findOne(query, File.class, "File");
    }

    public void updateFileSize(String objectId, long size) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(objectId));

        Update update = new Update();
        update.set("FileSize", size);
        mongoTemplate.updateFirst(query, update, File.class, "File");
    }

    public String findDuplicateFile(String fileName, String filePath) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fileName").is(fileName));
        query.addCriteria(Criteria.where("path").is(filePath));

        query.fields().include("id");
        return mongoTemplate.findOne(query, String.class, "File");
    }

    /**
     * 원하는 이름을 지정해 이름 수정
     * @param id 해당 도큐먼트의 id
     * @param fileName 파일 이름
     */
    public void updateFileNameInDB(String id, String fileName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("fileName", fileName);
        mongoTemplate.updateFirst(query, update, File.class, "File");
    }
}
