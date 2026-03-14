package com.piperinnshall.fluentguijava.fluentgraphics;

import java.awt.Dimension;
import java.awt.Point;

/* 
 * Library-only point (do not expose to library user)
 */
record Point2(int x, int y) {
  static Point2 round(Vec2 v) {
    return new Point2(Math.round(v.x()), Math.round(v.y()));
  }
  Dimension awtDimension() { return new Dimension(x, y); }
  Point awtPoint() { return new Point(x, y); }
}


public record Vec2(float x, float y) {
  Vec2(float scalar) {
    this(scalar, scalar);
  }
  Vec2 add(Vec2 point) {
    return new Vec2(x + point.x, y + point.y);
  }
  Vec2 sub(Vec2 point) {
    return new Vec2(x - point.x, y - point.y);
  }
  Vec2 sub(float scalar) {
    return new Vec2(x - scalar, y - scalar);
  }
  Vec2 mul(float scalar) {
    return new Vec2(x * scalar, y * scalar);
  }
  Vec2 div(float scalar) {
    return new Vec2(x / scalar, y / scalar);
  }
}

