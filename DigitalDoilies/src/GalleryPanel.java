import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class GalleryPanel extends JPanel 
{

	private static final long serialVersionUID = 1L;
	private final int MAX_IMAGES = 12;
	private int selectedImageIndex;
	private JScrollPane scrollPane;
	private ImageIcon logo;
	private JList<ImageIcon> imageGallery;
	private DefaultListModel<ImageIcon> listModel;
	private JLabel logoImage;
	private JPanel upper;
	private JPanel lower;
	private ControlPanel control;
	
	public GalleryPanel()
	{
		init();
	}
	
	
	public void init()
	{	
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		logo = new ImageIcon("gallery_logo.jpg");
		logoImage = new JLabel(logo, JLabel.CENTER);
		
		upper = new JPanel();
		upper.add(logoImage);
		upper.setBackground(Color.decode("#F3F1F2"));
		
		lower = new JPanel();
		lower.setLayout(new BorderLayout());
		
		listModel = new DefaultListModel<ImageIcon>();
	
		imageGallery = new JList<ImageIcon>(listModel);
		imageGallery.setBackground(Color.decode("#F3F1F2"));
		imageGallery.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		imageGallery.setLayoutOrientation(JList.VERTICAL);
		imageGallery.setVisibleRowCount(-1);
		imageGallery.setSelectionBackground(Color.decode("#F3F1F2"));
		imageGallery.setBorder(new EmptyBorder(5, 5, 5, 5));
		imageGallery.setCellRenderer(new TitledListCellRenderer());
				
		
		imageGallery.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event) 
			{	
				setSelectedImageIndex(imageGallery.getSelectedIndex());
				control.getRemoveButton().setEnabled(!getImageGallery().isSelectionEmpty());
			}	
			
		});
		
		
		scrollPane = new JScrollPane(imageGallery);
		scrollPane.setPreferredSize(new Dimension(10, 20));
		lower.add(scrollPane, BorderLayout.CENTER);
		lower.setSize(getWidth(), getHeight());
		
		
		add(upper);
		add(Box.createVerticalGlue());
		add(lower);
		add(Box.createVerticalGlue());
		
	}
	
	
	public void addImageToGallery(File file)
	{
		
		if ( imageGallery.getModel().getSize() < MAX_IMAGES)
		{	
			BufferedImage bufImage = null;
			
			try
			{
				bufImage = ImageIO.read(file);  //tries to load the image
			}
			catch (Exception e)
			{
				System.out.println("Unable to load file " + file.toString());
			}
			
			//creates a new Image and resizes it proportionally to the BufferedImage
			Image resizedImage = bufImage.getScaledInstance(bufImage.getWidth()/5, bufImage.getHeight()/5, Image.SCALE_SMOOTH);

			ImageIcon icon = new ImageIcon(resizedImage);
			icon.setDescription(file.toString());
			
			imageGallery.setFixedCellWidth(icon.getIconWidth());  // seems to have no effect
			
			listModel.addElement(icon);
			
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException("The gallery is full");
		}
		
	}

	
	public void setControlPanel(ControlPanel control)
	{
		this.control = control;
	}
	
	
	public DefaultListModel<ImageIcon> getDefaultListModel()
	{
		return listModel;
	}
	

	public void setSelectedImageIndex(int index)
	{
		selectedImageIndex = index;
	}
	
	
	// returns the index of the currently selected item in the JList
	public int getSelectedImageIndex()
	{
		return selectedImageIndex;
	}
	
	
	public JList<ImageIcon> getImageGallery()
	{
		return imageGallery;
	}

	
	public Dimension getPreferredSize() 
	{
	    return new Dimension(286, 700);
	}
	
}



