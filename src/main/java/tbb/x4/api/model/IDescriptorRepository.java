package tbb.x4.api.model;

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
    TagDescriptor getTagDescriptor(ElementName tagName);

    /**
     * Retrieves the descriptor for an attribute based on its {@link ElementName}.
     *
     * @param attributeName the name of the attribute
     * @return the {@link AttributeDescriptor} associated with the attribute
     */
    AttributeDescriptor getAttributeDescriptor(ElementName attributeName);
}