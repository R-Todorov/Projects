import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

class TitledListCellRenderer implements ListCellRenderer 
{
	protected static TitledBorder focusedBorder = new TitledBorder(new LineBorder(Color.RED,5));
	protected static TitledBorder notFocusedBorder = new TitledBorder(new LineBorder(Color.GRAY,5));

	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
	{
		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		
		ImageIcon icon = (ImageIcon) value;
		
		notFocusedBorder.setTitle(icon.getDescription());
		notFocusedBorder.setTitleJustification(TitledBorder.CENTER);
		
		focusedBorder.setTitle(icon.getDescription());
		focusedBorder.setTitleJustification(TitledBorder.CENTER);
		
		// changes the border colour of the current image that the user has selected
		renderer.setBorder(isSelected ? focusedBorder : notFocusedBorder);
		
		return renderer;
	}

}