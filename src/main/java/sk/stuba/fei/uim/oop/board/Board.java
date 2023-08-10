package sk.stuba.fei.uim.oop.board;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class Board extends JPanel {
    private Tile[][] board;
    private final int dimension;
    private Tile startingPipe;
    private Tile endingPipe;
    private final Random random;

    public Board(int dimension) {
        this.dimension = dimension;
        this.random = new Random();
        this.generateBoard();
        this.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        this.setBackground(Color.darkGray);
    }

    private void generateBoard() {
        this.initializeBoard();
        HashSet<Tile> visitedTiles = new HashSet<>();
        ArrayList<Tile> pathTiles = new ArrayList<>();
        visitedTiles.add(this.startingPipe);
        depthFirstSearch(this.startingPipe, this.endingPipe, visitedTiles, pathTiles);
        this.setNextAndPreviousInPath(pathTiles);
        this.setPipeState(pathTiles);
    }

    private void initializeBoard() {
        this.board = new Tile[dimension][dimension];
        this.setLayout(new GridLayout(dimension, dimension));
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.board[i][j] = new Tile();
                this.add(this.board[i][j]);
            }
        }

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++)  {
                if (i != 0) {
                    this.board[i][j].addNeighbour(Direction.UP, this.board[i-1][j]);
                }
                if (i != dimension - 1) {
                    this.board[i][j].addNeighbour(Direction.DOWN, this.board[i+1][j]);
                }
                if (j != 0) {
                    this.board[i][j].addNeighbour(Direction.LEFT, this.board[i][j-1]);
                }
                if (j != dimension - 1) {
                    this.board[i][j].addNeighbour(Direction.RIGHT, this.board[i][j+1]);
                }
            }
        }
        this.initializeStartingAndEndingPipes();
    }

    private void initializeStartingAndEndingPipes() {
        this.startingPipe = this.board[this.random.nextInt(dimension)][0];
        this.endingPipe = this.board[this.random.nextInt(dimension)][this.dimension-1];
        this.startingPipe.setState(State.START);
        this.endingPipe.setState(State.FINISH);
    }

    private void setNextAndPreviousInPath(ArrayList<Tile> pathTiles) {
        setNextInPath(pathTiles);
        setPreviousInPath(pathTiles);
    }

    private void setNextInPath(ArrayList<Tile> pathTiles) {
        for (int i = 0; i < pathTiles.size() - 1; i++) {
            Tile currentTile = pathTiles.get(i);
            Tile nextTile = pathTiles.get(i + 1);
            for (Direction direction : currentTile.getNeighbours().keySet()) {
                Tile neighbor = currentTile.getNeighbours().get(direction);
                if (neighbor == nextTile) {
                    currentTile.setNext(direction);
                }
            }
        }
    }

    private void setPreviousInPath(ArrayList<Tile> pathTiles) {
        for (int i = 1; i < pathTiles.size(); i++) {
            Tile currentTile = pathTiles.get(i);
            Tile previousTile = pathTiles.get(i - 1);
            for (Direction direction : currentTile.getNeighbours().keySet()) {
                Tile neighbor = currentTile.getNeighbours().get(direction);
                if (neighbor == previousTile) {
                    currentTile.setPrevious(direction);
                }
            }
        }
    }

    private boolean depthFirstSearch(Tile currentTile, Tile endTile, HashSet<Tile> visitedTiles, ArrayList<Tile> pathTiles) {
        if (currentTile == endTile) {
            pathTiles.add(currentTile);
            return true;
        }
        ArrayList<Tile> neighbours = currentTile.getAllNeighbour();
        Collections.shuffle(neighbours);
        for (Tile neighbour : neighbours) {
            if (!visitedTiles.contains(neighbour)) {
                visitedTiles.add(neighbour);
                pathTiles.add(currentTile);
                if (depthFirstSearch(neighbour, endTile, visitedTiles, pathTiles)) {
                    return true;
                }
                pathTiles.remove(pathTiles.size()-1);
            }
        }
        return false;
    }

    private void setPipeState(ArrayList<Tile> pathTiles) {
        for (int i = 0; i < pathTiles.size(); i++) {
            Tile currentTile = pathTiles.get(i);
            if (i == 0 || i == pathTiles.size() - 1){
                if (i == 0) {
                    currentTile.setState(State.START);
                } else {
                    currentTile.setState(State.FINISH);
                }
                currentTile.getOutputs().set(0, null);
                currentTile.getOutputs().set(1, Direction.UP);
            } else {
                Direction previous = currentTile.getPrevious();
                Direction next = currentTile.getNext();

                if ((previous == Direction.DOWN && next == Direction.UP) || (previous == Direction.UP && next == Direction.DOWN) ||
                        (previous == Direction.LEFT && next == Direction.RIGHT) || (previous == Direction.RIGHT && next == Direction.LEFT)) {
                    currentTile.setState(State.IPIPE);
                    currentTile.getOutputs().set(0, Direction.DOWN);
                    currentTile.getOutputs().set(1, Direction.UP);
                } else {
                    currentTile.setState(State.LPIPE);
                    currentTile.getOutputs().set(0, Direction.UP);
                    currentTile.getOutputs().set(1, Direction.RIGHT);
                }
            }
        }
    }

    public void resetWaterInPipes() {
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                this.board[i][j].setHasWater(false);
            }
        }
    }

    public boolean checkPipes() {
        Tile currentPipe = startingPipe;
        Tile nextPipe = currentPipe.outputTwo();
        ArrayList<Tile> correctPipes = new ArrayList<>();
        correctPipes.add(currentPipe);

        while(true) {
            if (nextPipe == null || nextPipe.isEmpty()) {
                break;
            }

            if (nextPipe.outputOne() == null || nextPipe.outputTwo() == null) {
                if (nextPipe.getState().equals(State.FINISH)){
                    if (nextPipe.outputOne() == currentPipe || nextPipe.outputTwo() == currentPipe) {
                        correctPipes.add(nextPipe);
                        break;
                    }
                } else {
                    if (nextPipe.outputOne() == currentPipe || nextPipe.outputTwo() == currentPipe){
                        correctPipes.add(nextPipe);
                    }
                    break;
                }
            }
            if (nextPipe.outputOne() == currentPipe || nextPipe.outputTwo() == currentPipe) {
                correctPipes.add(nextPipe);
                if (currentPipe == nextPipe.outputOne()) {
                    currentPipe = nextPipe;
                    nextPipe = nextPipe.outputTwo();
                } else if (currentPipe == nextPipe.outputTwo()) {
                    currentPipe = nextPipe;
                    nextPipe = nextPipe.outputOne();
                }
            } else {
                break;
            }
        }

        if (correctPipes.get(correctPipes.size()-1).getState().equals(State.FINISH)) {
            return true;
        } else {
            for (Tile correctPipe : correctPipes) {
                correctPipe.setHasWater(true);
                this.repaint();
            }
            return false;
        }
    }
}