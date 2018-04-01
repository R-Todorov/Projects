import java.util.*;

public class Node implements Comparable<Node>
{
    private int[][] state;
    private Node parent;
    private int depth;
    private int builderX, builderY, builderDirection;
    private int evaluation;

    public Node (int[][] state, Node parent, int depth)
    {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
        this.builderDirection = 3;
        setBuilderPosition();
    }

    public Node (int[][] state, Node parent, int depth, int builderDirection)
    {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
        this.builderDirection = builderDirection;
        setBuilderPosition();
    }

    //Defines the ordering of nodes to be f(n) (used for A*)
    public int compareTo(Node node)
    {
        if (this.evaluation < node.getEvaluation())
        {
            return -1;
        }
        else if (this.evaluation > node.getEvaluation())
        {
            return 1;
        }

        return 0;
    }

    public void setBuilderX(int newX)
    {
        builderX = newX;
    }

    public void setBuilderY(int newY)
    {
        builderY = newY;
    }

    public void setEvaluation(int evaluation)
    {
        this.evaluation = evaluation;
    }

    public int getBuilderX()
    {
        return builderX;
    }

    public int getBuilderY()
    {
        return builderY;
    }

    public int getBuilderDirection()
    {
        return builderDirection;
    }

    public int [][] getState()
    {
        return state;
    }

    public Node getParent()
    {
        return parent;
    }

    public int getDepth()
    {
        return this.depth;
    }

    public int getEvaluation()
    {
        return this.evaluation;
    }

    //Sets the coordinates of the builder in the current state
    public void setBuilderPosition()
    {
        int n = state.length;

        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state.length; j++)
            {
                if (state [i][j] == n * n)
                {
                    setBuilderX(i);
                    setBuilderY(j);
                }
            }
        }
    }

    //Checks whether the grid state is repeated by traversing the tree up to the root
    public boolean isStateRepeated(Node node)
    {
        Node checked = node;

        while(node.getParent() != null)
        {
            if(node.getParent().hasSameState(checked.getState()))
            {
                return true;
            }

            node = node.getParent();
        }

        return false;
    }

    //Generates all the available successor states from the current grid state
    public List<Node> getSuccessors()
    {
        List<Node> successors = new LinkedList<>();

        int direction = 0;

        while (direction < 4)
        {
            Node successor = generateNode(direction);

            if (!hasSameState(successor.getState()))
            {
                successors.add(successor);
            }

            direction++;
        }

        return successors;
    }

    //Generates all the possible successor states in random order
    public List<Node> getSuccessorsRandomOrder()
    {
        List<Node> successors = new LinkedList<>();
        Set<Integer> directions = new TreeSet<>();

        Random random = new Random();
        int direction = random.nextInt(4);

        while (directions.size() < 4)
        {
            while (directions.contains(direction))
            {
                direction = random.nextInt(4);
            }

            Node successor = generateNode(direction);
            successors.add(successor);
            directions.add(direction);
        }

        return  successors;
    }

    //Generates a successor node by moving the builder from its current position in
    //in the direction passed as an argument (0-UP 1-DOWN 2-LEFT 3-RIGHT)
    private Node generateNode(int direction)
    {
        int [][] current = new int[state.length][state.length];
        int n = state.length;

        for (int i = 0; i < state.length; i++)
        {
            current[i] = Arrays.copyOf(state[i], state.length);
        }

        int builder = current[builderX][builderY];

        switch (direction)
        {
            case 0:
                if (builderX > 0)
                {
                    current[builderX][builderY] = current[builderX - 1][builderY];
                    current[builderX - 1][builderY] = builder;
                }
                break;
            case 1:
                if (builderX < n - 1)
                {
                    current[builderX][builderY] = current[builderX + 1][builderY];
                    current[builderX + 1][builderY] = builder;
                }
                break;
            case 2:
                if (builderY > 0)
                {
                    current[builderX][builderY] = current[builderX][builderY - 1];
                    current[builderX][builderY - 1] = builder;
                }
                break;
            case 3:
                if (builderY < n - 1)
                {
                    current[builderX][builderY] = current[builderX][builderY + 1];
                    current[builderX][builderY + 1] = builder;
                }
                break;
        }

        return new Node(current, this, depth + 1, direction);
    }

    //Returns true if the current grid state is the actual goal state
    public boolean isGoalState()
    {
        boolean goal = true;

        int i = 1;
        int n = state.length;
        int partBeginning = n * n - n;

        while (i < n)
        {
            // Checks if the tower has been built
            if (state[i][1] != partBeginning + i)
            {
                goal = false;
                break;
            }

            i++;
        }

        return goal;
    }

    //Checks whether the current state and the state passed as an argument are the same
    public boolean hasSameState(int[][] nextState)
    {
        for (int i = 0; i < state.length; i++)
        {
            if (!Arrays.equals(state[i], nextState[i]))
            {
                return false;
            }
        }

        return true;
    }

    //Calculates the Manhattan distance heuristic of the current goal state
    public int calcManhattan(Map<Integer, Tile> goalConfig)
    {
        int n = state.length;
        int maxTileVal = n * n;
        int lastNonPart = maxTileVal - n;
        int distance = 0;

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                int tileNum = state[i][j];

                if (tileNum > lastNonPart && tileNum < maxTileVal)
                {
                    Tile tile = goalConfig.get(tileNum);
                    distance += Math.abs(tile.getX() - i) + Math.abs(tile.getY() - j);
                }
            }
        }

        return distance;
    }

    //Prints the state of the grid in a pretty manner
    public void printState()
    {
        if (getParent() != null)
        {
            printDirection();
            System.out.println();
        }

        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state.length; j++)
            {
                System.out.printf("%3d ", state[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    //Prints the direction leading to the next state in the sequence of
    //moves leading to the goal state
    public void printDirection()
    {
        String move;

        switch (builderDirection)
        {
            case 0:
                move = "        ▲";
                break;
            case 1:
                move = "        ▼";
                break;
            case 2:
                move = "        ◀";
                break;
            default:
                move = "        ▶";
                break;
        }

        System.out.println(move);
    }
}
