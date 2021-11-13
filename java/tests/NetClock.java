import java.io.*;
import java.net.*;
import java.util.*;

public class NetClock {
    
    public static void main(String args[])
    {
        ServerSocket servsock = null;
        Socket sock = null;;
        OutputStream out;
        String outstr;
        int i;
        Date d;

        try
        {
            System.out.println("Hello");
            servsock = new ServerSocket(10080, 300);
            while(true)
            {
                sock = servsock.accept();
                System.out.println("[*] socker accept.");
                outstr = "hello";
                out = sock.getOutputStream();
                for(i = 0; i < outstr.length(); i++)
                {
                    out.write((int)outstr.charAt(i));
                }
                out.write('\n');
                sock.close();
            }
        }
        catch(IOException e)
        {
            System.exit(1);
        }
    }

}
