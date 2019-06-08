package ParsedObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account{
    int id;
    int customerId;//The id of the customer that owns the account
    String type;
    String accountNumber;
    String bankOfOrigin;
    
    Statement stmt = null;
    
    public Account(int id, String accNum, String accType, String bankName) {
        this.id = id;
        accountNumber = accNum;
        type = accType;
        bankOfOrigin = bankName;
    }

    //Write object to DB
    public void sendToDB(Connection con){
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO account_table " 
                    + "VALUES (DEFAULT," + customerId + ",'"+ accountNumber + "','" + type + "','"+ bankOfOrigin + "')");
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setCustomerId(int id){
        customerId = id;
    }
}
