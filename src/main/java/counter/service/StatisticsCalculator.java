package counter.service;

import counter.model.DirectoryInfo;
import counter.model.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static counter.util.FileContentReader.readContentOfFile;
import static java.lang.String.format;

/**
 * Calculates java source code lines per each file in provided directory.
 */
public class StatisticsCalculator {
    private final LocCounter locCounter;

    public StatisticsCalculator(LocCounter locCounter) {
        this.locCounter = locCounter;
    }

    Map<String, DirectoryInfo> calculateStatistic(Path dir) {
        Map<String, DirectoryInfo> cache = new HashMap<>();
        try {
            Files.walk(dir)
                    .filter(path -> path.toFile().toString().endsWith(".java")) // limit search only to java source files
                    .forEach(javaFile -> calculateStatistics(javaFile, cache));

            System.out.println(format("Completed scanning directory [%s].",
                    dir.toAbsolutePath().normalize().toString()));
            return cache;
        } catch (IOException e) {
            throw new RuntimeException(format("Cannot calculate statistics for dir %s. Provided directory exists: %s", dir.toString(), dir.toFile().exists()));
        }
    }

    private void calculateStatistics(Path javaFile, Map<String, DirectoryInfo> cache) {
        /* IMPLEMENTATION_DETAILS:
        1). Having a list of java source files, calculate list of directories, which contain these files
        2). Create a link between parent directory -> its subdirs -> source files.
         This link is created backwards, starting from file, working up to its parent,
         then parent is connected to its own parent etc. Since we resolve here to
         recursion (which may not be an ideal solution, but easier to implement),
         working way up results in _much_ less recursion calls, as compared to walking
         from the root directory to all its descendants. The downside here it that it
         might take more operations: we will have to resolve parents for different
         source files; the upside here is that resolving parent is not a disk access
         operation and again, most importantly - there will be much less recursion.
         Ideally though it should be possible to rewrite this logic without using
         recursive calls at all - but current implementation seems to work fine even
         on scanning directories with multiple large projects
         3). Line count for each source file is propagated to all its parent directories
         along with other metadata
         4). Data about root directory and its children - both subdirectories and
         source files - is stored in cache, which is then used to print statistics
         */
        Path parent = javaFile.getParent();

        DirectoryInfo parentInfo = cache.get(parent.toString());
        if (parentInfo == null) {
            parentInfo = new DirectoryInfo(parent);
        }

        long linesOfCode = locCounter.countSourceCodeLines(readContentOfFile(javaFile.toFile()));
        parentInfo.addLinesOfCode(linesOfCode);

        FileInfo fileInfo = new FileInfo(javaFile);
        fileInfo.setLinesOfCode(linesOfCode);
        parentInfo.addFile(fileInfo);
        cache.put(parent.toString(), parentInfo);

        // this is a part when we walk from a source file to all its parents -> recursion
        populateParents(parent, null, linesOfCode, cache);
    }

    private void populateParents(Path currentDir, Path subdir, long linesOfCode, Map<String, DirectoryInfo> cache) {
        // fixme it is assumed, that this recursion won't be too long, that is, source file should not be nested too deep
        Path parent = currentDir.getParent();
        if (parent != null) {
            populateParents(parent, currentDir, linesOfCode, cache);
        }

        if (subdir != null) {
            String currentDirString = currentDir.toString();
            DirectoryInfo currentDirectoryInfo = cache.get(currentDirString);

            if (currentDirectoryInfo == null) {
                currentDirectoryInfo = new DirectoryInfo(currentDir);
            }
            currentDirectoryInfo.addSubdir(subdir);
            currentDirectoryInfo.addLinesOfCode(linesOfCode);

            cache.put(currentDirString, currentDirectoryInfo);
        }
    }
}
