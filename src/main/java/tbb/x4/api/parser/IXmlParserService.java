package tbb.x4.api.parser;

import java.io.FileInputStream;
import java.util.List;

public interface IXmlParserService {
    List<Tag> parseFile(FileInputStream inputStream);
}
