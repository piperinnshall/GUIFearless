package com.piperinnshall.fluentguijava.swing;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class Main {
  public static void main(String args[]) {
    SwingUtilities.invokeLater(Frame::new);
  }
}

class Frame extends JFrame {
  Frame() {
    var root = new TweenShowcase();
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

