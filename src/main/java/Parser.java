import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import container.Element;
import handler.THandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import util.Loader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Andrey
 * Date: 04.05.12
 * Time: 13:58
 */
public class Parser {

    public static void main(String argv[]) {
        String file;
        int action = 0;
        long time = System.currentTimeMillis();
        if (argv != null && argv.length != 0) {
            file = (argv[0] != null) ? argv[0] : "d:\\availability.xml";
        } else
            //file = "d:\\availability.xml";
            return;
        if (argv != null && argv.length > 1) {
            try {
                action = Integer.parseInt(argv[1]);
            } catch (NumberFormatException e) {

            }
        }
        //String table_name = argv[2];
        debug(file);
        debug("start:" + action);

        if (action == 0) files(file);
        if (action == 1) time = parseAvailabilyty(file);
        if (action == 2) parseManyXml(files(file), file);


        debug("Finish time :" + (System.currentTimeMillis() - time) / 1000);
    }

    private static long parseAvailabilyty(String file) {
        long time = System.currentTimeMillis();
        try {
            String path = "out.txt";

            File f;
            f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            //final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8"));
            final BufferedWriter bw = new BufferedWriter(new FileWriter(path));

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                String current;
                StringBuffer sb = new StringBuffer();
                String tmp;
                String firstTag;
                String secondTag;

                public void startElement(String uri, String localName, String qName,
                                         Attributes attributes) throws SAXException {
                    if (firstTag != null && secondTag == null) secondTag = qName;
                    if (firstTag == null) firstTag = qName;
                    current = qName;
                    if (qName.equals(secondTag)) {
                        sb = new StringBuffer();
                    }

                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {
                    if (qName.equals(secondTag)) {
                        try {
                            String s = sb.toString().trim().replace('\n', ' ');
                            bw.write(s.substring(0, s.length() - 5));
                            bw.newLine();
                        } catch (IOException e) {
                        }
                        //pw.flush();
                        //pw.println(sb.toString());
                    } else {
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
        return time;
    }

    private static long parseManyXml(String[] file, final String path) {
        long time = System.currentTimeMillis();
        String outFile = path + "/" + "out.txt";

        File f;
        f = new File(outFile);
        if (f.exists()) {
            f.delete();
        }
        try {
            //final BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();


//            DefaultHandler handler1 = new DefaultHandler() {
//                String current;
//                StringBuffer sb = new StringBuffer();
//                String tmp;
//                String firstTag;
//                int index = 1;
//                List<String> column = new ArrayList<String>();
//
//                public void startElement(String uri, String localName, String qName,
//                                         Attributes attributes) throws SAXException {
//                    if (firstTag == null) firstTag = qName;
//                    current = qName;
//                    if (qName.equals(firstTag)) {
//                        sb = new StringBuffer();
//                    }
//                    int length = attributes.getLength();
//
//                    for (int i = 0; i < length; i++) {
//                        String name = attributes.getQName(i);
//                        sb.append(name + "_" + index + ",");
//                        column.add(name + "_" + index );
//                        index++;
//                    }
//
//                }
//
//                public void endElement(String uri, String localName,
//                                       String qName) throws SAXException {
//                    if (qName.equals(firstTag)) {
//                        try {
//                            String s = sb.toString().trim().replace('\n', ' ');
//                            bw.write(s.substring(0, s.length() - 1));
//                            bw.newLine();
//                            //debug(s);
//                        } catch (IOException e) {
//                        }
//                        util.Loader.createTable(column,table_name);
//                    } else {
//                        if (qName.equals(current)) {
//                            sb.append(current + "_" + index);
//                            sb.append(",");
//                            column.add(current + "_" + index);
//                            //debug(column.toString());
//                            index++;
//                        }
//                    }
//
//
//                }
//
//                public void characters(char ch[], int start, int length) throws SAXException {
//                    //sb.append(current+",");
//                    tmp = current;
//
//                }
//            };
//            saxParser.parse(path+"/"+file[0], handler1);
//

            int id = 0;
            THandler th = new THandler();
            th.setPath(path);
            for (String item : file) {
                id++;
                try {
                    if (item.equals("out.txt")) continue;
                    debug("current:" + item+" id:"+id);

                    th.setFinalId(id);
                    saxParser.parse(path + "/" + item, th);
//                    bw.flush();


                } catch (SAXParseException e) {
                    //e.printStackTrace();
                    debug("SAXParseException");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String[] files(String path) {
        String list[] = new File(path).list();
        return list;
    }


    static void debug(String s) {
        System.out.println(s);
    }
}
