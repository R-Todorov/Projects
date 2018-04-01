import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MyActionListener implements ActionListener
{
    private GUI gui;

    public MyActionListener(GUI gui)
    {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        gui.getGridPanel().removeAll();
        gui.generateGrid();
        gui.getShowButton().setEnabled(true);
        gui.revalidate();
        gui.repaint();
    }
}
