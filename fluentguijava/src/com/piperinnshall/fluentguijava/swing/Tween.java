package com.piperinnshall.fluentguijava.swing;

import java.util.function.UnaryOperator;

record Point(int x, int y) {
  Point add(Point point) {
    return new Point(x + point.x, y + point.y);
  }

  Point sub(Point point) {
    return new Point(x - point.x, y - point.y);
  }

  Point scale(float f) {
    return new Point((int) (x * f), (int) (y * f));
  }
}

class Tween {
  private final Point start;
  private final Point end;
  private final float duration;
  private final UnaryOperator<Float> lerp;
  private final UnaryOperator<Float> reverseLerp;
  private float elapsed;
  private boolean active;
  private boolean reversed;
  private boolean repaint;

  Tween(Point start, Point end, float duration, UnaryOperator<Float> lerp) {
    this(start, end, duration, lerp, lerp);
  }

  Tween(Point start, Point end, float duration, UnaryOperator<Float> lerp, UnaryOperator<Float> reverseLerp) {
    this.start = start;
    this.end = end;
    this.duration = duration;
    this.lerp = lerp;
    this.reverseLerp = reverseLerp;
    this.elapsed = 0;
    this.active = false;
    this.repaint = false;
  }

  boolean repaint() {
    return repaint;
  }

  void reset() {
    elapsed = 0;
  }

  void trigger() {
    active = true;
    reversed = false;
    repaint = true;
  }

  void reverse() {
    active = true;
    reversed = true;
    repaint = true;
  }

  void update(float dt) {
    if (!active) {
      repaint = false;
      return;
    }
    repaint = true;
    if (reversed)
      stepBackward(dt);
    else
      stepForward(dt);
  }

  private void stepForward(float dt) {
    elapsed = Math.min(elapsed + dt, duration);
    if (elapsed >= duration) {
      elapsed = duration;
      active = false;
    }
  }

  private void stepBackward(float dt) {
    elapsed = Math.max(elapsed - dt, 0);
    if (elapsed <= 0) {
      elapsed = 0;
      active = false;
    }
  }

  Point point() {
    float t = elapsed / duration;
    float progress = (reversed ? reverseLerp : lerp).apply(t);
    Point delta = end.sub(start);
    return start.add(delta.scale(progress));
  }
}
