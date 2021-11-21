// package umi2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class UmiClient implements Runnable {
	Frame f;
	Panel p;
	Canvas c;

	public UmiClient ()
	{
		Button b;
		f = new Frame();
		p = new Panel();
		p.setLayout(new BorderLayout());

		b = new Button("up");
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCommand("up");
			}
		});
		p.add(b, BorderLayout.NORTH);

		b = new Button("left");
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCommand("left");
			}
		});
		p.add(b, BorderLayout.WEST);

		b = new Button("right");
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCommand("right");
			}
		});
		p.add(b, BorderLayout.EAST);

		b = new Button("down");
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCommand("down");
			}
		});
		p.add(b, BorderLayout.SOUTH);

		c = new Canvas();
		c.setSize(256,256);
		p.add(c);
		f.add(p);

		b = new Button("login");
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(server == null) login();
			}
		});
		f.add(b,BorderLayout.NORTH);

		b = new Button("logout");
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				logout();
			}
		});
		f.add(b,BorderLayout.SOUTH);

		f.setSize(335,345);
		f.setVisible(true);
	}

	public void run(){
		while (true){
			try {
				Thread.sleep(500);
			}catch(Exception e){
			}
			repaint();
		}
	}

	int sx = 100;
	int sy = 100;
	TextField host, tf_name;
	Dialog d;

	void login()
	{
		d = new Dialog(f, true);
		host = new TextField(10) ;
		tf_name = new TextField(10) ;
		d.setLayout(new GridLayout(3,2));
		d.add(new Label("host:"));
		d.add(host);
		d.add(new Label("name:"));
		d.add(tf_name);
		Button b = new Button("OK");
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				realLogin(host.getText(), tf_name.getText());
				d.dispose();
				d.setVisible(false);
			}
		});
		d.add(b);
		d.setResizable(true);
		d.setSize(200, 150);
		d.setVisible(true);
		(new Thread(this)).start();
	}

	Socket server;
	int port = 80;
	BufferedReader in;
	PrintWriter out;
	String name;

	void realLogin(String host, String name){
		try {
			this.name = name;
			server = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(
			  server.getInputStream()));
			out = new PrintWriter(server.getOutputStream());

			out.println("login " + name);
			out.flush();
			System.out.println("login");
			repaint();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	void logout(){
		try {
			out.println("logout");
			out.flush();
			server.close();
		}catch (Exception e){
			;
		}
		System.exit(0);
	}

	void repaint(){
		out.println("stat");
		out.flush();

		try {
			String line = in.readLine();
			Graphics g = c.getGraphics();

			g.setColor(Color.blue);
			g.fillRect(0, 0, 256, 256);

			while (!"ship_info".equalsIgnoreCase(line))
				line = in.readLine();

			line = in.readLine();
			while (!".".equals(line)){
				StringTokenizer st = new StringTokenizer(line);
				String obj_name = st.nextToken().trim();

				if (obj_name.equals(name))
					g.setColor(Color.red);
				else
					g.setColor(Color.green);

				int x = Integer.parseInt(st.nextToken()) ;
				int y = Integer.parseInt(st.nextToken()) ;

				g.fillOval(x - 10, 256 - y - 10, 20, 20);
				g.drawString(st.nextToken(),x+10,256-y+10) ;
				g.drawString(obj_name,x+10,256-y-10) ;

				line = in.readLine();
			}

			while (!"energy_info".equalsIgnoreCase(line))
				line = in.readLine();

			line = in.readLine();
			while (!".".equals(line)){
				StringTokenizer st = new StringTokenizer(line);

				int x = Integer.parseInt(st.nextToken()) ;
				int y = Integer.parseInt(st.nextToken()) ;

				g.setColor(Color.red);
				g.fillOval(x - 5, 256 - y - 5, 10, 10);
				g.setColor(Color.white);
				g.fillOval(x - 3, 256 - y - 3, 6, 6);

				line = in.readLine();
			}
		}catch (Exception e){
		e.printStackTrace();
		System.exit(1);
		}
	}

	void sendCommand(String s){
		if ("up".equals(s)){
			out.println("up");
		}else if ("down".equals(s)){
			out.println("down");
		}else if ("left".equals(s)){
			out.println("left");
		}else if ("right".equals(s)){
			out.println("right");
		}
		out.flush();
	}

	void ppp()
	{
		System.out.println("start");
	}

	public static void main(String[] arg){
		// new UmiClient();
		JFrame frame = new JFrame("Umi");

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JButton button = new JButton("up");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		panel.add(button, BorderLayout.NORTH);

		frame.add(panel);

		Rectangle rect = new Rectangle(0, 0, 335, 345);
		frame.setBounds(rect);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}