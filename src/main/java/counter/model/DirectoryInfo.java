package counter.model;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class DirectoryInfo extends FileInfo {

    private Set<Path> subDirs;
    private Set<FileInfo> files;

    public DirectoryInfo(Path path) {
        super(path);
    }

    public void addLinesOfCode(long linesOfCode) {
        this.linesOfCode += linesOfCode;
    }

    public Set<Path> getSubDirs() {
        return subDirs;
    }

    public void addSubdir(Path subdir) {
        if (subDirs == null) {
            subDirs = new HashSet<>();
        }
        subDirs.add(subdir);
    }

    public Set<FileInfo> getFiles() {
        return files;
    }

    public void addFile(FileInfo file) {
        if (files == null) {
            files = new HashSet<>();
        }
        files.add(file);
    }
}
