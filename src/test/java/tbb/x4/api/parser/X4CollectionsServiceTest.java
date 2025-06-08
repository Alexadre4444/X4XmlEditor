package tbb.x4.api.parser;

import org.junit.jupiter.api.Test;
import tbb.x4.api.directory.IXDirectoryService;
import tbb.x4.api.directory.XDirectory;
import tbb.x4.api.model.ElementName;
import tbb.x4.api.model.IDescriptorRepository;
import tbb.x4.imp.parser.X4CollectionsService;
import tbb.x4.imp.parser.XDirectoryService;
import tbb.x4.imp.parser.XmlParserService;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class X4CollectionsServiceTest {
    IDescriptorRepository descriptorRepository = new DescriptorRepositoryMock();
    X4CollectionsService x4CollectionsService =
            new X4CollectionsService(descriptorRepository, new XmlParserService(descriptorRepository));
    IXDirectoryService directoryService = new XDirectoryService();

    @Test
    public void getCollections_shouldReturnCollection_ifCollectionFileFound() {
        XDirectory directory = directoryService.parseXBaseDirectory(
                new File("src/test/resources/parser"));
        List<Collection> collections = x4CollectionsService.getCollections(directory);
        assertEquals(1, collections.size());
        assertEquals(1, collections.getFirst().elements().size());

        Tag tagProduction = collections.getFirst().elements().getFirst();
        assertEquals(DescriptorRepositoryMock.WARES_PRODUCTION, tagProduction.descriptor());
        assertNull(tagProduction.value());
        assertEquals(0, tagProduction.attributes().size());
        assertEquals(1, tagProduction.subTags().size());

        Tag tagMethodArgon = tagProduction.subTags().getFirst();
        assertEquals(DescriptorRepositoryMock.WARES_PRODUCTION_METHOD, tagMethodArgon.descriptor());
        assertNull(tagMethodArgon.value());
        assertEquals(2, tagMethodArgon.attributes().size());
        assertTrue(tagMethodArgon.attribute("id").isPresent());
        Attribute tagMethodArgonId = tagMethodArgon.attribute("id").get();
        assertEquals(DescriptorRepositoryMock.ID, tagMethodArgonId.descriptor());
        assertEquals("argon", tagMethodArgonId.value().stringValue());
        assertTrue(tagMethodArgon.attribute("name").isPresent());
        Attribute tagMethodArgonName = tagMethodArgon.attribute("name").get();
        assertEquals(DescriptorRepositoryMock.NAME, tagMethodArgonName.descriptor());
        assertEquals("{20206,201}", tagMethodArgonName.value().stringValue());
        assertEquals(2, tagMethodArgon.subTags().size());

        Tag tagDefault = tagMethodArgon.subTags().getFirst();
        assertEquals(DescriptorRepositoryMock.WARES_PRODUCTION_METHOD_DEFAULT, tagDefault.descriptor());
        assertNull(tagDefault.value());
        assertEquals(1, tagDefault.attributes().size());
        Attribute tagMethodDefaultRace = tagDefault.attributes().getFirst();
        assertEquals(DescriptorRepositoryMock.RACE, tagMethodDefaultRace.descriptor());
        assertEquals("argon", tagMethodDefaultRace.value().stringValue());

        Tag tagUnknown = tagMethodArgon.subTags().get(1);
        assertEquals(descriptorRepository.getUnknownTagDescriptor(new ElementName("unknown")), tagUnknown.descriptor());
        assertEquals("Unknown content", tagUnknown.value().value());
        assertEquals(1, tagUnknown.attributes().size());
        Attribute tagUnknownUnknownAttribute = tagUnknown.attributes().getFirst();
        assertEquals(descriptorRepository.getUnknowAttributeDescriptor(new ElementName("pouet")), tagUnknownUnknownAttribute.descriptor());
        assertEquals("10", tagUnknownUnknownAttribute.value().stringValue());
    }
}
