import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import parser.container.Offer;

import java.io.*;

/**
 * User: Andrey
 * Date: 04.05.12
 * Time: 13:58
 */
public class Parser {

    public static void main(String argv[]) {
        String file;
        if (argv != null && argv.length!=0) {
            file = (argv[0] != null) ? argv[0] : "d:\\availability.xml";
        } else
            file = "d:\\availability.xml";
        debug(file);
        debug("start");
        long time = System.currentTimeMillis();
        try {
            String path = "out.txt";

            File f;
            f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8"));

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                String current;
                Offer offer = new Offer();

                public void startElement(String uri, String localName, String qName,
                                         Attributes attributes) throws SAXException {

                    current = qName;
                    if (qName.equals("offer")) {
                        offer = new Offer();
                    }

                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {
                    if (qName.equals("offer")) {
                        pw.println(offer.toFlatString());        //todo put offers in list & write more then one
                        pw.flush();
                    }

                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    //debug("state current:"+current+":"+(new String(ch, start, length)));
                    if (current.equals("propertyid")) offer.propertiId += (new String(ch, start, length));
                    if (current.equals("arrivaldate")) offer.arrivalDate += (new String(ch, start, length));
                    if (current.equals("departuredate")) offer.departureDate += (new String(ch, start, length));
                    if (current.equals("nights")) offer.nights += (new String(ch, start, length));
                    if (current.equals("baserate")) offer.baserate += (new String(ch, start, length));
                    if (current.equals("totalbaserate")) offer.totalbaserate += (new String(ch, start, length));
                    if (current.equals("offerbaserate")) offer.offerbaserate += (new String(ch, start, length));
                    if (current.equals("offertotalrate")) offer.offertotalrate += (new String(ch, start, length));
                    if (current.equals("currency")) offer.currensy += (new String(ch, start, length));
                    if (current.equals("book_now")) offer.links.book_now += (new String(ch, start, length));
                    if (current.equals("send_enquiry")) offer.links.send_equiry += (new String(ch, start, length));
                    if (current.equals("listing")) offer.links.listing += (new String(ch, start, length));
                }
            };
            saxParser.parse(file, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        debug("Finish time :" + (System.currentTimeMillis() - time) / 1000);
        return;

    }


    static void debug(String s) {
        System.out.println(s);
    }
}
