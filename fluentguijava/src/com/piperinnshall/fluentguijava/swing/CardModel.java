package com.piperinnshall.fluentguijava.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;

class CardModel implements Model {
  private final int width;
  private final int height;
  private final Tween sizeTween = new Tween(new Point(120, 120), new Point(150, 150), 0.35f, Lerp.EASE_OUT_ELASTIC, Lerp.EASE_IN_CIRC);
  private boolean pressed = false;
  private boolean hovered = false;

  CardModel(Runnable task, int width, int height) {
    this.width = width;
    this.height = height;
    Timer.of(60).raw(sizeTween::update).edt(task).start();
  }

  private boolean hitTest(int mx, int my) {
    Point p = sizeTween.point();
    return mx >= width/2 - p.x() && mx <= width/2 + p.x()
        && my >= height/2 - p.y() && my <= height/2 + p.y();
  }

  @Override
  public void mousePressed(int mx, int my) {
    if (hitTest(mx, my))
      pressed = true;
  }

  @Override
  public void mouseReleased(int mx, int my) {
    pressed = false;
    mouseMoved(mx, my);
  }

  @Override
  public void mouseMoved(int mx, int my) {
    boolean over = hitTest(mx, my);
    if (over && !hovered) {
      hovered = true;
      sizeTween.trigger();
    } else if (!over && hovered) {
      hovered = false;
      sizeTween.reverse();
    }
  }

  @Override
  public void mouseDragged(int mx, int my) {
    if (!pressed)
      mouseMoved(mx, my);
  }

  @Override
  public void mouseExited() {
    hovered = false;
    sizeTween.reverse();
  }

  @Override
  public List<Draw> draws() {
    return List.of(
        g2d -> {
          g2d.setStroke(new BasicStroke(3));
          g2d.setColor(new Color(0xE8D5B7));
          g2d.drawRoundRect((int) (width * 0.048), (int) (height * 0.048), (int) (width * 0.904),
              (int) (height * 0.904), 30, 30);
        },
        g2d -> {
          Point p = sizeTween.point();
          g2d.setColor(pressed ? new Color(0xF2C6DE) : new Color(0xDBCDF0));
          g2d.fillRoundRect(width / 2 - p.x(), height / 2 - p.y(), p.x() * 2, p.y() * 2, 20, 20);
        });
  }
}
