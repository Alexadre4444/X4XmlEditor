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
    void constructor_expectedCreateInstance_ifParentTagDescriptorWithSubTagsValid(String name, ValueType valueType, String description, String childName, ValueType childValueType, String childDescription) {
        TagDescriptor childTag = new TagDescriptor(new ElementName(childName), childValueType, childDescription, Collections.emptySet(), Collections.emptySet());
        TagDescriptor descriptor = new TagDescriptor(new ElementName(name), valueType, description, Collections.emptySet(), Set.of(childTag));

        assertNotNull(descriptor);
        assertEquals(name.toLowerCase(), descriptor.name().name());
        assertEquals(valueType, descriptor.valueType());
        assertEquals(description, descriptor.description());
        if (!ValueType.UNKNOWN.equals(valueType)) {
            assertEquals(1, descriptor.subTags().size());
            assertTrue(descriptor.subTags().contains(childTag));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "ValidName, STRING, ValidDescription, AttributeName, STRING, AttributeDescription",
            "AnotherName, NUMBER, AnotherDescription, AnotherAttribute, NUMBER, AnotherAttributeDescription",
            "UnknownName, UNKNOWN, UnknownDescription, UnknownAttribute, UNKNOWN, UnknownAttributeDescription"
    })
    void constructor_expectedCreateInstance_ifTagDescriptorWithAttributesValid(String name, ValueType valueType, String description, String attributeName, ValueType attributeValueType, String attributeDescription) {
        AttributeDescriptor attribute = new AttributeDescriptor(new ElementName(attributeName), attributeValueType, attributeDescription);
        TagDescriptor descriptor = new TagDescriptor(new ElementName(name), valueType, description, Set.of(attribute), Collections.emptySet());

        assertNotNull(descriptor);
        assertEquals(name.toLowerCase(), descriptor.name().name());
        assertEquals(valueType, descriptor.valueType());
        assertEquals(description, descriptor.description());
        if (!ValueType.UNKNOWN.equals(valueType)) {
            assertEquals(1, descriptor.attributes().size());
            assertTrue(descriptor.attributes().contains(attribute));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "ParentName, PARENT, ParentDescription",
            "AnotherParent, PARENT, AnotherDescription"
    })
    void constructor_expectedException_ifParentTagDescriptorWithNullSubTags(String name, ValueType valueType, String description) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(new ElementName(name), valueType, description, Collections.emptySet(), null));
        assertEquals("SubTags cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "ValidName, STRING, ValidDescription",
            "AnotherName, NUMBER, AnotherDescription"
    })
    void constructor_expectedException_ifTagDescriptorWithNullAttributes(String name, ValueType valueType, String description) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(new ElementName(name), valueType, description, null, null, Collections.emptySet()));
        assertEquals("Attributes cannot be null", exception.getMessage());
    }

    @Test
    void constructor_expectedException_ifTagWithNullDescriptor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Tag(null, null, Collections.emptyList(), Collections.emptyList()));
        assertEquals("TagDescriptor cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "ParentName, PARENT, ParentDescription, InvalidSubTagName, STRING",
            "AnotherParent, PARENT, AnotherDescription, AnotherInvalidSubTag, NUMBER"
    })
    void constructor_expectedException_ifTagWithInvalidSubTags(String name, ValueType valueType, String description, String invalidSubTagName, ValueType invalidSubTagValueType) {
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
    void constructor_expectedNoException_ifTagWithUnknownSubTag() {
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
    void constructor_expectedNoException_ifTagWithUnknownAttribute() {
        AttributeDescriptor unknownAttribute = new AttributeDescriptor(new ElementName("unknownAttribute"),
                ValueType.UNKNOWN, "unknownAttribute");
        TagDescriptor validSubTag = new TagDescriptor(new ElementName("test4"), ValueType.STRING, "validSubTag",
                Collections.emptySet(), Collections.emptySet());

        assertDoesNotThrow(() -> new Tag(validSubTag, null, List.of(new Attribute(unknownAttribute, "5")),
                List.of()));
    }

    @ParameterizedTest
    @CsvSource({
            "ValidName, STRING, ValidDescription, InvalidAttributeName, STRING",
            "AnotherName, STRING, AnotherDescription, AnotherInvalidAttribute, STRING"
    })
    void constructor_expectedException_ifTagWithInvalidAttributes(String name, ValueType valueType, String description, String invalidAttributeName, ValueType invalidAttributeValueType) {
        AttributeDescriptor validAttribute = new AttributeDescriptor(new ElementName("ValidAttribute"), valueType, "ValidAttributeDescription");
        AttributeDescriptor invalidAttribute = new AttributeDescriptor(new ElementName(invalidAttributeName), invalidAttributeValueType, "InvalidAttributeDescription");
        TagDescriptor descriptor = new TagDescriptor(new ElementName(name), valueType, description, Set.of(validAttribute), Collections.emptySet());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Tag(descriptor, new ElementValue("Value", valueType),
                        List.of(new Attribute(invalidAttribute, new ElementValue("Value", valueType))), Collections.emptyList()));
        assertEquals("Attribute %s not found in descriptor %s.".formatted(invalidAttribute.name().name(), descriptor.name().name()), exception.getMessage());
    }

    @Test
    void attribute_expectedPresent_ifAttributeByElementName() {
        AttributeDescriptor attrDesc = new AttributeDescriptor(new ElementName("attr"), ValueType.STRING, "desc");
        TagDescriptor tagDesc = new TagDescriptor(new ElementName("tag"), ValueType.STRING, "desc", Set.of(attrDesc), Collections.emptySet());
        Attribute attr = new Attribute(attrDesc, "val");
        Tag tag = new Tag(tagDesc, new ElementValue("val", ValueType.STRING), List.of(attr), Collections.emptyList());

        assertTrue(tag.attribute(new ElementName("attr")).isPresent());
        assertEquals(attr, tag.attribute(new ElementName("attr")).get());
    }

    @Test
    void attribute_expectedPresent_ifAttributeByString() {
        AttributeDescriptor attrDesc = new AttributeDescriptor(new ElementName("attr2"), ValueType.STRING, "desc2");
        TagDescriptor tagDesc = new TagDescriptor(new ElementName("tag2"), ValueType.STRING, "desc2", Set.of(attrDesc), Collections.emptySet());
        Attribute attr = new Attribute(attrDesc, "val2");
        Tag tag = new Tag(tagDesc, new ElementValue("val2", ValueType.STRING), List.of(attr), Collections.emptyList());

        assertTrue(tag.attribute("attr2").isPresent());
        assertEquals(attr, tag.attribute("attr2").get());
    }

    @Test
    void attribute_expectedEmpty_ifAttributeNotFound() {
        AttributeDescriptor attrDesc = new AttributeDescriptor(new ElementName("attr3"), ValueType.STRING, "desc3");
        TagDescriptor tagDesc = new TagDescriptor(new ElementName("tag3"), ValueType.STRING, "desc3", Set.of(attrDesc), Collections.emptySet());
        Tag tag = new Tag(tagDesc, new ElementValue("val3", ValueType.STRING), List.of(), Collections.emptyList());

        assertTrue(tag.attribute("notfound").isEmpty());
        assertTrue(tag.attribute(new ElementName("notfound")).isEmpty());
    }

    @Test
    void findIdAttribute_expectedReturnIdAttribute_ifPresent() {
        AttributeDescriptor idAttrDesc = new AttributeDescriptor(new ElementName("id"), ValueType.STRING, "ID attribute");
        AttributeDescriptor otherAttrDesc = new AttributeDescriptor(new ElementName("other"), ValueType.STRING, "Other attribute");
        TagDescriptor tagDescriptor = new TagDescriptor(
                new ElementName("tag"), ValueType.STRING, "desc", idAttrDesc, Set.of(idAttrDesc, otherAttrDesc), Collections.emptySet());
        Attribute idAttr = new Attribute(idAttrDesc, "42");
        Attribute otherAttr = new Attribute(otherAttrDesc, "foo");
        Tag tag = new Tag(tagDescriptor, new ElementValue("val", ValueType.STRING), List.of(idAttr, otherAttr), Collections.emptyList());
        assertTrue(tag.findIdAttribute().isPresent());
        assertEquals(idAttr, tag.findIdAttribute().get());
    }

    @Test
    void findIdAttribute_expectedReturnEmpty_ifNotPresent() {
        AttributeDescriptor idAttrDesc = new AttributeDescriptor(new ElementName("id"), ValueType.STRING, "ID attribute");
        AttributeDescriptor otherAttrDesc = new AttributeDescriptor(new ElementName("other"), ValueType.STRING, "Other attribute");
        TagDescriptor tagDescriptor = new TagDescriptor(
                new ElementName("tag"), ValueType.STRING, "desc", idAttrDesc, Set.of(idAttrDesc, otherAttrDesc), Collections.emptySet());
        Attribute otherAttr = new Attribute(otherAttrDesc, "foo");
        Tag tag = new Tag(tagDescriptor, new ElementValue("val", ValueType.STRING), List.of(otherAttr), Collections.emptyList());
        assertTrue(tag.findIdAttribute().isEmpty());
    }

    @Test
    void find_expectedReturnSubTag_ifSubTagExists() {
        TagDescriptor childDesc = new TagDescriptor(new ElementName("child"), ValueType.STRING, "child desc", Collections.emptySet(), Collections.emptySet());
        TagDescriptor parentDesc = new TagDescriptor(new ElementName("parent"), ValueType.PARENT, "parent desc", Collections.emptySet(), Set.of(childDesc));
        Tag childTag = new Tag(childDesc, new ElementValue("val", ValueType.STRING), Collections.emptyList(), Collections.emptyList());
        Tag parentTag = new Tag(parentDesc, null, Collections.emptyList(), List.of(childTag));
        ElementPath path = new ElementPath(List.of(new ElementName("child")));
        List<Tag> found = parentTag.find(path);
        assertEquals(1, found.size());
        assertEquals(childTag, found.get(0));
    }

    @Test
    void find_expectedReturnEmpty_ifSubTagNotExists() {
        TagDescriptor childDesc = new TagDescriptor(new ElementName("child"), ValueType.STRING, "child desc", Collections.emptySet(), Collections.emptySet());
        TagDescriptor parentDesc = new TagDescriptor(new ElementName("parent"), ValueType.PARENT, "parent desc", Collections.emptySet(), Set.of(childDesc));
        Tag parentTag = new Tag(parentDesc, null, Collections.emptyList(), Collections.emptyList());
        ElementPath path = new ElementPath(List.of(new ElementName("child")));
        List<Tag> found = parentTag.find(path);
        assertTrue(found.isEmpty());
    }

    @Test
    void find_expectedReturnNestedSubTag_ifPathMatches() {
        TagDescriptor grandChildDesc = new TagDescriptor(new ElementName("grandchild"), ValueType.STRING, "grandchild desc", Collections.emptySet(), Collections.emptySet());
        TagDescriptor childDesc = new TagDescriptor(new ElementName("child"), ValueType.PARENT, "child desc", Collections.emptySet(), Set.of(grandChildDesc));
        TagDescriptor parentDesc = new TagDescriptor(new ElementName("parent"), ValueType.PARENT, "parent desc", Collections.emptySet(), Set.of(childDesc));
        Tag grandChildTag = new Tag(grandChildDesc, new ElementValue("val", ValueType.STRING), Collections.emptyList(), Collections.emptyList());
        Tag childTag = new Tag(childDesc, null, Collections.emptyList(), List.of(grandChildTag));
        Tag parentTag = new Tag(parentDesc, null, Collections.emptyList(), List.of(childTag));
        ElementPath path = new ElementPath(List.of(new ElementName("child"), new ElementName("grandchild")));
        List<Tag> found = parentTag.find(path);
        assertEquals(1, found.size());
        assertEquals(grandChildTag, found.get(0));
    }

}
