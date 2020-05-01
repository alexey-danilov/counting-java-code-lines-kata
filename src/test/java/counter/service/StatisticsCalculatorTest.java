package counter.service;


import counter.model.DirectoryInfo;
import counter.model.FileInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for calculating source code statistics")
public class StatisticsCalculatorTest {
    private final StatisticsCalculator systemUnderTest = new StatisticsCalculator(new LocCounter());

    @Test
    @DisplayName("Provided subset of test data is processed correctly")
    public void processSubsetOfData() {
        String peopleDir = "src/test/resources/stubs/people";
        Map<String, DirectoryInfo> statistics = systemUnderTest.calculateStatistic(Paths.get(peopleDir));

        assertEquals(7, statistics.size());
        assertStatisticsForDir(statistics.get("src"), asList("src/test"), 8L, null);
        assertStatisticsForDir(statistics.get("src/test"), asList("src/test/resources"), 8L, null);
        assertStatisticsForDir(statistics.get("src/test/resources"), asList("src/test/resources/stubs"), 8L, null);
        assertStatisticsForDir(statistics.get("src/test/resources/stubs"), asList("src/test/resources/stubs/people"), 8L, null);
        assertStatisticsForDir(statistics.get("src/test/resources/stubs/people"), asList("src/test/resources/stubs/people/lazy", "src/test/resources/stubs/people/energetic"), 8L, null);
        assertStatisticsForDir(statistics.get("src/test/resources/stubs/people/lazy"), null, 3L, singletonList("src/test/resources/stubs/people/lazy/Dave.java"));
        assertStatisticsForDir(statistics.get("src/test/resources/stubs/people/energetic"), null, 5L, singletonList("src/test/resources/stubs/people/energetic/DaveOnSteroids.java"));
    }

    @Test
    @DisplayName("No regex issues when processing large file")
    public void processLargeFile() {
        String booksDir = "src/test/resources/stubs/books";
        Map<String, DirectoryInfo> statistics = systemUnderTest.calculateStatistic(Paths.get(booksDir));

        // asserting only parts specific for this case - processing of large file; rest is done in the test above
        assertStatisticsForDir(statistics.get("src/test/resources/stubs/books/dostoevsky"), null, 4L, singletonList("src/test/resources/stubs/books/dostoevsky/NoCatastrophicRegexBacktrackingForFyodorMikhaylovichPlease.java"));
    }

    private void assertStatisticsForDir(DirectoryInfo inspectedDir, List<String> subDirs, long linesOfCode, List<String> files) {
        assertSubDirs(inspectedDir, subDirs);
        assertFiles(inspectedDir, files);
        assertLinesOfCode(inspectedDir, linesOfCode);
    }

    private void assertSubDirs(DirectoryInfo inspectedDir, List<String> expectedSubDirs) {
        Set<Path> actualSubDirs = inspectedDir.getSubDirs();
        if (expectedSubDirs == null || expectedSubDirs.isEmpty()) {
            assertNull(actualSubDirs, format("Unexpected subdirectories for directory %s", inspectedDir.getPath().toString()));
            return;
        }
        assertEquals(expectedSubDirs.size(), actualSubDirs.size());
        for (String expectedSubdir : expectedSubDirs) {
            assertTrue(actualSubDirs.contains(Paths.get(expectedSubdir)), format("Expected subdirectory %s not found for directory %s", expectedSubdir, inspectedDir.getPath().toString()));
        }
    }

    private void assertFiles(DirectoryInfo inspectedDir, List<String> expectedFiles) {
        Set<FileInfo> actualFiles = inspectedDir.getFiles();
        if (expectedFiles == null || expectedFiles.isEmpty()) {
            assertNull(actualFiles, format("Unexpected files for directory %s", inspectedDir.getPath().toString()));
            return;
        }
        assertEquals(expectedFiles.size(), actualFiles.size());
        for (String expectedFile : expectedFiles) {

            assertTrue(actualFiles.stream()
                    .anyMatch(fileInfo -> fileInfo.getPath().equals(Paths.get(expectedFile))), format("Expected file %s not found for directory %s", expectedFile, inspectedDir.getPath().toString()));
        }
    }

    private void assertLinesOfCode(FileInfo info, long expectedLinesOfCode) {
        assertEquals(expectedLinesOfCode, info.getLinesOfCode(), format("Incorrect number of line of code in statistics for entry %s", info.getPath().toString()));
    }
}
