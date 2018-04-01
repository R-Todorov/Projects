import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MyDocumentListener implements DocumentListener
{
    private JTextField updated;
    private JTextField affected;

    public MyDocumentListener (JTextField updated, JTextField affected)
    {
        this.updated = updated;
        this.affected = affected;
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        affected.setText(updated.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        affected.setText(updated.getText());
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        affected.setText(updated.getText());
    }
}
