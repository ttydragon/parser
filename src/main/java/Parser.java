import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
            //final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8"));
            final BufferedWriter  bw = new BufferedWriter(new FileWriter(path));

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                String current;
                StringBuffer sb = new StringBuffer();
                String tmp;

                public void startElement(String uri, String localName, String qName,
                                         Attributes attributes) throws SAXException {

                    current = qName;
                    if (qName.equals("offer")) {
                        sb = new StringBuffer();
                    }

                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {
                    if (qName.equals("offer")) {  //todo find second elements
                        try{
                            bw.write(sb.toString().trim().replace('\n',' ') +"\n");
                            bw.newLine();
                        }catch (IOException e){}
                        //pw.flush();
                        //pw.println(sb.toString());
                    }else{
                        sb.append(",");
                    }


                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    //if(tmp.equals(current))
                    sb.append(ch, start, length);
                    tmp = current;

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
    }


    static void debug(String s) {
        System.out.println(s);
    }
}
