package handler;

import container.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import util.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Andrey
 * Date: 22.05.12
 * Time: 19:53
 */
public class THandlerBig extends DefaultHandler {
    String current;
    String full_path = "";
    //                        StringBuffer sb = new StringBuffer();
    StringBuffer itemSb = new StringBuffer();
    //                        String tmp;
    String firstTag;
    String secondTag;
    List<Element> element = new ArrayList<Element>();
    Element tmpElement = new Element();
    int index = 0;
    int finalId;
    String path;
    String table;

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (firstTag != null && secondTag == null)
            secondTag = qName;
        if (firstTag == null) {
            firstTag = qName;

            full_path += qName;
        } else {
            full_path += "_" + qName;
        }

        current = qName;

        itemSb = new StringBuffer();

        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String value = attributes.getValue(i);
            String name = attributes.getQName(i);
            index++;
            element.add(new Element(finalId, path, index, full_path + "_" + name, value));

        }
    }

    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        if (qName.equals(secondTag)) {
            Loader.bigInsertLine(element, table);
            element.clear();
            index = 0;
            finalId++;
        } else {
            String value = (itemSb.toString().trim().replace('\n', ' '));
            itemSb = new StringBuffer();
            index++;
            element.add(new Element(finalId, path, index, full_path, value));
        }

        if (qName.equals(firstTag)) {
            full_path = "";
            firstTag = null;
        } else {
            full_path = full_path.substring(0, (full_path.length() - qName.length() - 1));
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        itemSb.append(ch, start, length);

    }

    public void setFinalId(int finalId) {
        this.finalId = finalId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTable(String table) {
        this.table = table;
    }


    static void debug(String msg) {
        System.out.println(msg);
    }

}
