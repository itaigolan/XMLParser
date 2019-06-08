package xmlparser;

import Threads.Parser;
import Threads.WatchThread;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class XMLParser {

    public static void main(String[] args) {
  
        Connection connection = null;
        Parser parser = null;
        String connectionUrl = "jdbc:mysql://localhost:3306/quintrixbankdb";
        
        //Connect to database
        try {
            connection = DriverManager.getConnection(connectionUrl,"root",/*password*/);
            System.out.println("Connected");
        }
        catch (SQLException e) {
            e.printStackTrace();
            }
        
        ThreadFactory factory = Executors.defaultThreadFactory();
        //Creates executor to execute Parsers
        ExecutorService executor = Executors.newFixedThreadPool(10, factory);

        WatchThread watcher = new WatchThread("C:\\itai\\Jobs\\CapGemini\\Training\\Projects\\Project1\\XMLFiles");
        do{
            watcher.start();
            if(watcher.hasEvents()){
                try {
                    parser = new Parser(connection);
                } catch (ParserConfigurationException | SAXException ex) {
                    Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Sets the event of the parser to the first event in the watcher's eventList
                parser.setEvent(watcher.getEvent());
                //Submits parser for execution
                executor.execute(parser);
            }
        }while(true);
    }    
}
