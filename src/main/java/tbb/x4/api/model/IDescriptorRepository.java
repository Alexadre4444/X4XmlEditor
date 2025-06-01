package tbb.x4.api.model;

import java.util.Optional;

/**
 * Interface for accessing descriptor information for tags and attributes.
 * Provides methods to retrieve the descriptor of a tag or an attribute
 * based on their respective {@link ElementName}.
 */
public interface IDescriptorRepository {
    /**
     * Retrieves the descriptor for a tag based on its {@link ElementName}.
     *
     * @param tagName the name of the tag
     * @return the {@link TagDescriptor} associated with the tag
     */
    Optional<TagDescriptor> getTagDescriptor(ElementName tagName);

    /**
     * Retrieves the descriptor for an unknown tag based on its {@link ElementName}.
     *
     * @param elementName the name of the unknown tag
     * @return the {@link TagDescriptor} for an unknown tag
     */
    TagDescriptor getUnknownTagDescriptor(ElementName elementName);

    /**
     * Retrieves the descriptor for an unknown attribute based on its {@link ElementName}.
     *
     * @param elementName the name of the attribute
     * @return the {@link AttributeDescriptor} associated with the attribute
     */
    AttributeDescriptor getUnknowAttributeDescriptor(ElementName elementName);
}