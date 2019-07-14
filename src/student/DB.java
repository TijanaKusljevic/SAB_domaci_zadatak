package student;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DB {
  
    private static final String database="projekatsab3";
    private static final int port=1433;
    private static final String serverName="localhost";
    
    
  
    private static final String connectionString="jdbc:sqlserver://"+serverName+":"+port+";"+
            "database="+database+";integratedSecurity=true";
    
    private Connection connection;    
    private DB(){
        try {
            connection=DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static DB db=null;
    public static DB getInstance()
    {
        if(db==null)
            db=new DB();
        return db;
    }
    public Connection getConnection() {
        return connection;
    }
    
    
}
