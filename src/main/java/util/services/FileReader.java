package util.services;

import util.processing.producer.FileProducer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {
    private static final FileReader instance = new FileReader();


    public FileReader() {
    }
    public List<String> readFileLines(String fileName) throws IOException {
        System.out.println(Paths.get("").toAbsolutePath());
        Path currentPath = Paths.get("").toAbsolutePath().resolve("../../../test/resources");
        System.out.println(currentPath);
        Path path = currentPath.resolve(fileName);
        System.out.println(path);
        System.out.println(Files.exists(path));
        return Files.readAllLines(path);
    }

    public static FileReader getInstance() {return instance; }
}
