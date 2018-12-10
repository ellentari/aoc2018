package com.adventofcode;

import io.vavr.collection.List;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class ResourceUtils {

   static List<String> readLines(String path) {
       try (Stream<String> stream = streamFrom(path)) {
           return List.ofAll(stream);
       }
   }

   static String readString(String path) {
       return readLines(path).fold("", (s1, s2) -> s1 + s2);
   }

    private static Stream<String> streamFrom(String path) {
        try {
            URL resource = ResourceUtils.class.getClassLoader().getResource(path);

            if (resource == null) {
                return Stream.empty();
            }

            return Files.lines(Paths.get(resource.getFile()));
        } catch (IOException e) {
            return Stream.empty();
        }
    }

   private ResourceUtils() {}
}
