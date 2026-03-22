package com.piperinnshall.fluentguijava.main.vec;

public record Vec2(float x, float y) implements Vec<Vec2> {
  public Vec2(float scalar) { this(scalar, scalar); }
  public Vec2 add(Vec2 point) { return new Vec2(x + point.x, y + point.y); }
  public Vec2 sub(Vec2 point) { return new Vec2(x - point.x, y - point.y); }
  public Vec2 add(float scalar) { return new Vec2(x + scalar, y + scalar); }
  public Vec2 sub(float scalar) { return new Vec2(x - scalar, y - scalar); }
  public Vec2 mul(float scalar) { return new Vec2(x * scalar, y * scalar); }
  public Vec2 div(float scalar) { return new Vec2(x / scalar, y / scalar); }
  public float dot(Vec<Vec2> v) { var v2 = (Vec2) v; return x * v2.x() + y * v2.y(); }
}
