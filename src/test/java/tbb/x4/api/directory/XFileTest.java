package tbb.x4.api.directory;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class XFileTest {
    @Test
    void constructor_expectedValidFile_ifFileIsValid() throws Exception {
        Path tempFile = Files.createTempFile("xfile", ".txt");
        XFile file = new XFile(tempFile);
        assertEquals(tempFile.getFileName().toString(), file.name());
        assertFalse(file.isDirectory());
    }

    @Test
    void constructor_expectedException_ifPathIsNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new XFile(null));
        assertEquals("Path must be a valid file", ex.getMessage());
    }

    @Test
    void constructor_expectedException_ifPathIsDirectory() throws Exception {
        Path tempDir = Files.createTempDirectory("xfile");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new XFile(tempDir));
        assertEquals("Path must be a valid file", ex.getMessage());
    }
}
