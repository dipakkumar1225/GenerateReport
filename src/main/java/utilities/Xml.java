package utilities;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

public class Xml {

    public static Document getDocument(String fileName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fileName);
        return doc;
    }

    public static NodeList getNodeList(Document document, String xpathExpression) throws XPathExpressionException {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        XPathExpression expr = xpath.compile(xpathExpression);
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        return (nodes != null && nodes.getLength() > 0) ? nodes : null;
    }
}
