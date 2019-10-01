package uk.yapily.facts.parser;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.yapily.facts.client.RandomUselessFactsClient;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

public class XmlParser {
    public static String getXmlValueFromTag(String xml, String tagName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource sourceXml = new InputSource();
        sourceXml.setCharacterStream(new StringReader(xml));

        Document doc = builder.parse(sourceXml);

        return doc.getElementsByTagName(tagName).item(0).getTextContent();
    }
}
