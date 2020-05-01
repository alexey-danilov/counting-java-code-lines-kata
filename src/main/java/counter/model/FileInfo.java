package counter.model;

import java.nio.file.Path;
import java.util.Objects;

public class FileInfo {

    protected final Path path;
    protected long linesOfCode;

    public FileInfo(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public long getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(long linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return linesOfCode == fileInfo.linesOfCode &&
                Objects.equals(path, fileInfo.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, linesOfCode);
    }

    @Override
    public String toString() {
        return "FileInf{" +
                "path=" + path +
                ", linesOfCode=" + linesOfCode +
                '}';
    }
}
