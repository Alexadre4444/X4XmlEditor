package tbb.x4.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {
    @ParameterizedTest
    @CsvSource({
            "ParentName, PARENT, ParentDescription, ChildName, STRING, ChildDescription",
            "AnotherParent, PARENT, AnotherDescription, SubTag, NUMBER, SubTagDescription"
    })
    void validParentTagDescriptorWithSubTagsShouldCreateInstance(String name, ValueType valueType, String description, String childName, ValueType childValueType, String childDescription) {
        TagDescriptor childTag = new TagDescriptor(new ElementName(childName), childValueType, childDescription, Collections.emptySet(), Collections.emptySet());
        TagDescriptor descriptor = new TagDescriptor(new ElementName(name), valueType, description, Collections.emptySet(), Set.of(childTag));

        assertNotNull(descriptor);
        assertEquals(name, descriptor.name().name());
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
        assertEquals(name, descriptor.name().name());
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
                () -> new TagDescriptor(new ElementName(name), valueType, description, null, Collections.emptySet()));
        assertEquals("Attributes cannot be null", exception.getMessage());
    }

    @Test
    void tagWithNullDescriptorShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Tag(null, null, Collections.emptySet(), Collections.emptySet()));
        assertEquals("TagDescriptor cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "STRING"
    })
    void tagWithStringValueTypeAndNullValueShouldThrowException(ValueType valueType) {
        TagDescriptor descriptor = new TagDescriptor(new ElementName("TestName"), valueType, "TestDescription", Collections.emptySet(), Collections.emptySet());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Tag(descriptor, null, Collections.emptySet(), Collections.emptySet()));
        assertEquals("Value cannot be null for non-PARENT type", exception.getMessage());
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
                        Collections.emptySet(), Set.of(new Tag(invalidSubTag, new ElementValue("1", invalidSubTagValueType),
                        Collections.emptySet(), Collections.emptySet()))));
        assertEquals("SubTag %s not found in descriptor %s.".formatted(invalidSubTag.name().name(), descriptor.name().name()), exception.getMessage());
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
                        Set.of(new Attribute(invalidAttribute, new ElementValue("Value", valueType))), Collections.emptySet()));
        assertEquals("Attribute %s not found in descriptor %s.".formatted(invalidAttribute.name().name(), descriptor.name().name()), exception.getMessage());
    }
}
