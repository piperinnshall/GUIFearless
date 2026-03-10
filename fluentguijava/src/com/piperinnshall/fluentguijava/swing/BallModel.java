package com.piperinnshall.fluentguijava.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

record BallModel(Runnable task, int width, int height, Tween... tweens) implements Model {
  private static final List<Color> COLORS = List.of(new Color(0xDBCDF0), new Color(0xF2C6DE), new Color(0xF7D9C4));

  BallModel(Runnable task, int width, int height) {
    this(task, width, height,
        new Tween(new Point((int) (width * 0.225), (int) (height * 0.85)),
            new Point((int) (width * 0.225), (int) (height * 0.15)), 2.0f, Lerp.EASE_OUT_ELASTIC),
        new Tween(new Point((int) (width * 0.500), (int) (height * 0.85)),
            new Point((int) (width * 0.500), (int) (height * 0.15)), 2.0f, Lerp.EASE_IN_BOUNCE),
        new Tween(new Point((int) (width * 0.775), (int) (height * 0.85)),
            new Point((int) (width * 0.775), (int) (height * 0.15)), 2.0f, Lerp.EASE_OUT_BACK));
  }

  BallModel {
    Timer.of(60)
        .raw(dt -> tweenStream().forEach(t -> t.update(dt)))
        .edtPredicate(_ -> tweenStream().anyMatch(Tween::repaint), task)
        .start();
  }

  private Stream<Tween> tweenStream() {
    return Arrays.stream(tweens);
  }

  @Override
  public List<Draw> draws() {
    int pad = (int) (width * 0.048);
    int radius = (int) (width * 0.2);
    var points = tweenStream().map(Tween::point).toList();
    return List.of(
        g2d -> {
          g2d.setStroke(new BasicStroke(3));
          g2d.setColor(new Color(0xE8D5B7));
          g2d.drawRoundRect(pad, pad, width - pad * 2, height - pad * 2, 30, 30);
        },
        g2d -> {
          for (int i = 0; i < points.size(); i++) {
            g2d.setColor(COLORS.get(i));
            g2d.fillOval(points.get(i).x() - radius / 2, points.get(i).y() - radius / 2, radius, radius);
          }
        });
  }

  @Override
  public void keyPressed(int keyCode) {
    if (keyCode == KeyEvent.VK_SPACE)
      tweenStream().forEach(Tween::trigger);
  }

  @Override
  public void keyReleased(int keyCode) {
    if (keyCode == KeyEvent.VK_SPACE)
      tweenStream().forEach(Tween::reverse);
  }
}
