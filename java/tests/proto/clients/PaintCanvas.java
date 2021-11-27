package clients;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;

import common.*;

public class PaintCanvas extends Canvas
{

    private BufferedImage cImage = null;
    private Graphics2D g2d;
    private Constants.State state;
    BufferedReader in;
	PrintWriter out;
    private int w = 0;
    private int h = 0;
    public int width = 1;
    String myName;

    public PaintCanvas() 
    {
        w = Constants.WIDTH_CANVAS;
        h = Constants.HEIGHT_CANVAS;

        setSize(w, h);
        setBackground(Color.GRAY);

        cImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) cImage.getGraphics();
        
        repaint();

        state = Constants.State.INIT;
    }

    public void setStream(BufferedReader in, PrintWriter out)
    {
        this.in = in;
        this.out = out;
    }

    public void setMyName(String name)
    {
        myName = name;
    }

    public void paint(Graphics g) 
    {
        printBackground();
        
        switch(state)
        {
            case INIT:
                printInit();
                break;
            case UPDATE:
                printUpdate();
                break;
            case END:
                printEnd();
                break;
        }

        g.drawImage(cImage, 0, 0, null);
    }

    private void printInit()
    {
        printString("INIT", 100, 100);
    }

    private void printUpdate()
    {
        out.println("stat");
        out.flush();

        try
        {
            String line = in.readLine();
            while (!"ship_info".equalsIgnoreCase(line))
            {
				line = in.readLine();
            }

			line = in.readLine();
			while (!".".equals(line))
            {
				StringTokenizer st = new StringTokenizer(line);
				String obj_name = st.nextToken().trim();
                int x = Integer.parseInt(st.nextToken()) ;
				int y = Integer.parseInt(st.nextToken()) ;

				if (obj_name.equals(myName))
                {
                    printMyself(x, y);
                }
				else
                {
                    printEnemy(obj_name, x, y);
                }

				line = in.readLine();
			}

			while (!"energy_info".equalsIgnoreCase(line))
            {
				line = in.readLine();
            }

			line = in.readLine();
			while (!".".equals(line))
            {
				StringTokenizer st = new StringTokenizer(line);

				int x = Integer.parseInt(st.nextToken()) ;
				int y = Integer.parseInt(st.nextToken()) ;
                printEnergy(x, y);

				line = in.readLine();
			}

        }
        catch(Exception e)
        {
            e.printStackTrace();
		    System.exit(1);
        }
    }

    private void printEnd()
    {
        int mx = 100;
        int my = 300;
        printMyself(mx, my);
        printString("END", 0, 0);

    }

    public void setState(Constants.State state)
    {
        this.state = state;
    }

    public void printString(String s, int x, int y)
    {
        g2d.setColor(Constants.COLOR_STR1);
        g2d.drawString(s, x, y);
    }

    public void printMyself(int x, int y)
    {
        g2d.setColor(Constants.COLOR_MYSELF);
        g2d.fillOval(x, y, Constants.SIZE_OBJ, Constants.SIZE_OBJ);
        g2d.drawString(myName, x, y);
    }

    public void printEnemy(String name, int x, int y)
    {
        g2d.setColor(Constants.COLOR_ENEMY);
        g2d.fillOval(x, y, Constants.SIZE_OBJ, Constants.SIZE_OBJ);
        g2d.drawString(name, x, y);
    }

    public void printEnergy(int x, int y)
    {
        g2d.setColor(Constants.COLOR_ENEMY);
        g2d.fillRect(x, y, Constants.SIZE_OBJ, Constants.SIZE_OBJ);
    }

    public void printBackground()
    {
        g2d.setColor(Constants.COLOR_BACKGROUND);
        g2d.fillRect(0, 0, Constants.WIDTH_CANVAS, Constants.HEIGHT_CANVAS);

    }

}
