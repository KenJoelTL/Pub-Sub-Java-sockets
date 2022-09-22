package Classes;


import Interfaces.IPublication;
import java.util.Map;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
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

    private Topic topic;
    private Map<String,String> content;

    @Override
    public void fromXMLtoCanonical(Document xmlDocument) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Document fromCanonicaltoXML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject fromCanonicaltoJSON() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fromJSONtoCanonical(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
