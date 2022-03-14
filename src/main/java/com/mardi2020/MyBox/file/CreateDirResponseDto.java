package com.mardi2020.MyBox.file;

import lombok.Data;

@Data
public class CreateDirResponseDto {

    private String path;

    private String directoryName;

    private String parentId;
}
