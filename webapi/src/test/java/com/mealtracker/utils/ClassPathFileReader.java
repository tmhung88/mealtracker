package com.mealtracker.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ClassPathFileReader {

    public List<String> readFile(String inputFile) {
        try {
            var res = new ClassPathResource(inputFile);
            var myFile = res.getFile();
            return Files.readAllLines(myFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot read the file %s", inputFile), e);
        }

    }
}
