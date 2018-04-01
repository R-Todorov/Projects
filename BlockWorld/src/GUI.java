import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GUI extends JFrame
{
    private JPanel pane;
    private JPanel gridPanel;
    private JPanel settingsPanel;
    private JPanel algoChoicePanel;
    private JPanel buttonPanel;
    private JButton [][] grid;
    private ImageIcon[ ] images;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel time;
    private JLabel depth;
    private JLabel visited;
    private JButton solve;
    private JButton setState;
    private JButton show;
    private final int SQUARE_SIZE = 130;
    private final int HEIGHT = 450;
    private int  n;
    private int counter;
    private int[][] initState;
    private Node finalNode;
    private Font font;
    private JLabel error;


    public GUI(String title)
    {
        setTitle(title);
        counter = 0;
    }

    public void setInitState(int[][] initState)
    {
        this.initState = initState;
        n = initState.length;
    }

    public JPanel getGridPanel()
    {
        return gridPanel;
    }

    public JButton getShowButton()
    {
        return show;
    }

    //Draws the gridPanel and the GUI of the application
    public void initGUI()
    {
        pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.setSize(getWidth(), getHeight());

        gridPanel = new JPanel(new GridLayout(n, n));
        gridPanel.setPreferredSize(new Dimension(n * SQUARE_SIZE, n * SQUARE_SIZE));
        generateGrid();

        settingsPanel = new JPanel();
        initSettingsPanel();

        pane.add(gridPanel, BorderLayout.NORTH);
        pane.add(settingsPanel, BorderLayout.CENTER);

        setSize(n * SQUARE_SIZE, n * SQUARE_SIZE);
        setContentPane(pane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        pack();
    }

    public void generateGrid()
    {
        images = new ImageIcon[5];
        images[0] = new ImageIcon("images//empty.png");
        images[1] = new ImageIcon("images//top.png");
        images[2] = new ImageIcon("images//middle.png");
        images[3] = new ImageIcon("images//bottom.png");
        images[4] = new ImageIcon("images//worker.png");

        grid = new JButton[n][n];

        int numOfTiles = n * n;
        int tileNum;


        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                JButton btn = new JButton();
                tileNum = initState[i][j];

                if (tileNum <= numOfTiles - n)
                {
                    btn.setIcon(images[0]);
                }
                else
                {
                    if (tileNum == (numOfTiles - n) + 1)
                    {
                        btn.setIcon(images[1]);
                    }
                    else if (tileNum == numOfTiles - 1)
                    {
                        btn.setIcon(images[3]);
                    }
                    else if (tileNum == numOfTiles)
                    {
                        btn.setIcon(images[4]);
                    }
                    else
                    {
                        btn.setIcon(images[2]);
                    }
                }

                gridPanel.add(btn);
                grid[i][j] = btn;
            }
        }
    }

    public void initSettingsPanel()
    {
        settingsPanel.setPreferredSize(new Dimension(settingsPanel.getWidth(), HEIGHT));

        font = new Font("Serif", Font.BOLD, 25);

        algoChoicePanel = new JPanel();
        algoChoicePanel.setLayout(new BoxLayout(algoChoicePanel, BoxLayout.PAGE_AXIS));
        algoChoicePanel.setFont(font.deriveFont(12.0f));
        algoChoicePanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK),"Select Algorithm", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, font.deriveFont(20.0f)));

        ButtonGroup bg = new ButtonGroup();

        JRadioButton bfs = new JRadioButton(" Breadth First search");
        bfs.setFont(font);
        MyActionListener actListener = new MyActionListener(this);
        bfs.addActionListener(actListener);

        JRadioButton dfs = new JRadioButton(" Depth First search");
        dfs.setFont(font);
        dfs.addActionListener(actListener);

        JRadioButton ids = new JRadioButton(" Iterative Deepening search");
        ids.setFont(font);
        ids.addActionListener(actListener);

        JRadioButton aStar= new JRadioButton(" A* heuristic search");
        aStar.setFont(font);
        aStar.addActionListener(actListener);

        bg.add(bfs);
        bg.add(dfs);
        bg.add(ids);
        bg.add(aStar);

        time = new JLabel("Time: ");
        time.setFont(font);

        depth = new JLabel( "Depth: ");
        depth.setFont(font);

        visited = new JLabel("Visited nodes: ");
        visited.setFont(font);

        algoChoicePanel.add(bfs);
        algoChoicePanel.add(dfs);
        algoChoicePanel.add(ids);
        algoChoicePanel.add(aStar);

        JPanel metricsPanel = new JPanel();
        metricsPanel.setLayout(new BoxLayout(metricsPanel, BoxLayout.PAGE_AXIS));
        metricsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLUE),"Metrics", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, font.deriveFont(20.0f)));

        metricsPanel.add(time);
        metricsPanel.add(depth);
        metricsPanel.add(visited);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));

        textField1 = new JTextField();
        textField1.setText(Integer.toString(n));
        textField1.setFont(font);
        textField1.setHorizontalAlignment(JTextField.CENTER);
        textField1.setPreferredSize(new Dimension(10,10));

        textField2 = new JTextField();
        textField2.setText(textField1.getText());
        textField2.setFont(font);
        textField2.setHorizontalAlignment(JTextField.CENTER);
        textField2.setEditable(false);

        MyDocumentListener tf1Listener = new MyDocumentListener(textField1, textField2);
        textField1.getDocument().addDocumentListener(tf1Listener);

        JPanel gridSize = new JPanel();
        gridSize.setLayout(new BoxLayout(gridSize, BoxLayout.LINE_AXIS));

        JLabel size = new JLabel("Grid size: ");
        size.setFont(font);

        JLabel x = new JLabel("  x  ");
        x.setFont(font);

        gridSize.add(size);
        gridSize.add(Box.createHorizontalStrut(10));
        gridSize.add(textField1);
        gridSize.add(Box.createHorizontalStrut(10));
        gridSize.add(x);
        gridSize.add(Box.createHorizontalStrut(10));
        gridSize.add(textField2);
        gridSize.setMinimumSize(new Dimension(gridSize.getWidth(), gridSize.getHeight()));
        gridSize.add(Box.createHorizontalStrut(150));

        solve = new JButton("Solve");
        solve.setFont(font);

        solve.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JLabel label = new JLabel();
                label.setFont(new Font("Arial", Font.PLAIN, 18));

                if (bg.getSelection() != null)
                {
                    if (textField1.getText().matches("[3-5]"))
                    {

                        time.setText("Time: ");
                        depth.setText("Depth: ");
                        visited.setText("Visited nodes: ");

                        finalNode = null;
                        counter = 0;

                        long start = System.currentTimeMillis();

                        if (bfs.isSelected())
                        {
                            finalNode = solveBFS();
                        }
                        else if (dfs.isSelected())
                        {
                            finalNode = solveDFS();
                        }
                        else if (ids.isSelected())
                        {
                            finalNode = solveIDS();
                        }
                        else
                        {
                            finalNode = solveAStar();
                        }

                        long finish = System.currentTimeMillis();

                        if (finalNode != null)
                        {
                            long result = finish - start;
                            time.setText(time.getText() + " " + result + " ms");
                            depth.setText(depth.getText() + " " + finalNode.getDepth());
                            visited.setText(visited.getText() + " " + counter);
                        }
                        else
                        {
                            System.out.println("There is no solution leading to the goal state with this initial configuration");
                            Node initNode = new Node(initState, null,0);
                            initNode.printState();
                        }
                    }
                    else
                    {
                        label.setText("The current supported grid sizes are 3x3, 4x4 and 5x5. ");
                        JOptionPane.showMessageDialog(null, label, "Invalid grid size", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    label.setText("Please select a search strategy. ");
                    JOptionPane.showMessageDialog(null, label, "No strategy selected", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        setState = new JButton("Set State");
        setState.setFont(font);

        setState.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int newN = Integer.parseInt(textField1.getText());

                error = new JLabel();
                error.setFont(new Font("Arial", Font.PLAIN, 18));

                if (newN < 3 || newN > 5)
                {
                    textField1.setText(Integer.toString(n));
                    error.setText("The current supported grid sizes are 3x3, 4x4 and 5x5.");
                    JOptionPane.showMessageDialog(null, error, "Invalid grid size", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    int numTiles = newN * newN;

                    error.setText("Please enter the numbers  1 to " + numTiles + " separated by comma to initialise the new " + newN + " x " + newN + " grid "
                            + "(builder - " + numTiles + ", building parts - " + ((numTiles - newN) + 1)  + " - " + (numTiles - 1) + ")");
                    String state = JOptionPane.showInputDialog(null, error, "Generate grid", JOptionPane.INFORMATION_MESSAGE);

                    //Match all numbers between 1-25 that are delimited by comma (biggest grid size)
                    if (state.matches("(((([1-9])|(1[0-6])),){15}(1[0-6]|[1-9]))"))
                    {
                        StringTokenizer tokenizer = new StringTokenizer(state, ",");

                        int[][] newInitial = new int[newN][newN];

                        for (int i = 0; i < newN; i++)
                        {
                            for (int j = 0; j < newN; j++)
                            {
                                // deal with the rest of the restrictions that the regex doesn't enforce
                                if (i > newN)
                                {
                                    displayGridInitErrorMessage(numTiles);
                                }
                                else
                                {
                                    newInitial[i][j] = Integer.parseInt(tokenizer.nextToken());
                                }
                            }
                        }

                        n = newN;
                        initState = newInitial;
                        initGUI();
                        repaint();
                    }
                    else
                    {
                       displayGridInitErrorMessage(numTiles);
                    }
                }
            }
        });

        show = new JButton("Show Solution");
        show.setFont(font);

        show.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (finalNode != null)
                {

                    printSteps(finalNode);
                    animateSolution(finalNode);
                    show.setEnabled(false);
                }
                else
                {
                    JLabel label = new JLabel("The problem must be solved first ");
                    label.setFont(new Font("Arial", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(null, label, "No solution found", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(solve);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(setState);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(show);
        buttonPanel.add(Box.createHorizontalStrut(80));

        optionsPanel.add(algoChoicePanel);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(metricsPanel);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(gridSize);
        optionsPanel.add(Box.createVerticalStrut(5));

        settingsPanel.add(optionsPanel, BorderLayout.CENTER);
        settingsPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void displayGridInitErrorMessage(int numTiles)
    {
        error.setText("The initial state you have entered for the grid is invalid. Please make sure you enter the numbers from 1 to "
                + numTiles + " separated by comma in any order.");
        JOptionPane.showMessageDialog(null, error, "Invalid initial state", JOptionPane.ERROR_MESSAGE);
    }

    public void printSteps(Node node)
    {
        Stack<Node> solution = new Stack<>();

        while (node != null)
        {
            solution.push(node);
            node = node.getParent();
        }

        System.out.println(" Move Sequence: ");
        System.out.println("----------------\n");

        while (!solution.isEmpty())
        {
            Node current = solution.pop();
            current.printState();
        }
    }

    public void animateSolution(Node node)
    {
        Stack<Node> solution = new Stack<>();

        while (node != null)
        {
            solution.push(node);
            node = node.getParent();
        }

        solution.pop();

        Timer timer = new Timer(750, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!solution.isEmpty()){

                    Node current = solution.pop();
                    moveBuilder(current);
                    repaint();
                }
            }
        });

        timer.setRepeats(true);
        timer.start();

    }

    //Moves the tile occupied by the builder into the according direction with respect to the current node
    public void moveBuilder(Node current)
    {
        int direction = current.getBuilderDirection();
        int x = current.getParent().getBuilderX();
        int y = current.getParent().getBuilderY();

        Icon builder = grid[x][y].getIcon();

        switch (direction)
        {
            case 0:
                if (x > 0)
                {
                    grid[x][y].setIcon(grid[x - 1][y].getIcon());
                    grid[x - 1][y].setIcon(builder);
                }
                break;

            case 1:
                if (x < n-1)
                {
                    grid[x][y].setIcon(grid[x + 1][y].getIcon());
                    grid[x + 1][y].setIcon(builder);
                }
                break;

            case 2:
                if (y > 0)
                {
                    grid[x][y].setIcon(grid[x][y - 1].getIcon());
                    grid[x][y - 1].setIcon(builder);
                }
                break;

            case 3:
                if (y < n-1)
                {
                    grid[x][y].setIcon(grid[x][y + 1].getIcon());
                    grid[x][y + 1].setIcon(builder);
                }
                break;
        }
    }

    public Node solveBFS()
    {
        Node initNode = new Node(initState, null, 0);
        LinkedList<Node> list = new LinkedList<>();

        list.add(initNode);

        while (!list.isEmpty())
        {
            Node node = list.remove(0);
            counter++;

            if (node.isGoalState())
            {
                return node;
            }

            for (Node successor : node.getSuccessors())
            {
                if (!node.isStateRepeated(successor))
                {
                    successor.setBuilderPosition();
                    list.add(successor);
                }
            }
        }

        return null;
    }

    public Node solveDFS()
    {
        Stack<Node> stack = new Stack<>();
        Node initNode = new Node(initState, null, 0);

        stack.push(initNode);

        while (!stack.isEmpty())
        {
            Node node = stack.pop();
            counter++;

            if (node.isGoalState())
            {
                return node;
            }

            for (Node successor : node.getSuccessorsRandomOrder())
            {
                if (!node.isStateRepeated(successor))
                {
                    stack.push(successor);
                }
            }
        }

        return  null;
    }

    public Node solveIDS()
    {
        Node initNode = new Node(initState, null, 0);

        int depth = 0;

        while (true)
        {
            Node finalNode = depthLimitedSearch(initNode, depth);

            if (finalNode != null)
            {
                return finalNode;
            }

            depth++;
        }
    }

    public Node depthLimitedSearch(Node node, int limit)
    {
        counter++;

        if (limit == 0 && node.isGoalState())
        {
            return node;
        }

        if (limit > 0)
        {

            for (Node successor : node.getSuccessors())
            {
                Node nextNode = null;

                if (!node.isStateRepeated(successor))
                {
                    nextNode = depthLimitedSearch(successor, limit - 1);
                }

                if (nextNode != null)
                {
                    return nextNode;
                }
             }
        }

        return null;
    }

    public Node solveAStar()
    {
        Queue<Node> queue = new PriorityQueue<>();
        Node initNode = new Node(initState, null, 0);
        Map<Integer,Tile> goalConfig = getGoalTiles();

        queue.add(initNode);

        while (!queue.isEmpty())
        {
            Node node = queue.poll();
            counter++;

            if (node.isGoalState())
            {
                return node;
            }

            for (Node successor : node.getSuccessors())
            {
                if (!node.isStateRepeated(successor))
                {
                    int evaluation = successor.getDepth() + successor.calcManhattan(goalConfig);
                    successor.setEvaluation(evaluation);
                    queue.add(successor);
                }
            }
        }

        return null;
    }

    public Map<Integer,Tile> getGoalTiles()
    {
        Map<Integer,Tile> buildingParts = new HashMap<>();

        int notParts = n * (n - 1);
        int partNum = 1;

        while(partNum <= n - 1)
        {
            buildingParts.put(notParts + partNum, new Tile(partNum,1));
            partNum++;
        }

        return buildingParts;
    }
}
