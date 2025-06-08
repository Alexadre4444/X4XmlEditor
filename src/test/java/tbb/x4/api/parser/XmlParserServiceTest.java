package tbb.x4.api.parser;

import org.junit.jupiter.api.Test;
import tbb.x4.api.model.ElementName;
import tbb.x4.api.model.IDescriptorRepository;
import tbb.x4.imp.parser.XmlParserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class XmlParserServiceTest {
    IDescriptorRepository descriptorRepository = new DescriptorRepositoryMock();
    IXmlParserService xmlParserService = new XmlParserService(descriptorRepository);

    @Test
    public void parseFile_expectedNoException_ifSimpleFile() throws IOException {
        // Assuming you have a valid XML file path
        String xmlFilePath = "src/test/resources/parser/wares_simple.xml";
        try (FileInputStream inputStream = new FileInputStream(xmlFilePath)) {
            ParsedFile parsedFile = assertDoesNotThrow(() -> xmlParserService.parseFile(inputStream));
            List<Tag> tags = parsedFile.tags();
            assertEquals(1, tags.size());

            Tag tagWares = tags.getFirst();
            assertEquals(DescriptorRepositoryMock.WARES, tagWares.descriptor());
            assertNull(tagWares.value());
            assertEquals(0, tagWares.attributes().size());
            assertEquals(1, tagWares.subTags().size());

            Tag tagProduction = tagWares.subTags().getFirst();
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

    @Test
    public void parseFile_expectedNoException_ifFullWaresFile() throws IOException {
        // Assuming you have a valid XML file path
        String xmlFilePath = "src/test/resources/parser/wares.xml";
        try (FileInputStream inputStream = new FileInputStream(xmlFilePath)) {
            assertDoesNotThrow(() -> xmlParserService.parseFile(inputStream));
        }
    }
}
