import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        int[][] initState = {{1,2,3,4},{5,6,7,8},{9,10,11,12}, {13,14,15,16}};
        //int[][] initState = {{1,2,3}, {4,5,6}, {7,8,9}};
        //int[][] initState = {{1,7,2}, {9,4,3}, {5,8,6}};
        //int[][] initState = {{1,2,3,4},{14,5,7,8},{9,13,11,12}, {10,16,6,15}};
        //int[][] initState = {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{16,17,18,19,20},{21,22,23,24,25}};

        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                GUI gui = new GUI("Block World");
                gui.setInitState(initState);
                gui.initGUI();
            }
        });
    }
}
