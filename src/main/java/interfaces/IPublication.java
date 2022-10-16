/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import org.w3c.dom.Document;
import org.json.JSONObject;

/**
 *
 * @author AP57630
 */
public interface IPublication {
    enum Format {
        XML, JSON;
    }
    public void fromXMLtoCanonical(Document xmlDocument);
    public Document fromCanonicaltoXML ();
    public JSONObject fromCanonicaltoJSON ();
    public void fromJSONtoCanonical (JSONObject json);
}
