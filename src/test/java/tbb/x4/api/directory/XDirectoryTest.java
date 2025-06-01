package tbb.x4.api.directory;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class XDirectoryTest {
    @Test
    void constructor_expectedValidDirectoryAndChildren_ifDirectoryAndChildrenValid() throws Exception {
        Path tempDir = Files.createTempDirectory("xdir");
        Files.createDirectory(tempDir.resolve("child"));
        XFsElement child = new XDirectory(tempDir.resolve("child"), Collections.emptyList());
        XDirectory dir = new XDirectory(tempDir, List.of(child));
        assertEquals(tempDir.getFileName().toString(), dir.name());
        assertTrue(dir.isDirectory());
        assertEquals(1, dir.children().size());
        assertEquals(child, dir.children().get(0));
    }

    @Test
    void constructor_expectedException_ifPathIsNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new XDirectory(null, Collections.emptyList()));
        assertEquals("Path must be a valid directory", ex.getMessage());
    }

    @Test
    void constructor_expectedException_ifPathIsNotDirectory() throws Exception {
        Path tempFile = Files.createTempFile("xdir", ".txt");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new XDirectory(tempFile, Collections.emptyList()));
        assertEquals("Path must be a valid directory", ex.getMessage());
    }

    @Test
    void constructor_expectedException_ifChildrenIsNull() throws Exception {
        Path tempDir = Files.createTempDirectory("xdir");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new XDirectory(tempDir, null));
        assertEquals("Children list cannot be null", ex.getMessage());
    }
}
