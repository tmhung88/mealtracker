package com.mealtracker.utils;

import com.mealtracker.utils.generator.Writable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class ClassPathFileWriter {
    private static final String NEW_LINE = System.getProperty("line.separator");

    public <T extends Writable> void write(String outputFile, List<T> writableObjs, Function<T, String> formatCaller) {
        try (var writer = new FileWriter(outputFile)) {

            for (var obj : writableObjs) {
                writer.write(formatCaller.apply(obj));
                writer.write(NEW_LINE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
