package com.example.moviebooking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Slf4j
public final class FileUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> readFile(String path, TypeReference<List<T>> clazz) {

        try {
            File file = new File(path);
            return objectMapper.readValue(file, clazz);
        } catch(Exception e) {
            e.printStackTrace();
            log.info("Failed to read file from : " + path);
        }

        return Collections.emptyList();
    }
}
