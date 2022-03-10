package com.mardi2020.MyBox.file;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "file")
public class File {

    @Id
    private ObjectId id; // ObjectId

    private String fileName;

    private String userId; // 파일을 업로드한 주체

    private List<String> parent = new ArrayList<>(); // dicrectory

    private String createdDate; // 업로드된 날짜

    private String modified;

    private Long fileSize = 0L;

    private boolean isDirectory = false;

    private String type;

    private String path;

    private boolean isRoot = false;

    private String originalFileName;
}
