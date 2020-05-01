package counter.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests for formatting source code statistics")
class StatisticsPrinterTest {
    private final StatisticsPrinter systemUnderTest = new StatisticsPrinter(new StatisticsCalculator(new LocCounter()));

    @Test
    @DisplayName("Formatting of subset of data")
    public void formatStatistics() {
        String dir = "src/test/resources/stubs/people";
        List<String> formattedStatistics = systemUnderTest.getFormattedStatistics(dir);

        List<String> expectedFormat = asList(
                "          people: 8",
                "            lazy: 3",
                "              Dave.java: 3",
                "            energetic: 5",
                "              DaveOnSteroids.java: 5");

        assertEquals(expectedFormat, formattedStatistics, "Improper formatting of statistics");
    }
}