
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

import clients.PaintCanvas;
import common.*;

public class MainClient implements Runnable
{
    Boolean isLoop = true;
    Socket server;
	BufferedReader in;
	PrintWriter out;
	String charaName;
    PaintCanvas canvas = null;
    Constants.State state;

    public static void main(String[] arg)
    {
        new MainClient();
	}

    public MainClient()
    {
        JFrame frame = new JFrame("Umi");

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

        SetButton("up",     BorderLayout.NORTH, panel);
        SetButton("left",   BorderLayout.WEST,  panel);
        SetButton("right",  BorderLayout.EAST,  panel);
        SetButton("down",   BorderLayout.SOUTH, panel);
        SetButton("login",  BorderLayout.NORTH, frame);
        SetButton("logout", BorderLayout.SOUTH, frame);

		frame.add(panel);

        canvas = new PaintCanvas();
        panel.add(canvas);

        Rectangle rect = new Rectangle(0, 0, Constants.WIDTH_RECT, Constants.HEIGHT_RECT);
		frame.setBounds(rect);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

        setState(Constants.State.INIT);

    }

    public void run()
    {
        while(isLoop)
        {
            try{ Thread.sleep(Constants.SLEEP_TIME); }
            catch(Exception e){}

            canvas.repaint();
        }
        
    }

    private void SetButton(String name, Object obj, JPanel panel)
    {
        JButton b = new JButton(name);
        b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
                sendCommand(name);
			}
		});
		panel.add(b, obj);
    }

    private void SetButton(String name, Object obj, JFrame frame)
    {
        JButton b = new JButton(name);
        b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
                sendCommand(name, frame);
			}
		});
		frame.add(b, obj);
    }

    private void sendCommand(String name)
    {
        if ("up".equals(name))
        {
            sendMoveCommand("up");
		}
        else if ("down".equals(name))
        {
            sendMoveCommand("down");
		}
        else if ("left".equals(name))
        {
            sendMoveCommand("left");
		}
        else if ("right".equals(name))
        {
            sendMoveCommand("right");
		}
    }

    private void sendCommand(String name, JFrame frame)
    {
        if ("login".equals(name))
        {
            login(frame);
        }
        else if ("logout".equals(name))
        {
            logout();
        }

    }

    private void sendMoveCommand(String dir)
    {
		System.out.println(dir);
		out.println(dir);
		out.flush();
    }

    private void login(JFrame frame)
    {
		System.out.println("login");
        JDialog dialog = new JDialog(frame, true);
        TextField charaName = new TextField(10);
        dialog.setLayout(new GridLayout(3, 2));
		dialog.add(new Label("name:"));
		dialog.add(charaName);
		Button button = new Button("OK");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
                System.out.println("clicked OK.");
                generateSocket(charaName.getText());
				dialog.dispose();
			}
		});
        dialog.add(button);
        dialog.setResizable(true);
		dialog.setSize(200, 150);
		dialog.setVisible(true);
        (new Thread(this)).start();
    }

    private void generateSocket(String charaName)
    {
        try
        {
            this.charaName = charaName;
            server = new Socket(Constants.IP, Constants.PORT);
            in = new BufferedReader( new InputStreamReader( server.getInputStream() ) );
            out = new PrintWriter( server.getOutputStream() );

            out.println("login " + charaName);
            out.flush();
            System.out.println("login");

            canvas.setStream(in, out);
            canvas.setMyName(charaName);
            setState(Constants.State.UPDATE);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void logout()
    {
		System.out.println("logout");
    }

    private void setState(Constants.State state)
    {
        this.state = state;
        if(canvas != null)
        {
            canvas.setState(state);
        }
    }

}
