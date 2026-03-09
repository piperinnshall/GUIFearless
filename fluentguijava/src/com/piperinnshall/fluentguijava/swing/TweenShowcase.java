package com.piperinnshall.fluentguijava.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JPanel;

class TweenShowcase extends JPanel implements KeyListener {
  Model model;

  BasicStroke stroke = new BasicStroke(3);
  Color background = new Color(0xF6F0EB);
  Color border = new Color(0xE8D5B7);
  List<Color> colors = List.of(new Color(0xDBCDF0), new Color(0xF2C6DE), new Color(0xF7D9C4));

  TweenShowcase() {
    setPreferredSize(new Dimension(1000, 1000));
    setBackground(background);
    model = new Model(this::repaint);
  }

  @Override
  public void paintComponent(Graphics g) {
    var g2d = (Graphics2D) g;
    super.paintComponent(g2d);

    g2d.setStroke(stroke);
    g2d.setColor(border);
    g2d.drawRoundRect(48, 48, 904, 904, 30, 30);

    var radius = 200;
    var points = model.points();
    for (int i = 0; i < points.size(); i++) {
      g2d.setColor(colors.get(i));
      g2d.fillOval(points.get(i).x() - radius / 2, points.get(i).y() - radius / 2, radius, radius);
    }
  }

  public void keyTyped(KeyEvent e) { }
  public void keyPressed(KeyEvent e) { model.press(e); }
  public void keyReleased(KeyEvent e) { model.release(e); }
}

record Model(Runnable task, Tween... tweens) {
  Model(Runnable task) {
    this(task,
        new Tween(new Point(225, 850), new Point(225, 150), 2.0f, Lerp.EASE_OUT_ELASTIC),
        new Tween(new Point(500, 850), new Point(500, 150), 2.0f, Lerp.EASE_IN_BOUNCE),
        new Tween(new Point(775, 850), new Point(775, 150), 2.0f, Lerp.EASE_OUT_BACK));
  }

  Model {
    Timer.of(60)
        .raw(dt -> tweenStream().forEach(t -> t.update(dt)))
        .edt(task)
        .start();
  }

  List<Point> points() {
    return tweenStream().map(Tween::point).toList();
  }

  void press(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE)
      tweenStream().forEach(Tween::trigger);
  }

  void release(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE)
      tweenStream().forEach(Tween::reverse);
  }

  private Stream<Tween> tweenStream() {
    return Arrays.stream(tweens);
  }
}
