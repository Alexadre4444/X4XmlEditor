package tbb.x4.api.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tbb.x4.api.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {
    @ParameterizedTest
    @CsvSource({
            "ParentName, PARENT, ParentDescription, ChildName, STRING, ChildDescription",
            "AnotherParent, PARENT, AnotherDescription, SubTag, NUMBER, SubTagDescription",
            "AnotherParent, UNKNOWN, AnotherDescription, SubTag, NUMBER, SubTagDescription"
    })
    void validParentTagDescriptorWithSubTagsShouldCreateInstance(String name, ValueType valueType, String description, String childName, ValueType childValueType, String childDescription) {
        TagDescriptor childTag = new TagDescriptor(new ElementName(childName), childValueType, childDescription, Collections.emptySet(), Collections.emptySet());
        TagDescriptor descriptor = new TagDescriptor(new ElementName(name), valueType, description, Collections.emptySet(), Set.of(childTag));

        assertNotNull(descriptor);
        assertEquals(name.toLowerCase(), descriptor.name().name());
        assertEquals(valueType, descriptor.valueType());
        assertEquals(description, descriptor.description());
        assertEquals(1, descriptor.subTags().size());
        assertTrue(descriptor.subTags().contains(childTag));
    }

    @ParameterizedTest
    @CsvSource({
            "ValidName, STRING, ValidDescription, AttributeName, STRING, AttributeDescription",
            "AnotherName, NUMBER, AnotherDescription, AnotherAttribute, NUMBER, AnotherAttributeDescription"
    })
    void validTagDescriptorWithAttributesShouldCreateInstance(String name, ValueType valueType, String description, String attributeName, ValueType attributeValueType, String attributeDescription) {
        AttributeDescriptor attribute = new AttributeDescriptor(new ElementName(attributeName), attributeValueType, attributeDescription);
        TagDescriptor descriptor = new TagDescriptor(new ElementName(name), valueType, description, Set.of(attribute), Collections.emptySet());

        assertNotNull(descriptor);
        assertEquals(name.toLowerCase(), descriptor.name().name());
        assertEquals(valueType, descriptor.valueType());
        assertEquals(description, descriptor.description());
        assertEquals(1, descriptor.attributes().size());
        assertTrue(descriptor.attributes().contains(attribute));
    }

    @ParameterizedTest
    @CsvSource({
            "ParentName, PARENT, ParentDescription",
            "AnotherParent, PARENT, AnotherDescription"
    })
    void parentTagDescriptorWithNullSubTagsShouldThrowException(String name, ValueType valueType, String description) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(new ElementName(name), valueType, description, Collections.emptySet(), null));
        assertEquals("SubTags cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "ValidName, STRING, ValidDescription",
            "AnotherName, NUMBER, AnotherDescription"
    })
    void tagDescriptorWithNullAttributesShouldThrowException(String name, ValueType valueType, String description) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(new ElementName(name), valueType, description, null, null, Collections.emptySet()));
        assertEquals("Attributes cannot be null", exception.getMessage());
    }

    @Test
    void tagWithNullDescriptorShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Tag(null, null, Collections.emptyList(), Collections.emptyList()));
        assertEquals("TagDescriptor cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "ParentName, PARENT, ParentDescription, InvalidSubTagName, STRING",
            "AnotherParent, PARENT, AnotherDescription, AnotherInvalidSubTag, NUMBER"
    })
    void tagWithInvalidSubTagsShouldThrowException(String name, ValueType valueType, String description, String invalidSubTagName, ValueType invalidSubTagValueType) {
        TagDescriptor validSubTag = new TagDescriptor(new ElementName("ValidSubTag"), ValueType.STRING, "ValidSubTagDescription", Collections.emptySet(), Collections.emptySet());
        TagDescriptor invalidSubTag = new TagDescriptor(new ElementName(invalidSubTagName), invalidSubTagValueType, "InvalidSubTagDescription", Collections.emptySet(), Collections.emptySet());
        TagDescriptor descriptor = new TagDescriptor(new ElementName(name), valueType, description, Collections.emptySet(), Set.of(validSubTag));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Tag(descriptor, null,
                        Collections.emptyList(), List.of(new Tag(invalidSubTag, new ElementValue("1", invalidSubTagValueType),
                        Collections.emptyList(), Collections.emptyList()))));
        assertEquals("SubTag %s not found in descriptor %s.".formatted(invalidSubTag.name().name(), descriptor.name().name()), exception.getMessage());
    }

    @Test
    void tagWithUnknownSubTagShouldNotThrow() {
        TagDescriptor unknownSubTag = new TagDescriptor(new ElementName("test"), ValueType.UNKNOWN, "unknownSubTag",
                Collections.emptySet(), Collections.emptySet());
        TagDescriptor validSubTag = new TagDescriptor(new ElementName("test4"), ValueType.STRING, "validSubTag",
                Collections.emptySet(), Collections.emptySet());
        TagDescriptor descriptor = new TagDescriptor(new ElementName("test2"), ValueType.PARENT, "desc",
                Collections.emptySet(), Set.of(validSubTag));

        assertDoesNotThrow(() -> new Tag(descriptor, null, Collections.emptyList(),
                List.of(new Tag(unknownSubTag, new ElementValue("1", ValueType.UNKNOWN),
                        Collections.emptyList(), Collections.emptyList()))));
    }

    @Test
    void tagWithUnknownAttributeShouldNotThrow() {
        AttributeDescriptor unknownAttribute = new AttributeDescriptor(new ElementName("unknownAttribute"),
                ValueType.UNKNOWN, "unknownAttribute");
        TagDescriptor validSubTag = new TagDescriptor(new ElementName("test4"), ValueType.STRING, "validSubTag",
                Collections.emptySet(), Collections.emptySet());

        assertDoesNotThrow(() -> new Tag(validSubTag, null, List.of(new Attribute(unknownAttribute, "5")),
                List.of()));
    }

    @CsvSource({
            "ValidName, STRING, ValidDescription, InvalidAttributeName, STRING",
            "AnotherName, NUMBER, AnotherDescription, AnotherInvalidAttribute, NUMBER"
    })
    void tagWithInvalidAttributesShouldThrowException(String name, ValueType valueType, String description, String invalidAttributeName, ValueType invalidAttributeValueType) {
        AttributeDescriptor validAttribute = new AttributeDescriptor(new ElementName("ValidAttribute"), valueType, "ValidAttributeDescription");
        AttributeDescriptor invalidAttribute = new AttributeDescriptor(new ElementName(invalidAttributeName), invalidAttributeValueType, "InvalidAttributeDescription");
        TagDescriptor descriptor = new TagDescriptor(new ElementName(name), valueType, description, Set.of(validAttribute), Collections.emptySet());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Tag(descriptor, new ElementValue("Value", valueType),
                        List.of(new Attribute(invalidAttribute, new ElementValue("Value", valueType))), Collections.emptyList()));
        assertEquals("Attribute %s not found in descriptor %s.".formatted(invalidAttribute.name().name(), descriptor.name().name()), exception.getMessage());
    }

    @Test
    void attributeByElementName() {
        AttributeDescriptor attrDesc = new AttributeDescriptor(new ElementName("attr"), ValueType.STRING, "desc");
        TagDescriptor tagDesc = new TagDescriptor(new ElementName("tag"), ValueType.STRING, "desc", Set.of(attrDesc), Collections.emptySet());
        Attribute attr = new Attribute(attrDesc, "val");
        Tag tag = new Tag(tagDesc, new ElementValue("val", ValueType.STRING), List.of(attr), Collections.emptyList());

        assertTrue(tag.attribute(new ElementName("attr")).isPresent());
        assertEquals(attr, tag.attribute(new ElementName("attr")).get());
    }

    @Test
    void attributeByString() {
        AttributeDescriptor attrDesc = new AttributeDescriptor(new ElementName("attr2"), ValueType.STRING, "desc2");
        TagDescriptor tagDesc = new TagDescriptor(new ElementName("tag2"), ValueType.STRING, "desc2", Set.of(attrDesc), Collections.emptySet());
        Attribute attr = new Attribute(attrDesc, "val2");
        Tag tag = new Tag(tagDesc, new ElementValue("val2", ValueType.STRING), List.of(attr), Collections.emptyList());

        assertTrue(tag.attribute("attr2").isPresent());
        assertEquals(attr, tag.attribute("attr2").get());
    }

    @Test
    void attributeShouldReturnEmptyIfNotFound() {
        AttributeDescriptor attrDesc = new AttributeDescriptor(new ElementName("attr3"), ValueType.STRING, "desc3");
        TagDescriptor tagDesc = new TagDescriptor(new ElementName("tag3"), ValueType.STRING, "desc3", Set.of(attrDesc), Collections.emptySet());
        Tag tag = new Tag(tagDesc, new ElementValue("val3", ValueType.STRING), List.of(), Collections.emptyList());

        assertTrue(tag.attribute("notfound").isEmpty());
        assertTrue(tag.attribute(new ElementName("notfound")).isEmpty());
    }
}
