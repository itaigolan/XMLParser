package Threads;

import ParsedObject.Account;
import ParsedObject.Customer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser implements Runnable{
    
    WatchEvent event;
    String fileLocation = "C:\\itai\\Jobs\\CapGemini\\Training\\Projects\\Project1\\XMLFiles";
    
    DocumentBuilderFactory factory;
    DocumentBuilder dBuilder;
    Document doc;
    Connection con;
    Statement stmt;
    
    Account account;
    Customer customer;
    
    public Parser(Connection connection) throws ParserConfigurationException, SAXException{
        factory = DocumentBuilderFactory.newInstance();
        dBuilder = factory.newDocumentBuilder();
        con = connection;
    }
    
    //Set the current event
    public void setEvent(WatchEvent event) {
        this.event = event;
    }
    
    @Override
    public void run(){
        System.out.println(this.toString() + " Event: " + event);

        //Check if the event refers to a new file created
        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
            //Makes a File object for the new file
            File file = new File(fileLocation + "\\" + event.context().toString());

            try {
                doc = dBuilder.parse(file);
            } catch (SAXException | IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
            doc.getDocumentElement().normalize();
            
            //Puts Account nodes from document into nList
            NodeList nList = doc.getElementsByTagName("Account");
            NodeList customers = doc.getElementsByTagName("CustomerDetails");
                    
            //Iterates through nList
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                System.out.println("Current Element :" + nNode.getNodeName());
            
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    int id = Integer.parseInt(element.getElementsByTagName("ACCID").item(0).getTextContent());
                    String accountNumber = element.getElementsByTagName("AccNum").item(0).getTextContent();
                    String type = element.getElementsByTagName("ACCTYPE").item(0).getTextContent();
                    String bankName = element.getElementsByTagName("BankName").item(0).getTextContent();
                    
                    account = new Account(id,accountNumber,type,bankName);
                    
                    Node customerNode = customers.item(i);
                    element = (Element)customerNode;
                    
                    String firstName = element.getElementsByTagName("FirstName").item(0).getTextContent();
                    String lastName = element.getElementsByTagName("LastName").item(0).getTextContent();
                    
                    customer = new Customer(firstName,lastName);
                    customer.sendToDB(con);
                    
                    account.setCustomerId(customer.getId(con));
                    account.sendToDB(con);
                  
                }
            }
            try {
                //Move file to completed folder after parsing is completed
                Path temp = Files.move(Paths.get(file.getPath()),
                        Paths.get(fileLocation + "\\Completed\\" + event.context().toString()));
            } catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
