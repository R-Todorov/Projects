import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;


public class DisplayPanel extends JPanel
{
	
	private static final long serialVersionUID = 1L;
	private int sectors;
	private int penSize;
	private boolean drawSectors;
	private boolean reflection;
	private ArrayList<MyPoint> points;
	private BufferedImage bufImage;
	private Color penColor;
	private Line2D sectorLine;
	private ControlPanel control;
	
	
	public DisplayPanel()
	{
		init();
	}
	
	
	// initialises the display on which the user draws
	public void init()
	{		
		DisplayListener listener = new DisplayListener();
		
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		setPreferredSize(new Dimension(1200,820));
		
		bufImage = new BufferedImage((int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight(), BufferedImage.TYPE_INT_RGB);
		points = new ArrayList<MyPoint>();
		
		drawSectors = true;
		
		setPenColor(Color.GREEN);
		setPenSize(10);
		setNumberOfSectors(10);
		setBackground(Color.BLACK);
	}

	
	// performs the drawing on the display panel
    public void paintComponent(Graphics g)
	{
		super.paintComponent(g);		
	    Graphics2D g2d = (Graphics2D) g;
	    
	    g2d.drawImage(bufImage, 0, 0, null);
	    sectorLine = new Line2D.Double(getCenterX(), getCenterY(), getCenterX(), 0);	
	   
	    
	    drawSectorLines();
	    
	    draw();
	    
	    g2d.dispose();
	}
    
    
    // draws the mouse pattern
    public void draw()
	{	
    	control.getUndoButton().setEnabled(points.size()>0);
    	
    	Graphics2D g2d = (Graphics2D) bufImage.createGraphics();
    	
		for (int i = 0; i < points.size() - 1; i++) 
		{
			rotate(g2d, i);		
		}
		
		g2d.dispose();
	}
    

	// draws a line between two consecutive points in the ArrayList and rotates it in each sector
	public void rotate(Graphics2D g2d, int index)
	{		
		MyPoint point1 = points.get(index);
		MyPoint point2 = points.get(index+1);
		
		getPointSettings(g2d, point1);
			
		for(int i=0; i < sectors; i++)
		{	
			if(point1.isConnectedToNext() && point2.isConnectedToNext())
			{	
				g2d.drawLine(point1.getX() - getMouseOffset(point1), point1.getY() - getMouseOffset(point1), point2.getX() - getMouseOffset(point2), point2.getY() - getMouseOffset(point2));
				
				if(point1.isReflected())
				{
					g2d.drawLine(calculateReflection(point1) - getMouseOffset(point1), point1.getY() - getMouseOffset(point1), calculateReflection(point2) - getMouseOffset(point2), point2.getY() - getMouseOffset(point2));
				}		
			}
			else
			{
				drawPoint(g2d,point1);
			}
			
		
			g2d.rotate(Math.toRadians(getRotationDegrees()), getCenterX(), getCenterY());
		}
		
	}
	
	
	// draws a point when the user clicks
	public void drawPoint(Graphics2D g2d, MyPoint point1)
	{	
		g2d.fillOval(point1.getX() - getMouseOffset(point1), point1.getY() - getMouseOffset(point1), point1.getSize(), point1.getSize());
		
		if(point1.isReflected())
		{
			g2d.fillOval( calculateReflection(point1) - getMouseOffset(point1), point1.getY() - getMouseOffset(point1), point1.getSize(),  point1.getSize());
		}
		
	}
	
	
	// sets the graphics context for a given point
	public Graphics2D getPointSettings(Graphics2D g2d, MyPoint point)
	{
			
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		g2d.setColor(point.getColor());
		g2d.setStroke(new BasicStroke(point.getSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.setClip( new Ellipse2D.Double(getCenterX() - getLineLength(), getCenterY() - getLineLength(), getLineLength()*2, getLineLength()*2));
				
		return g2d;
	}	
	
	
	// clears the screen and redraws the doily
	public void redraw()
	{
		Graphics2D g2d = bufImage.createGraphics();
		
		g2d.setColor(getBackground());
		g2d.clearRect(0, 0, getWidth(), getHeight());
		
		g2d.dispose();
		
		
		repaint();
	}
	
	
	// clears the current doily
	public void clearScreen()
	{
		points.clear();
		redraw();
	}
	
	// draws the sector lines
	public void drawSectorLines()
	{
		if (drawSectors)
		{
			Graphics2D g2d = bufImage.createGraphics();
			
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.RED);	
			g2d.setStroke(new BasicStroke(2));
			
			for(int i=0; i<sectors; i++)
			{	
				g2d.draw(sectorLine);
				g2d.rotate(Math.toRadians(getRotationDegrees()), getCenterX() , getCenterY());
			}

			g2d.dispose();
			repaint();
		}
		else
		{
			redraw();
		}
	}
	
	
	// undoes a drawing action performed by the user
	public void undoDrawing()
	{
		if(points.size() > 5)
		{
			
			for(int i=0; i <5; i++)
			{
				points.remove(points.size() - 1);
			}
			
			redraw();
		}
		else
		{	
			clearScreen();
		}
	}
	
	// calculates the X coordinate of the reflected point
	public int calculateReflection(MyPoint point)
	{
		return getWidth() - point.getX();
	}
	
	
	//returns the buffered image on which the user draws
	public BufferedImage getBufferedImage()
	{
		return bufImage;
	}
	
	
	// changes the font size of the pen
	public void setPenSize(double size)
	{	
		penSize = (int) size;
	}
	
	
	// returns the font size of the graphics object
	public int getPenSize()
	{
		return penSize;
	}
	
	
	//toggles points reflection
	public void toggleReflection(boolean toggle)
	{
		reflection = toggle;
	}
	
	
	public boolean isReflectionOn()
	{
		return reflection;
	}
	
	
	// toggles the sectors on and off 
	public void toggleSectors(boolean toggle)
	{
		drawSectors = toggle;
		drawSectorLines();
	}
	
	
	//sets the number of sectors on the display panel
	public void setNumberOfSectors(int numOfSectors)
	{
		sectors = numOfSectors;
		redraw();
	}
	
	
	// returns the current number of sectors
	public int getNumberOfSectors()
	{
		return sectors;
	}
	
	
	// changes the color of the pen
	public void setPenColor(Color newColor)
	{
		penColor = newColor;
	}
	
	
	// returns the current color of the pen
	public Color getPenColor()
	{
		return penColor;
	}
	
	
	// returns the X coordinate of the center of the panel
	public double getCenterX()
	{
		return  (double) getWidth()/2;
	}
		
		
	// returns the Y coordinate of the center of the panel
	public double getCenterY()
	{
		return (double) getHeight()/2;
	}
		
	
	// returns the mouse offset so that the point gets drawn at the exact place where the click or drag occurred
	public int getMouseOffset(MyPoint point)
	{
		return point.getSize()/2;
	}
	
	
	// returns the degree of rotation by which the point should be rotated
	public double getRotationDegrees()
	{
		return (double) 360/sectors;
	}
	
	
	// returns the length of the line which separates the sectors
	public double getLineLength()
	{
		double diffX = Math.pow((sectorLine.getX2() - sectorLine.getX1()), 2);
		double diffY = Math.pow((sectorLine.getY2() - sectorLine.getY1()), 2);
		double lineLength = Math.sqrt(diffX + diffY);
		
		return lineLength;
	}
	
	
	// establishes a reference to the Contrl Panel
	public void setControlPanel(ControlPanel control)
	{
		this.control = control;
	}
	
	
	public Dimension getPrefferedSize()
	{
		return new Dimension(1150, 850);
	}
	
	
	// handles drawing
	private class DisplayListener extends MouseAdapter
	{	
		private void addPoint(MyPoint point) 
		{
			points.add(point);
			draw();	
		}
		
		public void mouseClicked(MouseEvent event)
		{
			addPoint(new MyPoint(event.getX(), event.getY(), getPenSize(), getPenColor(), isReflectionOn(), false));
		}
			
		
		public void mouseDragged(MouseEvent event)
		{
			addPoint(new MyPoint(event.getX(), event.getY(), getPenSize(), getPenColor(), isReflectionOn(), true));
		}
		
		
		public void mouseReleased(MouseEvent event)
		{
			addPoint(new MyPoint(event.getX(), event.getY(), getPenSize(), getPenColor(), isReflectionOn(), false));
		}
		
	}
			
}