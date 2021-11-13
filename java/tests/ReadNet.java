import java.io.*;
import java.net.*;

public class ReadNet {
    public static void main(String args[])
    {
        byte[] buff = new byte[1024];
        Socket sock = null;
        InputStream instr = null;
        boolean cont = true;

        try
        {
            sock = new Socket(args[0], Integer.parseInt(args[1]));
            instr = sock.getInputStream();
        }
        catch(Exception e)
        {
            System.err.println("[!!!] network error occour.");
            System.exit(1);
        }

        while(cont)
        {
            try
            {
                int n = instr.read(buff);
                System.out.write(buff, 0, n);
            }
            catch(Exception e)
            {
                cont = false;
            }
        }

        try
        {
            instr.close();
        }
        catch(Exception e)
        {
            System.err.println("[!!!] network error");
            System.exit(1);
        }

    }
}
