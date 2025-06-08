package tbb.x4.api.parser;

import java.io.FileInputStream;

public interface IXmlParserService {
    /**
     * Parses an XML file from the given input stream and returns a list of tags.
     *
     * @param inputStream the input stream of the XML file to parse
     * @return a list of tags parsed from the XML file
     * @throws ParsingException if an error occurs during parsing
     */
    ParsedFile parseFile(FileInputStream inputStream);
}
