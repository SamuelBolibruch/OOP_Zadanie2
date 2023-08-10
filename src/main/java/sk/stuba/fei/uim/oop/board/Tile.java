package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tile extends JPanel {
    @Getter
    private final Map<Direction, Tile> neighbours;
    @Getter @Setter
    private State state;
    @Getter @Setter
    private Direction previous;
    @Getter @Setter
    private Direction next;
    @Getter
    private final ArrayList<Direction> outputs;
    @Setter
    private boolean highlight;
    @Setter
    private boolean hasWater;
    private Rotate rotate;

    public Tile() {
        this.state = State.EMPTY;
        this.setBackground(Color.GRAY);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.neighbours = new HashMap<>();
        this.previous = null;
        this.next = null;
        this.outputs = new ArrayList<>(2);
        this.outputs.add(null);
        this.outputs.add(null);
        this.highlight = false;
        this.rotate = Rotate.FIRST_POSITION;
        this.rotate = rotate.getRandomRotate();
        this.hasWater = false;
        this.setPreferredSize(new Dimension(50,50));
    }

    public void rotateTile() {
        this.rotate = this.rotate.moveForward();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.state.equals(State.START)|| this.state.equals(State.FINISH)) {
            g.setColor(Color.BLACK);
            int ovalWidth = (int) (this.getWidth() * 0.5);
            int ovalHeight = (int) (this.getHeight() * 0.5);
            int ovalX = (this.getWidth() - ovalWidth) / 2;
            int ovalY = (this.getHeight() - ovalHeight) / 2;
            g.fillOval(ovalX, ovalY, ovalWidth, ovalHeight);
        }

        if (highlight) {
            g.setColor(Color.YELLOW);
            ((Graphics2D) g).setStroke(new BasicStroke(7));
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            this.highlight = false;
        }

        this.setColor(g);

        switch (this.state) {
            case START:
            case FINISH:
                drawStartFinish(g);
                break;
            case IPIPE:
                drawIpipe(g);
                break;
            case LPIPE:
                drawLpipe(g);
                break;
            default:
                break;
        }
        this.changeOutputs();
    }

    private void setColor(Graphics g) {
        Color color = hasWater ? Color.BLUE : Color.BLACK;

        if (this.state.equals(State.START)) {
            color = hasWater ? Color.BLUE : Color.GREEN;
        } else if (this.state.equals(State.FINISH)) {
            color = Color.RED;
        }

        g.setColor(color);
    }

    private void drawStartFinish(Graphics g) {
        switch (this.rotate) {
            case FIRST_POSITION:
                g.fillRect((int) (this.getWidth() * 0.4), 0, (int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.5));
                break;
            case SECOND_POSITION:
                g.fillRect((int) (this.getWidth() * 0.5), (int)(this.getHeight() * 0.4), (int) (getWidth() * 0.5), (int)(getHeight() * 0.2));
                break;
            case THIRD_POSITION:
                g.fillRect((int) (this.getWidth() * 0.4), (int)(this.getHeight() * 0.5), (int) (getWidth() * 0.2), (int)(getHeight() * 0.5));
                break;
            case FOURTH_POSITION:
                g.fillRect(0, (int)(this.getHeight() * 0.4), (int) (getWidth() * 0.5), (int)(getHeight() * 0.2));
                break;
            default:
                break;
        }
    }

    private void drawIpipe(Graphics g) {
        switch (this.rotate) {
            case FIRST_POSITION:
            case THIRD_POSITION:
                g.fillRect((int) (this.getWidth() * 0.4), 0, (int) (this.getWidth() * 0.2), this.getHeight());
                break;
            case SECOND_POSITION:
            case FOURTH_POSITION:
                g.fillRect(0, (int) (this.getHeight() * 0.4), this.getWidth(), (int) (this.getHeight() * 0.2));
                break;
            default:
                break;
        }
    }

    private void drawLpipe(Graphics g) {
        switch (this.rotate) {
            case FIRST_POSITION:
                g.fillRect((int) (this.getWidth() * 0.4), 0, (int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.4));
                g.fillRect((int) (this.getWidth() * 0.4), (int) (this.getHeight() * 0.4), (int) (getWidth() * 0.6), (int) (getHeight() * 0.2));
                break;
            case SECOND_POSITION:
                g.fillRect((int) (this.getWidth() * 0.4), (int) (this.getWidth() * 0.4), (int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.6));
                g.fillRect((int) (this.getWidth() * 0.4), (int) (this.getHeight() * 0.4), (int) (getWidth() * 0.6), (int) (getHeight() * 0.2));
                break;
            case THIRD_POSITION:
                g.fillRect((int) (this.getWidth() * 0.4), (int) (this.getWidth() * 0.4), (int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.6));
                g.fillRect(0, (int) (this.getHeight() * 0.4), (int) (getWidth() * 0.59), (int) (getHeight() * 0.2));
                break;
            case FOURTH_POSITION:
                g.fillRect((int) (this.getWidth() * 0.4), 0, (int) (this.getWidth() * 0.2), (int) (this.getHeight() * 0.59));
                g.fillRect(0, (int) (this.getHeight() * 0.4), (int) (this.getWidth() * 0.5), (int) (this.getHeight() * 0.2));
                break;
        }
    }

    private void changeOutputs() {
        switch (this.state) {
            case START:
            case FINISH:
                if (this.rotate.equals(Rotate.FIRST_POSITION)) {
                    setOutputs(null, Direction.UP);
                } else if (this.rotate.equals(Rotate.SECOND_POSITION)) {
                    setOutputs(null, Direction.RIGHT);
                } else if (this.rotate.equals(Rotate.THIRD_POSITION)) {
                    setOutputs(null, Direction.DOWN);
                } else if (this.rotate.equals(Rotate.FOURTH_POSITION)) {
                    setOutputs(null, Direction.LEFT);
                }
                break;
            case IPIPE:
                if (this.rotate.equals(Rotate.FIRST_POSITION) || this.rotate.equals(Rotate.THIRD_POSITION)) {
                    setOutputs(Direction.DOWN, Direction.UP);
                } else {
                    setOutputs(Direction.LEFT, Direction.RIGHT);
                }
                break;
            case LPIPE:
                if (this.rotate.equals(Rotate.FIRST_POSITION)) {
                    setOutputs(Direction.UP, Direction.RIGHT);
                } else if (this.rotate.equals(Rotate.SECOND_POSITION)) {
                    setOutputs(Direction.RIGHT, Direction.DOWN);
                } else if (this.rotate.equals(Rotate.THIRD_POSITION)) {
                    setOutputs(Direction.DOWN, Direction.LEFT);
                } else if (this.rotate.equals(Rotate.FOURTH_POSITION)) {
                    setOutputs(Direction.LEFT, Direction.UP);
                }
                break;
        }
    }

    private void setOutputs(Direction output1, Direction output2) {
        this.getOutputs().set(0, output1);
        this.getOutputs().set(1, output2);
    }

    public void addNeighbour(Direction direction, Tile tile) {
        this.neighbours.put(direction, tile);
    }

    public ArrayList<Tile> getAllNeighbour() {
        return new ArrayList<>(this.neighbours.values());
    }

    public Tile outputOne() {
        return this.getNeighbours().get(this.getOutputs().get(0));
    }

    public Tile outputTwo() {
        return this.getNeighbours().get(this.getOutputs().get(1));
    }

    public boolean isEmpty() {
        return this.state.equals(State.EMPTY);
    }
}