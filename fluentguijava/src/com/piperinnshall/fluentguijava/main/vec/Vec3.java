package com.piperinnshall.fluentguijava.main.vec;

public record Vec3(float x, float y, float z) implements Vec<Vec3> {
  Vec3(float scalar) {
    this(scalar, scalar, scalar);
  }
  public Vec3 add(Vec3 point) { return new Vec3(x + point.x, y + point.y, z + point.z); }
  public Vec3 sub(Vec3 point) { return new Vec3(x - point.x, y - point.y, z - point.z); }
  public Vec3 add(float scalar) { return new Vec3(x + scalar, y + scalar, z + scalar); }
  public Vec3 sub(float scalar) { return new Vec3(x - scalar, y - scalar, z - scalar); }
  public Vec3 mul(float scalar) { return new Vec3(x * scalar, y * scalar, z * scalar); }
  public Vec3 div(float scalar) { return new Vec3(x / scalar, y / scalar, z / scalar); }
  public float dot(Vec3 v) {
    var v3 = (Vec3) v;
    return x * v3.x() + y * v3.y() + z * v3.z();
  }
  public Vec3 cross(Vec3 v) {
    return new Vec3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
  }
  public static Vec3 fromHSV(float h, float s, float v) {
    var i = (int)(h * 6);
    var f = h * 6 - i;
    var q = v * (1 - f);
    var t = v * f;
    var p = v * (1 - s);
    var r = switch (i % 6) { case 0, 5 -> v; case 1 -> q; case 2, 3 -> p; default -> t; };
    var g = switch (i % 6) { case 0 -> t; case 1, 2 -> v; case 3 -> q; default -> p; };
    var b = switch (i % 6) { case 0, 1 -> p; case 2 -> t; case 3, 4 -> v; default -> q; };
    return new Vec3(r * 255, g * 255, b * 255);
  }
}
