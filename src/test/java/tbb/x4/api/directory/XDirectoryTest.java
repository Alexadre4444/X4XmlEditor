package tbb.x4.api.directory;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Test
    void find_expectedReturnChild_ifChildExists() throws Exception {
        Path tempDir = Files.createTempDirectory("xdir-find");
        Path childPath = tempDir.resolve("child");
        Files.createDirectory(childPath);
        XFsElement child = new XDirectory(childPath, Collections.emptyList());
        XDirectory dir = new XDirectory(tempDir, List.of(child));
        // Recherche du sous-dossier par chemin relatif
        Optional<XFsElement> found = dir.find(childPath.getFileName());
        assertTrue(found.isPresent());
        assertEquals(child, found.get());
    }

    @Test
    void find_expectedReturnEmpty_ifChildDoesNotExist() throws Exception {
        Path tempDir = Files.createTempDirectory("xdir-find-missing");
        XDirectory dir = new XDirectory(tempDir, Collections.emptyList());
        Optional<XFsElement> found = dir.find(Path.of("notfound"));
        assertTrue(found.isEmpty());
    }

    @Test
    void find_expectedException_ifPathIsNullOrAbsolute() throws Exception {
        Path tempDir = Files.createTempDirectory("xdir-find-exc");
        XDirectory dir = new XDirectory(tempDir, Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> dir.find(null));
        assertThrows(IllegalArgumentException.class, () -> dir.find(tempDir.toAbsolutePath()));
    }

    @Test
    void find_expectedReturnNestedChild_ifChildInSubdirectory() throws Exception {
        Path tempDir = Files.createTempDirectory("xdir-nested");
        Path subDirPath = tempDir.resolve("sub1");
        Path subSubDirPath = subDirPath.resolve("sub2");
        Files.createDirectory(subDirPath);
        Files.createDirectory(subSubDirPath);
        XFsElement subSubDir = new XDirectory(subSubDirPath, Collections.emptyList());
        XFsElement subDir = new XDirectory(subDirPath, List.of(subSubDir));
        XDirectory root = new XDirectory(tempDir, List.of(subDir));
        // Recherche du sous-sous-dossier par chemin relatif
        Optional<XFsElement> found = root.find(Path.of("sub1", "sub2"));
        assertTrue(found.isPresent());
        assertEquals(subSubDir, found.get());
    }

    @Test
    void allChildren_expectedReturnDirectAndNestedChildren() throws Exception {
        Path tempDir = Files.createTempDirectory("xdir-allchildren");
        Path subDirPath = tempDir.resolve("sub1");
        Path subSubDirPath = subDirPath.resolve("sub2");
        Path filePath = tempDir.resolve("file.txt");
        Files.createDirectory(subDirPath);
        Files.createDirectory(subSubDirPath);
        Files.createFile(filePath);
        XFsElement file = new XFile(filePath);
        XFsElement subSubDir = new XDirectory(subSubDirPath, Collections.emptyList());
        XFsElement subDir = new XDirectory(subDirPath, List.of(subSubDir));
        XDirectory root = new XDirectory(tempDir, List.of(file, subDir));
        List<XFsElement> all = root.allChildren();
        assertTrue(all.contains(file));
        assertTrue(all.contains(subDir));
        assertTrue(all.contains(subSubDir));
        assertEquals(3, all.size());
    }

    @Test
    void allChildren_expectedReturnEmpty_ifNoChildren() throws Exception {
        Path tempDir = Files.createTempDirectory("xdir-allchildren-empty");
        XDirectory dir = new XDirectory(tempDir, Collections.emptyList());
        List<XFsElement> all = dir.allChildren();
        assertTrue(all.isEmpty());
    }
}
