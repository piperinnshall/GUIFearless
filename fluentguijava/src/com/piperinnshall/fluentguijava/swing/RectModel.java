package com.piperinnshall.fluentguijava.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;

class RectModel implements Model {

  private final int width;
  private final int height;
  private final int borderX;
  private final int borderY;
  private final int borderWidth;
  private final int borderHeight;
  private int rectX;
  private int rectY;
  private Point rectSize;
  private Tween clickTween;
  private Tween snapTween;
  private Tween borderSnapTween;

  private int dragOffsetX;
  private int dragOffsetY;

  private boolean hovered;
  private boolean dragging;
  private boolean snapping;
  private boolean borderSnapping;

  RectModel(Runnable task, int width, int height) {
    this.width = width;
    this.height = height;
    this.borderX = (int) (width * 0.1);
    this.borderY = (int) (height * 0.1);
    this.borderWidth = (int) (width * 0.8);
    this.borderHeight = (int) (height * 0.8);
    this.rectX = width / 2;
    this.rectY = height / 2;
    this.rectSize = new Point(60, 60);
    this.clickTween = new Tween(rectSize, rectSize.add(new Point(30, 30)), 0.35f, Lerp.EASE_OUT_BOUNCE,
        Lerp.EASE_IN_CIRC);
    this.snapTween = new Tween(new Point(rectX, rectY), new Point(width / 2, height / 2), 0.4f, Lerp.EASE_OUT_ELASTIC);
    this.borderSnapTween = new Tween(new Point(rectX, rectY), new Point(rectX, rectY), 0.4f, Lerp.EASE_OUT_BACK);
    Timer.of(60)
        .raw(clickTween::update)
        .raw(dt -> {
          if (snapping) {
            snapTween.update(dt);
            rectX = snapTween.point().x();
            rectY = snapTween.point().y();
          }
          if (borderSnapping) {
            borderSnapTween.update(dt);
            rectX = borderSnapTween.point().x();
            rectY = borderSnapTween.point().y();
          }
        })
        .edt(task)
        .start();
  }

  private boolean hitTest(int mx, int my) {
    Point p = clickTween.point();
    return mx >= rectX - p.x() && mx <= rectX + p.x()
        && my >= rectY - p.y() && my <= rectY + p.y();
  }

  private boolean overCenter(int x, int y) {
    Point p = rectSize;
    int dot = 256 / 2;
    return x - p.x() <= width / 2 + dot && x + p.x() >= width / 2 - dot
        && y - p.y() <= height / 2 + dot && y + p.y() >= height / 2 - dot;
  }

  private boolean outsideBorder(int x, int y) {
    Point p = clickTween.point();
    return x - p.x() < borderX || x + p.x() > borderX + borderWidth
        || y - p.y() < borderY || y + p.y() > borderY + borderHeight;
  }

  private Point clampToBorder(int x, int y) {
    Point p = rectSize;
    int clampedX = Math.max(borderX + p.x(), Math.min(borderX + borderWidth - p.x(), x));
    int clampedY = Math.max(borderY + p.y(), Math.min(borderY + borderHeight - p.y(), y));
    return new Point(clampedX, clampedY);
  }

  @Override
  public List<Draw> draws() {
    return List.of(
        g2d -> {
          g2d.setStroke(new BasicStroke(5));
          g2d.setColor(new Color(0xE8D5B7));
          g2d.drawRoundRect(borderX, borderY, borderWidth, borderHeight, 30, 30);
        },
        g2d -> {
          g2d.setStroke(new BasicStroke(2));
          int dot = 256;
          g2d.setColor(new Color(0xF7D9C4));
          g2d.drawOval(width / 2 - dot / 2, height / 2 - dot / 2, dot, dot);
        },
        g2d -> {
          Point p = clickTween.point();
          g2d.setColor(hovered ? new Color(0xF2C6DE) : new Color(0xDBCDF0));
          g2d.fillRoundRect(rectX - p.x(), rectY - p.y(), p.x() * 2, p.y() * 2, 20, 20);
        });
  }

  @Override
  public void mousePressed(int mx, int my) {
    if (hitTest(mx, my)) {
      dragging = true;
      snapping = false;
      borderSnapping = false;
      clickTween.trigger();
      dragOffsetX = mx - rectX;
      dragOffsetY = my - rectY;
    }
  }

  @Override
  public void mouseReleased(int mx, int my) {
    dragging = false;
    clickTween.reverse();
    if (overCenter(rectX, rectY)) {
      snapTween = new Tween(new Point(rectX, rectY), new Point(width / 2, height / 2), 0.4f, Lerp.EASE_OUT_ELASTIC);
      snapTween.trigger();
      snapping = true;
    } else if (outsideBorder(rectX, rectY)) {
      Point target = clampToBorder(rectX, rectY);
      borderSnapTween = new Tween(new Point(rectX, rectY), target, 0.4f, Lerp.EASE_OUT_BACK);
      borderSnapTween.trigger();
      borderSnapping = true;
    }
    mouseMoved(mx, my);
  }

  @Override
  public void mouseDragged(int mx, int my) {
    if (dragging) {
      rectX = mx - dragOffsetX;
      rectY = my - dragOffsetY;
    } else {
      mouseMoved(mx, my);
    }
  }

  @Override
  public void mouseMoved(int mx, int my) {
    boolean over = hitTest(mx, my);
    if (over && !hovered) {
      hovered = true;
    } else if (!over && hovered) {
      hovered = false;
    }
  }

  @Override
  public void mouseExited() {
    hovered = false;
  }

}
