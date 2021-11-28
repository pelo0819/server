package servers;

import java.beans.Statement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbManager
{
    Connection con = null;
    PreparedStatement pstmt = null;
    CallableStatement cs = null;
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

            String u = "kuma";
            String mail = "";
            String passwd = "hello";

            String sql = "{CALL regist_user('"+ u + "', '" + mail + "', '" + passwd + "', ?)}";
            cs = con.prepareCall(sql);

            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.executeUpdate();

            System.out.println(cs.getInt(1));

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