package tbb.x4.imp.parser;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tbb.x4.api.directory.XDirectory;
import tbb.x4.api.directory.XFsElement;
import tbb.x4.api.model.CollectionDescriptor;
import tbb.x4.api.model.IDescriptorRepository;
import tbb.x4.api.parser.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class X4CollectionsService implements IX4CollectionsServices {
    private final IDescriptorRepository descriptorRepository;
    private final IXmlParserService xmlParserService;

    @Inject
    public X4CollectionsService(IDescriptorRepository descriptorRepository, IXmlParserService xmlParserService) {
        this.descriptorRepository = descriptorRepository;
        this.xmlParserService = xmlParserService;
    }

    @Override
    public List<Collection> getCollections(XDirectory directory) {
        List<CollectionDescriptor> collectionDescriptors = descriptorRepository.getCollectionDescriptors();
        return collectionDescriptors.stream().map(descriptor -> createCollection(descriptor, directory))
                .toList();
    }

    private Collection createCollection(CollectionDescriptor descriptor, XDirectory directory) {
        return directory.find(descriptor.filePath())
                .map(xFsElement -> createCollection(xFsElement, descriptor))
                .orElse(new Collection(descriptor, List.of()));
    }

    private Collection createCollection(XFsElement xFsElement, CollectionDescriptor descriptor) {
        if (xFsElement instanceof XDirectory) {
            throw new IllegalArgumentException("XDirectory cannot be converted to Collection");
        }
        try {
            return parseCollectionFromFile(xFsElement, descriptor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse collection from file: " + xFsElement.path(), e);
        }
    }

    private Collection parseCollectionFromFile(XFsElement xFsElement, CollectionDescriptor descriptor)
            throws IOException {
        try (FileInputStream fileToParse = new FileInputStream(xFsElement.path().toFile())) {
            ParsedFile parsedFile = xmlParserService.parseFile(fileToParse);
            return new Collection(descriptor, extractTags(parsedFile, descriptor));
        }
    }

    private List<Tag> extractTags(ParsedFile parsedFile, CollectionDescriptor descriptor) {
        return parsedFile.find(descriptor.elementPath());
    }
}

