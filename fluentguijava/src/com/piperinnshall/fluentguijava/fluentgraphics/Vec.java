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

interface Vec<T extends Vec<T>> {
  float x();
  float y();
  T add(T point);
  T sub(T point);
  T add(float scalar);
  T sub(float scalar);
  T div(float scalar);
  T mul(float scalar);
  float dot(Vec<T> v);

  default T normalize() { return div(len()); }
  default float len() { return (float) Math.sqrt(lenSq()); }
  default float lenSq() { return dot(this); }
}

record Vec2(float x, float y) implements Vec<Vec2> {
  Vec2(float scalar) {
    this(scalar, scalar);
  }
  public Vec2 add(Vec2 point) {
    return new Vec2(x + point.x, y + point.y);
  }
  public Vec2 sub(Vec2 point) {
    return new Vec2(x - point.x, y - point.y);
  }
  public Vec2 add(float scalar) {
    return new Vec2(x + scalar, y + scalar);
  }
  public Vec2 sub(float scalar) {
    return new Vec2(x - scalar, y - scalar);
  }
  public Vec2 mul(float scalar) {
    return new Vec2(x * scalar, y * scalar);
  }
  public Vec2 div(float scalar) {
    return new Vec2(x / scalar, y / scalar);
  }
  public float dot(Vec<Vec2> v) {
    return x * v.x() + y * v.y();
  }
}
