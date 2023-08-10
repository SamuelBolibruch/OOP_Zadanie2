package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {
    public static final String RESTART_BUTTON = "RESTART";
    public static final String CHECK_BUTTON = "CHECK";

    public Game() {
        JFrame frame = new JFrame("Pipes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,700);
        frame.getContentPane().setBackground(Color.darkGray);
        frame.setResizable(false);
        frame.requestFocusInWindow();

        GameLogic logic = new GameLogic(frame);
        frame.addKeyListener(logic);

        JPanel sideMenu = new JPanel();
        sideMenu.setBackground(Color.LIGHT_GRAY);
        JButton buttonRestart = new JButton(RESTART_BUTTON);
        buttonRestart.addActionListener(logic);
        buttonRestart.setFocusable(false);

        JButton buttonCheck = new JButton(CHECK_BUTTON);
        buttonCheck.addActionListener(logic);
        buttonCheck.setFocusable(false);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 8, 10, 8);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(logic);

        sideMenu.setLayout(new GridLayout(2, 3));
        sideMenu.add(logic.getBoardSizeLabel());
        sideMenu.add(buttonCheck);
        sideMenu.add(logic.getLabel());
        sideMenu.add(slider);
        sideMenu.add(buttonRestart);
        frame.add(sideMenu, BorderLayout.PAGE_END);

        frame.setVisible(true);
    }
}
