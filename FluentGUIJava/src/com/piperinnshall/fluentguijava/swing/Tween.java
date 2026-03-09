package com.piperinnshall.fluentguijava.swing;

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
  private final Lerp lerp;
  private float elapsed;
  private boolean active;

  Tween(Point start, Point end, float duration, Lerp lerp) {
    this.start = start;
    this.end = end;
    this.duration = duration;
    this.lerp = lerp;
    this.elapsed = 0;
    this.active = false;
  }

  void trigger() {
    elapsed = 0;
    active = true;
  }

  void update(float dt) {
    if (!active)
      return;
    elapsed = Math.min(elapsed + dt, duration);
    if (elapsed >= duration)
      active = false;
  }

  Point point() {
    float t = elapsed / duration;
    float progress = lerp.apply(t);
    Point delta = end.sub(start);
    return start.add(delta.scale(progress));
  }
}
