package com.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ResourceUtils {

    public static Stream<String> read(String path) {
        try {
            return Files.lines(Paths.get(ResourceUtils.class.getClassLoader().getResource(path).getFile()));
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    private ResourceUtils() {}
}
