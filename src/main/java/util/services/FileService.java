package util.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileService {
    private static final FileService instance = new FileService();

    public static FileService getInstance() {
        return instance;
    }

    public BufferedWriter createWriter(Path filePath, boolean appendMode) throws IOException {
        // Создание директорий
        Path parentDir = filePath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }

        StandardOpenOption[] options;
        if (appendMode && Files.exists(filePath)) {
            options = new StandardOpenOption[]{
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            };
        } else {
            options = new StandardOpenOption[]{
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            };
        }
        return Files.newBufferedWriter(filePath, options);
        }

    public Path buildOutputPath(String basePath, String prefix, String fileName) {
        Path path;

        if (basePath == null || basePath.isEmpty()) {
            path = Paths.get("");
        } else {
            path = Paths.get(basePath);
        }

        return path.resolve(prefix + fileName);
    }
}
