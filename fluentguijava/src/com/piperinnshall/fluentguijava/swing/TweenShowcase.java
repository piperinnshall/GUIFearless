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
  private static int WIDTH = 800;
  private static int HEIGHT = 800;
  private static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
  private static final BasicStroke STROKE = new BasicStroke(3);
  private static final Color BACKGROUND = new Color(0xF6F0EB);
  private static final Color BORDER = new Color(0xE8D5B7);
  private static final List<Color> COLORS = List.of(new Color(0xDBCDF0), new Color(0xF2C6DE), new Color(0xF7D9C4));

  Model model;

  TweenShowcase() {
    setPreferredSize(SIZE);
    setBackground(BACKGROUND);
    model = new Model(this::repaint, WIDTH, HEIGHT);
  }

  @Override
  public void paintComponent(Graphics g) {
    var g2d = (Graphics2D) g;
    super.paintComponent(g2d);
    g2d.setStroke(STROKE);
    g2d.setColor(BORDER);

    int pad = (int) (WIDTH * 0.048);
    int innerW = WIDTH - pad * 2;
    int innerH = HEIGHT - pad * 2;
    g2d.drawRoundRect(pad, pad, innerW, innerH, 30, 30);

    int radius = (int) (WIDTH * 0.2);
    var points = model.points();
    for (int i = 0; i < points.size(); i++) {
      g2d.setColor(COLORS.get(i));
      g2d.fillOval(points.get(i).x() - radius / 2, points.get(i).y() - radius / 2, radius, radius);
    }
  }

  public void keyTyped(KeyEvent e) { }
  public void keyPressed(KeyEvent e) { model.press(e); }
  public void keyReleased(KeyEvent e) { model.release(e); }
}

record Model(Runnable task, Tween... tweens) {
  Model(Runnable task, int w, int h) {
    this(task,
        new Tween(new Point((int)(w * 0.225), (int)(h * 0.85)), new Point((int)(w * 0.225), (int)(h * 0.15)), 2.0f, Lerp.EASE_OUT_ELASTIC),
        new Tween(new Point((int)(w * 0.500), (int)(h * 0.85)), new Point((int)(w * 0.500), (int)(h * 0.15)), 2.0f, Lerp.EASE_IN_BOUNCE),
        new Tween(new Point((int)(w * 0.775), (int)(h * 0.85)), new Point((int)(w * 0.775), (int)(h * 0.15)), 2.0f, Lerp.EASE_OUT_BACK));
  }

  Model {
    Timer.of(60)
        .raw(dt -> tweenStream().forEach(t -> t.update(dt)))
        .edtPredicate(_ -> tweenStream().anyMatch(Tween::repaint), task)
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
