package counter.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.lang.String.format;

public final class FileContentReader {

    private FileContentReader() {
    }

    /**
     * Utility method, so it static intentional (although in general I am not a bit fan of these)
     *
     * @param file
     * @return
     */
    public static String readContentOfFile(File file) {
        try {
            // in java 11 this would have been easier
            return String.join("\n", Files.readAllLines(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(format("Cannot read content of file %s", file), e);
        }
    }
}
