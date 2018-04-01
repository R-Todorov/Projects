import java.awt.Color;
import java.awt.Point;

public class MyPoint
{
	private int x;
	private int y;
	private int size;
	private boolean reflected;
	private Color color;
	private boolean connectToNext;

	// I have not extended Point to avoid casting the coordinates to int every single time
	public MyPoint(int x, int y, int size, Color color, boolean reflected, boolean connectToNext)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
		this.reflected = reflected;
		this.connectToNext = connectToNext;
	}
	
	
	public int getX()
	{
		return this.x;
	}
	
	
	public int getY()
	{
		return this.y;
	}
	
	
	public int getSize()
	{
		return this.size;
	}

	
	public Color getColor()
	{
		return this.color;
	}
	
	// returns true if a point should be reflected
	public boolean isReflected()
	{
		return this.reflected;
	}
	
	// returns true if a point should be connected with another point by a line
	public boolean isConnectedToNext()
	{
		return this.connectToNext;
	}		
}
