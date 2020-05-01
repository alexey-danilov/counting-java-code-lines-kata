package counter.service;

import counter.model.DirectoryInfo;
import counter.model.FileInfo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

/**
 * Prints source code lines statistics for each file and its parent directories (up to a provided directory).
 */
public class StatisticsPrinter {
    private final StatisticsCalculator statisticsCalculator;

    public StatisticsPrinter(StatisticsCalculator statisticsCalculator) {
        this.statisticsCalculator = statisticsCalculator;
    }

    public void printStatistics(String dir) {
        List<String> formattedStatistics = getFormattedStatistics(dir);

        if (formattedStatistics.isEmpty()) {
            System.out.println(format("No source files could be found in directory '%s'", dir));
            return;
        }

        formattedStatistics.forEach(System.out::println);
    }

    List<String> getFormattedStatistics(String dir) { // package access for testing
        List<String> printOutput = new ArrayList<>();

        Map<String, DirectoryInfo> statisticCache = statisticsCalculator.calculateStatistic(Paths.get(dir));
        // fixme assumption is that application is run on unix-based os
        String rootDir = dir.split("/")[0]; // this is the root dir from which we will start printing statistic, descending to its children

        if (statisticCache.containsKey(rootDir)) {
            formatStatisticsForDirectory(dir, statisticCache, printOutput);
        }
        return printOutput;
    }

    private void formatStatisticsForDirectory(String dir, Map<String, DirectoryInfo> statistics, List<String> printOutput) {
        DirectoryInfo dirStatistics = statistics.get(dir);
        formatInfo(dirStatistics, printOutput);

        Set<Path> subDirs = dirStatistics.getSubDirs();
        if (subDirs != null) {
            subDirs.forEach(subdir -> formatStatisticsForDirectory(subdir.toString(), statistics, printOutput));
        }

        Set<FileInfo> files = dirStatistics.getFiles();
        if (files != null) {
            files.forEach(info -> formatInfo(info, printOutput));
        }
    }

    private void formatInfo(FileInfo info, List<String> printOutput) {
        if (info == null) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < info.getPath().getNameCount(); i++) {
            buffer.append("  "); // fixme probably this should be configurable
        }
        long linesOfCode = info.getLinesOfCode();
        buffer.append(info.getPath().getFileName().toString()).append(": ").append(linesOfCode);

        printOutput.add(buffer.toString());
    }
}
