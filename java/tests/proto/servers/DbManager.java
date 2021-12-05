package servers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.UnaryOperator;
import java.util.Map;
import java.util.HashMap;

public class DbManager
{
    Connection connection = null;
    PreparedStatement pstmt = null;
    CallableStatement cs = null;
    ResultSet result = null;
    private Map<String, String> dbServerInfos = new HashMap<String, String>();

    public DbManager()
    {
        IoUtil.readFileServerInfo(dbServerInfos);
    }

    private boolean execSql(UnaryOperator<Boolean> callback)
    {
        var ret = false;
        try
        {
            var user = IoUtil.getValueStr(dbServerInfos, "user");
            var password = IoUtil.getValueStr(dbServerInfos, "password");
            var host = IoUtil.getValueStr(dbServerInfos, "host");
            var port = IoUtil.getValueStr(dbServerInfos, "port");
            IoUtil.Log("[*] attempt to connect DBServer host:" + host + ", port:" + port);

            String url = "jdbc:mysql://" + host + ":" + port + "/CANVAS_DB";
            connection = DriverManager.getConnection(url, user, password);
            ret = callback.apply(connection == null ? false : true);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            closeConnection();
        }
        return ret;
    }

    private void closeConnection()
    {
        try
        {
            if(connection != null){ connection.close(); }
            if(cs != null){ cs.close();}
        }
        catch(SQLException e)
        { 
            e.printStackTrace(); 
        }
    }

    private Boolean registUser(Boolean isExec, String user, String mail, String passwd)
    {
        try
        {
            String sql = "{CALL regist_user('"+ user + "', '" + mail + "', '" + passwd + "', ?)}";
            cs = connection.prepareCall(sql);

            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.executeUpdate();
            var ret = cs.getInt(1) == 0 ? true : false;
            if(ret)
            { 
                IoUtil.Log("[*] success registUser query, user_name(" + user + ")");
            }
            else
            {
                IoUtil.Log("[!!!] fail registUser query, user_name(" + user + ")");
            }
            return ret;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean execRegistUserSql(String user, String mail, String passwd)
    {
        return execSql(x -> { return registUser(x, user, mail, passwd); });
    }

    public void execRegistUserSqlTest()
    {
        for(int i=0; i < 100; i++)
        {
            String no = Integer.toString(i);
            String user = "user" + no;
            String mail = no + "@gmail.com";
            String passwd = "1111111" + no;
            System.out.println("user: " + user + ", mail: " + mail + ", passwd: " + passwd);
            execRegistUserSql(user, mail, passwd);
        }
    }



}