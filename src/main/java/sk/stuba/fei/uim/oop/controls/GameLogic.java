package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.board.State;
import sk.stuba.fei.uim.oop.board.Tile;
import sk.stuba.fei.uim.oop.gui.Game;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class GameLogic extends UniversalAdapter{
    public static final int INITIAL_BOARD_SIZE = 8;
    private final JFrame mainGame;
    private Board currentBoard;
    @Getter
    private final JLabel label;
    @Getter
    private final JLabel boardSizeLabel;
    private int currentBoardSize;
    private int currentLevel;

    public GameLogic(JFrame mainGame) {
        this.mainGame = mainGame;
        this.currentBoardSize = INITIAL_BOARD_SIZE;
        this.currentLevel = 1;
        this.initializeNewBoard(this.currentBoardSize);
        this.mainGame.add(this.currentBoard);
        this.label = new JLabel();
        this.boardSizeLabel = new JLabel();
        this.updateLevelLabel();
        this.updateBoardSizeLabel();
        this.mainGame.setFocusable(true);
        this.mainGame.requestFocus();
    }

    private void updateLevelLabel() {
        this.label.setText("LEVEL: " + currentLevel);
        this.label.setHorizontalAlignment(SwingConstants.CENTER);
        this.mainGame.revalidate();
        this.mainGame.repaint();
    }

    private void updateBoardSizeLabel() {
        this.boardSizeLabel.setText("CURRENT BOARD SIZE: " + this.currentBoardSize);
        this.boardSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void initializeNewBoard(int dimension) {
        this.currentBoard = new Board(dimension);
        this.currentBoard.addMouseMotionListener(this);
        this.currentBoard.addMouseListener(this);
    }

    private void newLevel() {
        this.mainGame.remove(this.currentBoard);
        this.initializeNewBoard(this.currentBoardSize);
        this.mainGame.add(currentBoard);
        this.currentLevel++;
        this.updateLevelLabel();
        this.mainGame.setFocusable(true);
        this.mainGame.requestFocus();
    }

    private void gameRestart() {
        this.newLevel();
        this.currentLevel = 1;
        this.updateLevelLabel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Game.RESTART_BUTTON:
                this.gameRestart();
                break;
            case Game.CHECK_BUTTON:
                if (this.currentBoard.checkPipes()) {
                    this.newLevel();
                    this.mainGame.revalidate();
                    this.mainGame.repaint();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!((JSlider) e.getSource()).getValueIsAdjusting() &&  ((JSlider) e.getSource()).getValue() != this.currentBoardSize) {
            this.currentBoardSize = ((JSlider) e.getSource()).getValue();
            this.updateBoardSizeLabel();
            this.gameRestart();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                this.gameRestart();
                break;
            case KeyEvent.VK_ESCAPE:
                this.mainGame.dispose();
                System.exit(0);
                break;
            case KeyEvent.VK_ENTER:
                if(this.currentBoard.checkPipes()) {
                    this.newLevel();
                    this.mainGame.revalidate();
                    this.mainGame.repaint();
                }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component current = this.currentBoard.getComponentAt(e.getX(), e.getY());
        if (!(current instanceof Tile)) {
            this.currentBoard.repaint();
            return;
        }
        ((Tile) current).setHighlight(true);
        this.mainGame.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Component current = this.currentBoard.getComponentAt(e.getX(), e.getY());
        if (!(current instanceof Tile)) {
            return;
        }
        ((Tile) current).setHighlight(true);
        if (((Tile) current).getState() != State.EMPTY) {
            this.currentBoard.resetWaterInPipes();
            ((Tile) current).rotateTile();
        }
        this.currentBoard.repaint();
    }
}