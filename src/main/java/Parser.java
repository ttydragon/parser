import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import handler.THandlerBig;
import handler.THandlerSmal;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import util.Loader;

import java.io.*;

/**
 * User: Andrey
 * Date: 04.05.12
 * Time: 13:58
 */
public class Parser {

    public static void main(String argv[]) {
        String file;
        int action = 0;
        String table_name="data";
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
        if (argv != null && argv.length >2) {
            table_name = (argv[2] != null) ? argv[2] : "data";
        }
        debug(table_name);
        debug(file);
        debug("start:" + action);

        if (action == 0) files(file);
        if (action == 1) time = parseAvailabilyty(file);
        if (action == 2) {parseManyXml( files(file), file,table_name);Loader.createIndex(table_name);}//parse many xml(files) into table_name
        if (action == 3) {parseBigXml(files(file), file, table_name);Loader.createIndex(table_name);}//parse one big xml(files) into table_name

        debug("Finish time :" + (System.currentTimeMillis() - time) / 1000);
    }

    @Deprecated
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

    private static long parseManyXml(String[] file, final String path, String table) {
        long time = System.currentTimeMillis();

        Loader.createTable(table);
        
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            int id = 0;
            THandlerSmal th = new THandlerSmal();
            th.setPath(path);
            th.setTable(table);
            int counterFiles = file.length/20;
            for (String item : file) {
                
                File fileToRead = new File(path + "/" + item);
                InputStream inputStream= new FileInputStream(fileToRead);
                Reader reader = new InputStreamReader(inputStream,"UTF-8");
                InputSource is = new InputSource(reader);
                is.setEncoding("UTF-8");
                
                id++;
                try {
                    if (item.equals("out.txt")) continue;
                    try{
                        if(id%counterFiles==0)
                            debug("current:" + item+" id:"+id+" speed:"+(id / ( -( time-System.currentTimeMillis() )/1000))+" fps, left " +(file.length-id) );
                    }catch (ArithmeticException e){
                        debug("sorry- progressbar not work");
                    }
                    th.setFinalId(id);
                    //saxParser.parse(path + "/" + item, th);
                    saxParser.parse(is, th);
                    reader.close();
                    inputStream.close();
//                  bw.flush();


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

    private static long parseBigXml(String[] file, final String path, String table) {   //this action requere typical structure in xml
        long time = System.currentTimeMillis();

        Loader.createTable(table);

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            int id = 0;
            THandlerBig th = new THandlerBig();
            th.setPath(path);
            th.setTable(table);
            for (String item : file) {
                id++;
                try {
                    if (item.equals("out.txt")) continue;
                    debug("current:" + item + " id:" + id);
                    th.setFinalId(id);
                    saxParser.parse(path + "/" + item, th);


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
