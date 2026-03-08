package com.piperinnshall.fluentguijava.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class Main {
  public static void main(String args[]) {
    SwingUtilities.invokeLater(Frame::new);
  }
}

class Frame extends JFrame {
  Frame() {
    var root = new Panel();
    setTitle("Animation Player");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    add(root);
    addKeyListener(root);
    setPreferredSize(new Dimension(1000, 1000));
    pack();
    setLocationRelativeTo(null);
    setResizable(false);
    setVisible(true);
    setFocusable(true);
  }

}

class Panel extends JPanel implements KeyListener {
  Tween tween;
  KeyModel keyModel;
  Panel() {
    var timer = new Timer(60, this::repaint);
    tween = new Tween(new Point(0, 0));
    keyModel = new KeyModel(tween);
    timer.start();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.red);
    g.fillOval(tween.point().x(), tween.point().y(), 200, 200);
  }

  public void keyTyped(KeyEvent e) {}
  public void keyPressed(KeyEvent e) { keyModel.press(e); }
  public void keyReleased(KeyEvent e) {}
}

record KeyModel(Tween t) {
  void press(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_SPACE -> { t.add(new Point(100, 100)); System.out.println("Hi"); }
    }
  }
}

record Point(int x, int y) {
  Point add(Point point) {
    return new Point(x + point.x, y + point.y);
  }
}

class Tween {
  Point point;
  Point point() { return point; }
  void point(Point point) { this.point = point; }
  void add(Point point) { this.point = this.point.add(point); }
  Tween(Point point) { point(point); }
}
