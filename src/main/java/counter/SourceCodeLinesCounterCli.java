package counter;

import counter.service.LocCounter;
import counter.service.StatisticsCalculator;
import counter.service.StatisticsPrinter;

import java.util.Objects;
import java.util.Scanner;

import static java.lang.String.format;

public class SourceCodeLinesCounterCli {
    public static void main(String[] args) {
        StatisticsPrinter statisticsPrinter = new StatisticsPrinter(new StatisticsCalculator(new LocCounter()));
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        boolean stopProgram = false;
        String input;

        do {
            System.out.println("Please provide name of directory to scan for java source code or type 'exit' to stop the program.");
            System.out.print(">> ");
            input = scanner.nextLine();

            try {
                if (Objects.equals(input, "exit")) {
                    stopProgram = true;
                } else {
                    statisticsPrinter.printStatistics(input);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(format("Cannot scan directory %s for java source code. Exception is: [%s]. Please try again", input,
                        e.getLocalizedMessage()));
            }
        } while (!stopProgram);

        System.out.println("Exiting program.");
    }
}



