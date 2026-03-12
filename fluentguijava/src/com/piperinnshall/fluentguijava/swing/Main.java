package com.piperinnshall.fluentguijava.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.util.List;

class Main {
  public static void main(String[] args) {
    int width = 800, height = 800;
    var root = new Panel(width, height);
    var model = new RectModel(root::repaint, width, height);
    root.model = model;
    SwingUtilities.invokeLater(() -> new Frame(root));
  }
}

class Frame extends JFrame {
  Frame(Panel root) {
    add(root);
    addKeyListener(root);
    addMouseListener(root);
    addMouseMotionListener(root);
    setTitle("Animation Player");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setResizable(false);
    setFocusable(true);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}

class Panel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
  Model model;

  Panel(int width, int height) {
    setPreferredSize(new Dimension(width, height));
    setBackground(new Color(0xF6F0EB));
  }

  public void paintComponent(Graphics g) {
    var g2d = (Graphics2D) g;
    super.paintComponent(g2d);
    model.draws().forEach(d -> d.draw(g2d));
  }

  public void keyTyped(KeyEvent e) { model.keyTyped(e.getKeyChar()); }
  public void keyPressed(KeyEvent e) { model.keyPressed(e.getKeyCode()); }
  public void keyReleased(KeyEvent e) { model.keyReleased(e.getKeyCode()); }
  public void mouseClicked(MouseEvent e) { model.mouseClicked(e.getX(), e.getY()); }
  public void mousePressed(MouseEvent e) { model.mousePressed(e.getX(), e.getY()); }
  public void mouseReleased(MouseEvent e){ model.mouseReleased(e.getX(), e.getY()); }
  public void mouseEntered(MouseEvent e) { model.mouseEntered(); }
  public void mouseExited(MouseEvent e) { model.mouseExited(); }
  public void mouseMoved(MouseEvent e) { model.mouseMoved(e.getX(), e.getY()); }
  public void mouseDragged(MouseEvent e) { model.mouseDragged(e.getX(), e.getY()); }

}

interface Draw { void draw(Graphics2D g2d); }
interface Model {
  List<Draw> draws();
  default void keyTyped(char c) {}
  default void keyPressed(int keyCode) {}
  default void keyReleased(int keyCode) {}
  default void mouseClicked(int mx, int my) {}
  default void mousePressed(int mx, int my) {}
  default void mouseReleased(int mx, int my) {}
  default void mouseEntered() {}
  default void mouseExited() {}
  default void mouseMoved(int mx, int my) {}
  default void mouseDragged(int mx, int my) {}
}

