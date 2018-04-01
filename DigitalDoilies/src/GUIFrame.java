import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSplitPane;


public class GUIFrame extends JFrame
{
	
	private static final long serialVersionUID = 1L;
	private DisplayPanel display;
	private ControlPanel control;
	private GalleryPanel gallery;
	private JSplitPane split;
	private Dimension screenSize;
	
	
	public GUIFrame(String title)
	{
		super(title);
	}
	
	
	public void init()
	{	
		this.setLayout(new BorderLayout());
		
		display = new DisplayPanel();
		gallery = new GalleryPanel();
		control = new ControlPanel(display, gallery);
		
		gallery.setControlPanel(control);
		display.setControlPanel(control);
		
		// redraws the doily if the frame is resized
		display.addComponentListener(new ComponentAdapter() 
		{
		    @Override
		    public void componentResized(ComponentEvent e)
		    {
		    	display.redraw();
		    }
		});

		
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, display, gallery);
		split.setDividerLocation(1200);
		split.setEnabled(true);
		split.setDividerSize(5);
				
		
		add(display, BorderLayout.CENTER);
		add(gallery, BorderLayout.EAST);
		add(control, BorderLayout.SOUTH);
		
		
		// Gets the maximum screen resolution
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
	    setIconImage(new ImageIcon("GUI_logo.png").getImage());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(screenSize.width,screenSize.height);
		setVisible(true);
		pack();
		
	}	

}
