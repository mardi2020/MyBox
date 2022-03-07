package com.mardi2020.MyBox.file;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
public class FileUpdateDto {

    @Id
    private ObjectId id;

    private String fileName;
}
