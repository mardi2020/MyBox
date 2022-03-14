package com.mardi2020.MyBox.file;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FilesResponseDto {

    private String email;

    private Long currentSize;

    private Long maxSize;

    List<File> files = new ArrayList<>();

    List<File> directories = new ArrayList<>();
}
