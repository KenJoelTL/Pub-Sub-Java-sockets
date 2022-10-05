package Classes;


import Interfaces.IPublication;
import java.util.Map;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AP57630
 */
public class Publication implements IPublication{
    // Le format Canonique est à changer pour qu'il prend en compte les types autres que STRING
    private Topic topic;
    private Map<String,String> content;

    public Publication(Topic topic) {
        this.topic = topic;
    }
    public Publication(Topic topic, Document xmlDocument) {
        this.topic = topic;
        this.fromXMLtoCanonical(xmlDocument);
    }
    public Publication(Topic topic, JSONObject json) {
        this.topic = topic;
        this.fromJSONtoCanonical(json);
    }

    @Override
    public void fromXMLtoCanonical(Document xmlDocument) {
        // var publicationNode = xmlDocument.getElementsByTagName("publication");
        // var message = publicationNode.item(0);
        var publication = xmlDocument.getFirstChild();
        var message = publication.getFirstChild();
        while(message != null) {
            content.put(message.getNodeName(), message.getNodeValue());
            message = message.getNextSibling();
        }
    }

    @Override
    public Document fromCanonicaltoXML() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDoc = dBuilder.newDocument();

            // Création de la balise racine
            Element rootElement = xmlDoc.createElement("publication");
            xmlDoc.appendChild(rootElement);

            // Ajout du contenu de la publication dans des balises de 2e niveau
            for (Map.Entry<String,String> entry : content.entrySet()) {
                Element element = xmlDoc.createElement(entry.getKey());
                element.appendChild(xmlDoc.createTextNode(entry.getValue()));
                rootElement.appendChild(element);
            }
            return xmlDoc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public JSONObject fromCanonicaltoJSON() {
        JSONObject jsonMessage = new JSONObject();
        for (Map.Entry<String,String> entry : content.entrySet()) {
            jsonMessage.put(entry.getKey(), entry.getValue());
        }
        return jsonMessage;
    }

    @Override
    public void fromJSONtoCanonical(JSONObject json) {
        this.content.clear();
        for (String key : json.keySet()) {
            this.content.put(key, json.getString(key));
        }
    }

}
