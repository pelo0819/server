import java.io.*;
import java.net.*;
import java.util.*;

import servers.Ship;
import servers.DbManager;
import common.Constants;

public class MainServer {
	static final int DEFAULT_PORT = 80;
	static ServerSocket serverSocket;
	static Vector<Socket> connections;
	static Vector<int[]> energy_v;
	static Hashtable<String, Ship> userTable = null;
	static Random random = null;

	public static void addConnection(Socket s)
	{
		if (connections == null)
		{
			connections = new Vector<Socket>();
		}
		connections.addElement(s);
	}

	public static void deleteConnection(Socket s)
	{
		if (connections != null)
		{
			connections.removeElement(s);
		}
	}

	public static void loginUser(String name)
	{
		if (userTable == null)
		{
			userTable = new Hashtable<String, Ship>();
		}
		
		if (random == null)
		{
			random = new Random();
		}

		int ix = Math.abs(random.nextInt()) % 256;
		int iy = Math.abs(random.nextInt()) % 256;

		userTable.put(name, new Ship(ix, iy));
		System.out.println("login:" + name);
		System.out.flush();
	}

	public static void logoutUser(String name)
	{
		System.out.println("logout:" + name);
		System.out.flush();
		userTable.remove(name);
	}

	public static void left(String name)
	{
		Ship ship = userTable.get(name);
		ship.left();
		calculation();
	}

	public static void right(String name)
	{
		Ship ship = userTable.get(name);
		ship.right();
		calculation();
	}

	public static void up(String name)
	{
		Ship ship = userTable.get(name);
		ship.up();
		calculation();
	}

	public static void down(String name)
	{
		Ship ship = userTable.get(name);
		ship.down();
		calculation();
	}

	static void calculation()
	{
		if (userTable != null && energy_v != null)
		{
			for (Enumeration<String> users = userTable.keys(); users.hasMoreElements();) 
			{
				String user = users.nextElement().toString();
				Ship ship = userTable.get(user);
				for (Enumeration<int[]> energys = energy_v.elements(); energys.hasMoreElements();) 
				{
					int[] e = energys.nextElement();
					int x = e[0] - ship.getX();
					int y = e[1] - ship.getY();
					double r = Math.sqrt(x * x + y * y);
					if (r < 10)
					{
						energy_v.removeElement(e);
						ship.addPoint(1);
					}
				}
			}
		}
	}

	public static void statInfo(PrintWriter pw)
	{
		System.out.println("[*] receive stat");
		pw.println("ship_info");
		if (userTable != null){
			for (Enumeration<String> users = userTable.keys(); users.hasMoreElements();) 
			{
				String user = users.nextElement().toString();
				Ship ship = userTable.get(user);
				pw.println(user + " " + ship.getX() + " " + ship.getY() + " " + ship.getPoint());
			}
		}
		pw.println(".");
		pw.println("energy_info");
		if (energy_v != null)
		{
			for (Enumeration<int[]> energys = energy_v.elements(); energys.hasMoreElements();) 
			{
				int[] e = energys.nextElement();
				pw.println(e[0] + " " + e[1]);
			}
		}
		pw.println(".");
		pw.flush();
	}

	public static void putEnergy()
	{
		if (energy_v == null)
		{
			energy_v = new Vector<int[]>();
		}

		if (random == null)
		{
			random = new Random();
		}
		
		int[] e = new int[2];
		e[0] = Math.abs(random.nextInt()) % 256;
		e[1] = Math.abs(random.nextInt()) % 256;

		energy_v.addElement(e);
	}

	public static void main(String[] arg)
	{
		try 
		{
			serverSocket = new ServerSocket(DEFAULT_PORT);
			DbManager dbMgr = new DbManager();
			dbMgr.execRegistUserSqlTest();
			
		}
		catch (IOException e){

			System.err.println("can't create server socket.");
			System.exit(1);
		}
		
		Thread et = new Thread()
		{
			public void run()
			{
				while(true){
					try {
						sleep(10000);
					}catch(InterruptedException e){
						break;
					}
					MainServer.putEnergy();
				}
			}
		};
		et.start();
		while (true) 
		{
			try {
				Socket cs = serverSocket.accept();
				addConnection(cs);
				Thread ct = new Thread(new clientProc(cs));
				ct.start();
			}
			catch (IOException e)
			{
				System.err.println("client socket or accept error.");
			}
		}
	}
	
}

class clientProc implements Runnable 
{
	Socket s;
	BufferedReader in;
	PrintWriter out;
	String name = null;

	public clientProc(Socket s) throws IOException 
	{
		this.s = s;
		in = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
		out = new PrintWriter(s.getOutputStream());
	}

	public void run()
	{
		try 
		{
			while (true) 
			{
				String line = in.readLine();
				if (name == null)
				{
					StringTokenizer st = new StringTokenizer(line);
					String cmd = st.nextToken();
					if ("login".equalsIgnoreCase(cmd))
					{
						name = st.nextToken();
						MainServer.loginUser(name);
					}
					else{}
				}
				else
				{
					StringTokenizer st = new StringTokenizer(line);
					String cmd = st.nextToken();
					if ("STAT".equalsIgnoreCase(cmd))
					{
						MainServer.statInfo(out);
					} 
					else if ("UP".equalsIgnoreCase(cmd))
					{
						MainServer.up(name);
					} 
					else if ("DOWN".equalsIgnoreCase(cmd))
					{
						MainServer.down(name);
					} 
					else if ("LEFT".equalsIgnoreCase(cmd))
					{
						MainServer.left(name);
					} 
					else if ("RIGHT".equalsIgnoreCase(cmd))
					{
						MainServer.right(name);
					} 
					else if ("LOGOUT".equalsIgnoreCase(cmd))
					{
						MainServer.logoutUser(name);
						break;
					}
				}
			}
			MainServer.deleteConnection(s);
			s.close();
		}
		catch (IOException e)
		{
			try 
			{
				s.close();
			}
			catch (IOException e2){}
		}
	}
}
