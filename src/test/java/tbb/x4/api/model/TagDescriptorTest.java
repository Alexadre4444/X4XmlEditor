package tbb.x4.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TagDescriptorTest {

    @ParameterizedTest
    @CsvSource({
            "ValidName, STRING, ValidDescription",
            "AnotherName, NUMBER, AnotherDescription"
    })
    void validTagDescriptorShouldCreateInstance(String name, ValueType valueType, String description) {
        TagDescriptor descriptor = new TagDescriptor(
                new ElementName(name),
                valueType,
                description,
                Collections.emptySet(),
                Collections.emptySet()
        );

        assertNotNull(descriptor);
        assertEquals(name.toLowerCase(), descriptor.name().name());
        assertEquals(valueType, descriptor.valueType());
        assertEquals(description, descriptor.description());
    }

    @ParameterizedTest
    @NullSource
    void nullNameShouldThrowException(ElementName name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(name, ValueType.STRING, "ValidDescription", Collections.emptySet(), Collections.emptySet()));
        assertEquals("Name cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void nullValueTypeShouldThrowException(ValueType valueType) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(new ElementName("ValidName"), valueType, "ValidDescription", Collections.emptySet(), Collections.emptySet()));
        assertEquals("ValueType cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    void nullDescriptionShouldThrowException(String description) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(new ElementName("ValidName"), ValueType.STRING, description, Collections.emptySet(), Collections.emptySet()));
        assertEquals("Description cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "NonParentName, STRING, NonParentDescription"
    })
    void nonParentTagDescriptorWithNonEmptySubTagsShouldThrowException(String name, ValueType valueType, String description) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(
                        new ElementName(name),
                        valueType,
                        description,
                        Collections.emptySet(),
                        Set.of(new TagDescriptor(new ElementName("ChildTag"), ValueType.STRING, "ChildDescription", Collections.emptySet(), Collections.emptySet()))
                ));
        assertEquals("SubTags must be empty if ValueType is not PARENT", exception.getMessage());
    }

    @CsvSource({
            "ParentName, PARENT, ParentDescription"
    })
    @ParameterizedTest
    void parentTagDescriptorWithEmptySubTagsShouldThrowException(String name, ValueType valueType, String description) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TagDescriptor(
                        new ElementName(name),
                        valueType,
                        description,
                        Collections.emptySet(),
                        Collections.emptySet()
                ));
        assertEquals("SubTags cannot be empty if ValueType is PARENT", exception.getMessage());
    }

    @CsvSource({
            "ParentName, PARENT, ParentDescription",
            "ParentName, UNKNOWN, ParentDescription"
    })
    @ParameterizedTest
    void validParentTagDescriptorShouldCreateInstance(String name, ValueType valueType, String description) {
        TagDescriptor descriptor = new TagDescriptor(
                new ElementName(name),
                valueType,
                description,
                Collections.emptySet(),
                Set.of(new TagDescriptor(new ElementName("ChildTag"), ValueType.STRING, "ChildDescription", Collections.emptySet(), Collections.emptySet()))
        );

        assertNotNull(descriptor);
        assertEquals(name.toLowerCase(), descriptor.name().name());
        assertEquals(valueType, descriptor.valueType());
        assertEquals(description, descriptor.description());
    }

    @ParameterizedTest
    @ValueSource(strings = {"UNKNOWN"})
    void unknownValueTypeShouldCreateInstance(String unknownValueType) {
        assertDoesNotThrow(() -> new TagDescriptor(new ElementName("ValidName"), ValueType.valueOf(unknownValueType), "ValidDescription", Collections.emptySet(), Collections.emptySet()));
    }

    @Test
    void idTagDescriptorShouldBeInAttributes() {
        AttributeDescriptor idAttr = new AttributeDescriptor(new ElementName("id"), ValueType.STRING, "desc");
        Set<AttributeDescriptor> attrs = Set.of(idAttr);
        TagDescriptor tag = new TagDescriptor(
                new ElementName("withId"),
                ValueType.STRING,
                "desc",
                idAttr,
                attrs,
                Collections.emptySet()
        );
        assertNotNull(tag);
        assertEquals(idAttr, tag.idTagDescriptor());
        assertTrue(tag.attributes().contains(idAttr));
    }
}
