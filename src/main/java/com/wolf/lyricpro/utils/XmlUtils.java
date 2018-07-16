package com.wolf.lyricpro.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

public class XmlUtils {
    public static XMLStreamReader parseFileToStaxCursor(InputStream is)
            throws XMLStreamException {
        XMLInputFactory fact = XMLInputFactory.newInstance();
        XMLStreamReader reader = fact.createXMLStreamReader(is, "UTF-8");
        return reader;
    }


    public static String getNodeStaxValue(XMLStreamReader reader,
                                          String elementName, String namespaceURI, String attrName)
            throws XMLStreamException {

        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagname = reader.getLocalName();
                    if (tagname.equals(elementName)) {
                        String result = reader.getAttributeValue(namespaceURI,
                                attrName);

                        return result;
                    }
                }
            }
        }
        return null;
    }

    public static boolean checkValidateWithSchema(XMLStreamReader reader, int code) {
        String schemaPath = "";
        if (code == 1) {
            schemaPath = "src/main/webapp/WEB-INF/xsd/songMetro.xsd";
        }
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(schemaPath));

            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(reader));

            System.out.println("Document is valid");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Document is not valid");
            return false;
        }
    }

    public static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
