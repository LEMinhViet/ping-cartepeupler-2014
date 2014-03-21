package upmc.ping.Utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLUtils {
	private static int id = -1;
	
	public static void writeXMLObject(String path, String name, String tag, String model, double ry, String version, double x, double y, double z) {
		try {
			id++;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// Root elements
			Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);
			Element rootElement = doc.createElement(tag);
			doc.appendChild(rootElement);
						
			rootElement.setAttributeNode(addAttribut(doc, "action", 				"void"));
			rootElement.setAttributeNode(addAttribut(doc, "actionDist", 			"10.0"));
			rootElement.setAttributeNode(addAttribut(doc, "collidable", 			"true"));
			rootElement.setAttributeNode(addAttribut(doc, "id", 					id + ""));
			rootElement.setAttributeNode(addAttribut(doc, "model", 					model));
			rootElement.setAttributeNode(addAttribut(doc, "pCameraFacing", 			"true"));
			rootElement.setAttributeNode(addAttribut(doc, "pControlFlow", 			"false"));
			rootElement.setAttributeNode(addAttribut(doc, "pEmitH", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEmitInnerRadius", 		"0.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEmitOutterRadius", 		"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEmitType", 				"0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEmitW", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEndA", 					"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEndB",					"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEndMass", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEndR", 					"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEndSize", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pEndV", 					"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pFactoryNumber", 		"500"));
			rootElement.setAttributeNode(addAttribut(doc, "pInitialVelocity", 		"0.0030"));
			rootElement.setAttributeNode(addAttribut(doc, "pMaxAngle", 				"10.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pMaxLife", 				"2000.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pMinAngle", 				"0.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pMinLife", 				"1000.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pParticulesPerSecVar", 	"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pParticulesPerSeconds", 	"100"));
			rootElement.setAttributeNode(addAttribut(doc, "pSpeed", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pStartA", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pStartB", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pStartMass", 			"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pStartR", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pStartSize", 			"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "pStartV", 				"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "rx", 					"0.0"));
			rootElement.setAttributeNode(addAttribut(doc, "ry", 					ry + ""));
			rootElement.setAttributeNode(addAttribut(doc, "rz", 					"0.0"));
			rootElement.setAttributeNode(addAttribut(doc, "s", 						"1.0"));
			rootElement.setAttributeNode(addAttribut(doc, "type", 					"basic"));
			rootElement.setAttributeNode(addAttribut(doc, "versionCode",			version));
			rootElement.setAttributeNode(addAttribut(doc, "x", 						x + ""));
			rootElement.setAttributeNode(addAttribut(doc, "y", 						y + ""));
			rootElement.setAttributeNode(addAttribut(doc, "z", 						z + ""));			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(path + "\\" + name + id + ".xml");
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	public static Attr addAttribut(Document doc, String tag, String value) {
		Attr attr = doc.createAttribute(tag);
		attr.setValue(value);
		return attr;
	}
}
