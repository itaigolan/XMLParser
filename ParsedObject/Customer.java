package ParsedObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer{
    
    int id;
    String firstName;
    String lastName;
    
    Statement stmt = null;
    
    public Customer(String firstName,String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    //Write object to DB
    public void sendToDB(Connection con){
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO customer_table " 
                    + "VALUES (DEFAULT,'" + firstName + "','"+ lastName + "')");
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getId(Connection con){
        int id = 0;
        String query = "select Id " +
                    "from customer_table where FirstName = '" +firstName+ "'"
                    +" and LastName='" + lastName + "'";
        
        try {
            stmt = con.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            queryResult.first();
            id = queryResult.getInt("Id");
            stmt.close();
            queryResult.close();
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
        
    }
}
