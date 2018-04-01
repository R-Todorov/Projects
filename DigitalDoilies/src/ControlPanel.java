import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	private Color color;
	private JButton undo;
	private JButton clear;
	private JButton save;
	private JButton remove;
	private JButton changeColor;
	private JCheckBox hideSectors;
	private JCheckBox reflection;
	private JSpinner penThickness;
	private JSlider sectors;
	private JLabel numOfSectors;
	private JLabel penSize;
	private DisplayPanel display;
	private GalleryPanel gallery;
	
	
	public ControlPanel(DisplayPanel display, GalleryPanel gallery)
	{
		this.display = display;
		this.gallery = gallery;	
		init();
	}
	
	
	public void init()
	{

		reflection = new JCheckBox("Reflection");
		reflection.setFocusPainted(false);
		reflection.setForeground(Color.BLACK);
		reflection.setBackground(Color.BLUE);
		
		hideSectors = new JCheckBox("Hide sectors");
		hideSectors.setFocusPainted(false);
		hideSectors.setForeground(Color.BLACK);
		hideSectors.setBackground(Color.BLUE);
		
		changeColor= new JButton("Change Color");
		changeColor.setToolTipText("Change the colour of your pen");
		
		undo = new JButton("Undo");
		undo.setToolTipText("Undoes a previous drawing action");
		undo.setEnabled(false);
		
		clear = new JButton("Clear");
		clear.setToolTipText("Clears the current doily");
		
		save = new JButton("Save");	
		save.setToolTipText("Saves the doily on the hard disk");
		
		remove= new JButton("Remove");
		remove.setToolTipText("Removes the selected image from your disk");
		remove.setEnabled(false);
		
		penThickness = new JSpinner(new SpinnerNumberModel(display.getPenSize(), 1.0, 50.0, 1.0));
		
		numOfSectors = new JLabel("Number of Sectors");
		numOfSectors.setForeground(Color.BLACK);
		
		penSize = new JLabel("Pen size");
		penSize.setForeground(Color.BLACK);

		sectors = new JSlider(10,50,10);
		initSectorSlider(); // initialises the JSlider
		
		
		// handles changing the pen size
		penThickness.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent event) 
			{
				display.setPenSize((double) penThickness.getValue());
			}
			
		});
		
		
		// handles changing the number of sectors
		sectors.addChangeListener(new ChangeListener()
		{	
			@Override
			public void stateChanged(ChangeEvent event)
			{
				JSlider source = (JSlider) event.getSource();
				display.setNumberOfSectors(source.getValue());
			}
			
		});
		
		
		// handles changing the pen color
		changeColor.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				changePenColor();				
			}		
					
		});
		
	
		
		// removes the last drawing performed by the user
		undo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				display.undoDrawing();
			}
			
		});
		
		
		// handles the screen clearing
		clear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				display.clearScreen();
			}
	
		});
		
		
		// saves the doily and displays is in the gallery
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				saveImage(display.getBufferedImage());
			}
		
		});
		
		
		// removes the selected doily from the gallery and deletes it from the disk
		remove.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				gallery.getDefaultListModel().remove(gallery.getSelectedImageIndex());
				
			}	
			
		});
		
		
		// handles the sector toggling
		hideSectors.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent event) 
			{
				display.toggleSectors(!hideSectors.isSelected());
			}	
			
		});
		
		
		// handles the reflection toggling
		reflection.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				display.toggleReflection(reflection.isSelected());
			}
			
		});
		
		
		// toggles the sectors on and off
		hideSectors.addItemListener(new ItemListener()
		{
			@Override
		    public void itemStateChanged(ItemEvent event) 
			{
				display.toggleSectors(!hideSectors.isSelected());
			}	
			
	    });
		

		add(reflection);
		add(hideSectors);
		add(penSize);
		add(penThickness);
		add(numOfSectors);
		add(sectors);
		add(changeColor);
		add(undo);
		add(clear);
		add(save);
		add(remove);
		
		setBackground(Color.BLUE);
		
	}
	
	
	//initialises the slider which determines the number of sectors
	public void initSectorSlider()
	{
		sectors.setMinorTickSpacing(1);
		sectors.setMajorTickSpacing(5);
		sectors.setSnapToTicks(true);
		sectors.setPaintLabels(true);
		sectors.setPaintTicks(true);
	}
	
	
	// Shows a dialog box which asks the user to choose a name for the file that he wants to save
	public void saveImage(BufferedImage bufImage)
	{
		String fileName = JOptionPane.showInputDialog("Choose image name");
			
		if (fileName != null)
		{	
			if(fileName.equals(""))
			{
				fileName = "Untitled";
			}
					
			chooseImageFormat(bufImage, fileName);
		}	
			
	}
			
			
	//shows a dialog box which asks the user to select the file format of the image he would like to save
	public void chooseImageFormat(BufferedImage bufImage, String fileName)
	{
		Object[] imageFormats = {"PNG", "JPEG"};
		
		String userInput = (String) JOptionPane.showInputDialog(null, "Choose file format", "File Format Settings", JOptionPane.PLAIN_MESSAGE, null, imageFormats, "PNG");

				
		String imageFormat = (userInput.equals("PNG")) ? "PNG" : "JPEG";
		String fileExtension = (imageFormat.equals("PNG")) ? ".png" : ".jpg";
				
		File file = new File(fileName + fileExtension);
				
		try 
		{
			ImageIO.write(bufImage, imageFormat, file);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			
		gallery.addImageToGallery(file);
	}
	
	
	// changes the color of the pen used for drawing the doilie
	public void changePenColor()
	{
		color = JColorChooser.showDialog(null, "Color Settings", color);
		
		if(color == null)
		{
			color = display.getPenColor();
		}
		
		display.setPenColor(color);
	}
	
	// returns a reference to the remove button
	public JButton getRemoveButton()
	{
		return remove;
	}
	
	// returns a reference to the undo button
	public JButton getUndoButton()
	{
		return undo;
	}
	
	
	public Dimension getPreferredSize() 
	{
	      return new Dimension(1150, 50);
	}
	
}
