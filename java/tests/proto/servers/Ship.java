package servers;

import common.*;

public class Ship 
{
	int x;
	int y;
	int point = 0;

	public Ship(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

    public int getX(){ return x;}
    public int getY(){ return y;}
    public int getPoint(){ return point;}
    
    public void addPoint(int a){ point += a;}

	public void left()
	{
		x -= 10;
		if (x < 0)
			x += Constants.WIDTH_CANVAS;
	}

	public void right()
	{
		x += 10;
		x %= Constants.WIDTH_CANVAS;
	}

	public void down()
	{
		y += 10;
		y %= Constants.HEIGHT_CANVAS;
	}

	public void up()
	{
		y -= 10;
		if (y < 0)
			y += Constants.HEIGHT_CANVAS;
	}
}