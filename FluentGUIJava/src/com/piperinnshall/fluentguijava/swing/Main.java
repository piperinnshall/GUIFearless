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
    root.setPreferredSize(new Dimension(1000, 1000));
    add(root);
    addKeyListener(root);

    setTitle("Animation Player");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
    tween = new Tween(new Point(500, 100), new Point(500, 900), 2.0f, Lerp.EASE_OUT_BOUNCE);

    keyModel = new KeyModel(tween);
    Timer.of(60)
        .raw(tween::update)
        .edt(this::repaint)
        .start();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(new Color(100, 200, 120));
    var radius = 200;
    g.fillOval(tween.point().x() - radius / 2, tween.point().y() - radius / 2, radius, radius);
  }

  public void keyTyped(KeyEvent e) {}
  public void keyPressed(KeyEvent e) { keyModel.press(e); }
  public void keyReleased(KeyEvent e) {}
}

record KeyModel(Tween t) {
  void press(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_SPACE -> t.trigger();
    }
  }
}
