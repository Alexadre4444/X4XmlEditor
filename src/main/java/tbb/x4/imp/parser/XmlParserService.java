package tbb.x4.imp.parser;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tbb.x4.api.model.IDescriptorRepository;
import tbb.x4.api.parser.IXmlParserService;
import tbb.x4.api.parser.ParsedFile;
import tbb.x4.api.parser.ParsingException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;

@ApplicationScoped
public class XmlParserService implements IXmlParserService {
    private final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    private final IDescriptorRepository descriptorRepository;

    @Inject
    public XmlParserService(IDescriptorRepository descriptorRepository) {
        this.descriptorRepository = descriptorRepository;
    }

    @Override
    public ParsedFile parseFile(FileInputStream inputStream) {
        try {
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(inputStream);
            Parser parser = new Parser(descriptorRepository, reader);
            return new ParsedFile(parser.parse());
        } catch (XMLStreamException e) {
            throw new ParsingException(e);
        }
    }

}
