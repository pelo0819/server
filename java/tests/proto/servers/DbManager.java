package servers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbManager
{
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet result = null;

    public DbManager(){}
    
    public void connect()
    {
        try
        {
            String url = "jdbc:mysql://192.168.3.7:3306/CANVAS_DB";
            String user = "";
            String password = "";
            con = DriverManager.getConnection(url, user, password);

            String sql = "select * from user";
            pstmt = con.prepareStatement(sql);

            result = pstmt.executeQuery();

            while(result.next())
            {
                System.out.println(result.getString("user_name"));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {

        }

    }
}