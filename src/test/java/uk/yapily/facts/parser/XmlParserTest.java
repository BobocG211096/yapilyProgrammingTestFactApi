package uk.yapily.facts.parser;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.*;

public class XmlParserTest {

    private String xmlExample =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<example>This is a test</example>";

    @Test
    public void testXmlParser_getExampleTag() throws IOException, SAXException, ParserConfigurationException {
        String xmlValueFromTag = XmlParser.getXmlValueFromTag(xmlExample, "example");

        assertEquals("This is a test", xmlValueFromTag);
    }

}