// package umi2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;


public class Main 
{
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

        PaintCanvas canvas = new PaintCanvas(255, 255);
        panel.add(canvas);

        Rectangle rect = new Rectangle(0, 0, 335, 345);
		frame.setBounds(rect);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}

// キャンバスクラス
class PaintCanvas extends Canvas
{
    int w = 0;
    int h = 0;

    private BufferedImage cImage = null;
    private Graphics2D g2d;

    private int x, y, xx, yy;
    private int type;
    public int width = 1;
    // public Color c = Color.black;

    public PaintCanvas(int width, int height) 
    {
        x = -1;
        y = -1;
        xx = -1;
        yy = -1;
        type = 0;

        w = width;
        h = height;

        setSize(w, h);
        setBackground(Color.GRAY);

        cImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) cImage.getGraphics();
        // set background to white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);

        repaint();
    }

    public void paint(Graphics g) 
    {
        
        System.out.println("hello");
        g2d.setColor(Color.RED);
        g2d.fillOval(50, 50, 130, 30);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(50, 0, w/3, h/3);
        
        g.drawImage(cImage, 0, 0, null);
        
    }




}
