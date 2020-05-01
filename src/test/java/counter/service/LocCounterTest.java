package counter.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static counter.util.FileContentReader.readContentOfFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests for counting lines of code in pre-defined test classes")
class LocCounterTest {
    private final LocCounter systemUnderTest = new LocCounter();

    @Test
    @DisplayName("Dave is processed")
    public void processDave() {
        // NOTE: in general I prefer unit tests not to rely on any external resources and be fully self-contained, even such as file in
        // resources
        String daveContent = readContentOfFile(Paths.get("src/test/resources/stubs/people/lazy/Dave.java").toFile());
        assertEquals(3, systemUnderTest.countSourceCodeLines(daveContent), "Incorrect number of calculated code lines");
    }

    @Test
    @DisplayName("Hello is processed")
    public void processHello() {
        String helloContent = readContentOfFile(Paths.get("src/test/resources/stubs/Hello.java").toFile());
        assertEquals(5, systemUnderTest.countSourceCodeLines(helloContent), "Incorrect number of calculated code lines");
    }
}