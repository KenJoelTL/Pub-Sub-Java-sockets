package classes;


import interfaces.IPublication;

import java.io.Serializable;

import org.json.JSONObject;
import org.json.XML;

/**
 * @author AP57630
 */
public class Publication implements IPublication, Serializable {

    private Topic topic;
    private String message;
    private IPublication.Format format;

    public Publication(Topic topic, String format, String message) {
        this.topic = topic;
        this.message = message;
        this.format = IPublication.Format.valueOf(format);
    }

    @Override
    public String fromCanonicalToXML() {
        String xmlstr = "";
        if (format.equals(Format.JSON)) {
            xmlstr = XML.toString(message);
        } else {
            xmlstr = message;
        }
        return xmlstr;
    }

    @Override
    public String fromCanonicalToJSON() {
        String jsonString = "";
        if (format.equals(Format.XML)) {
            JSONObject json = XML.toJSONObject(message);
            jsonString = json.toString(4);
        } else {
            jsonString = message;
        }
        return jsonString;
    }
}
