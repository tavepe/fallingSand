package com.tavepe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    final int WIDTH = 100, HEIGHT = 100, SIZE = 6;
    int[][] mundo = new int[WIDTH][HEIGHT];
    Timer timer = new Timer(30, this);

    public Main() {
        setPreferredSize(new Dimension(WIDTH * SIZE, HEIGHT * SIZE));
        addMouseListener(this);
        addMouseMotionListener( this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (mundo[x][y] == 1) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(x * SIZE, y * SIZE, SIZE, SIZE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        refreshWorld();
        repaint();
    }

    void refreshWorld() {
        for (int y = HEIGHT - 3; y >= 0; y--) {
            for (int x = 2; x < WIDTH-2; x++) {
                if (mundo[x][y] == 1 && mundo[x][y + 1] == 0) {
                    mundo[x][y + 1] = 1;
                    mundo[x][y] = 0;
                } else if (mundo[x][y] == 1 && mundo[x][y+2]==1){
                    if(mundo[x + 1][y + 1] == 0){
                        mundo[x][y] = 0;
                        mundo[x+1][y] = 1;
                    }
                    if(mundo[x - 1][y + 1] == 0){
                        mundo[x][y] = 0;
                        mundo[x-1][y] = 1;
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / SIZE;
        int y = e.getY() / SIZE;
        if (x >= 2 && x < WIDTH-2 && y >= 0 && y < HEIGHT)
            mundo[x][y] = 1;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / SIZE;
        int y = e.getY() / SIZE;
        if (x >= 2 && x < WIDTH-2 && y >= 0 && y < HEIGHT)
            mundo[x][y] = 1;
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}


    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulação de Areia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Main());
        frame.pack();
        frame.setVisible(true);
    }
}
