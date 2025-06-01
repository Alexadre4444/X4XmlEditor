package tbb.x4.api.directory;

import org.junit.jupiter.api.Test;
import tbb.x4.imp.parser.XDirectoryService;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class XDirectoryServiceTest {
    XDirectoryService xDirectoryService = new XDirectoryService();

    @Test
    public void isXBaseDirectory_expectedTrue_ifContainsJobEditorFile() {
        File validDir = new File("src/test/resources/directory/x4ValidDir");
        assertTrue(xDirectoryService.isXBaseDirectory(validDir));
    }

    @Test
    public void isXBaseDirectory_expectedFalse_ifDoesNotContainJobEditorFile() {
        File invalidDir = new File("src/test/resources/directory/x4InvalidDir");
        assertFalse(xDirectoryService.isXBaseDirectory(invalidDir));
    }

    @Test
    public void parseXBaseDirectory_expectedXDirectory_ifValidDirectory() {
        File validDir = new File("src/test/resources/directory");
        XDirectory xDirectory = xDirectoryService.parseXBaseDirectory(validDir);
        assertTrue(xDirectory.isDirectory());
        assertEquals("directory", xDirectory.name());
        assertEquals(2, xDirectory.children().size());

        Optional<XFsElement> validDirChild = xDirectory.children().stream()
                .filter(child -> child.name().equals("x4ValidDir"))
                .findFirst();
        assertTrue(validDirChild.isPresent());
        assertTrue(validDirChild.get().isDirectory());

        XDirectory validChildDir = (XDirectory) validDirChild.get();
        assertEquals(1, validChildDir.children().size());

        Optional<XFsElement> jobEditorFile = validChildDir.children().stream()
                .filter(child -> child.name().equals("jobeditor.html"))
                .findFirst();
        assertTrue(jobEditorFile.isPresent());
        assertFalse(jobEditorFile.get().isDirectory());
        assertInstanceOf(XFile.class, jobEditorFile.get());
        assertEquals("jobeditor.html", jobEditorFile.get().name());

        Optional<XFsElement> invalidDirChild = xDirectory.children().stream()
                .filter(child -> child.name().equals("x4InvalidDir"))
                .findFirst();
        assertTrue(invalidDirChild.isPresent());
    }
}
