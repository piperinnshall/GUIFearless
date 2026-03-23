package com.piperinnshall.fluentguijava.vec;

@SuppressWarnings("unchecked")
public interface Vec<T extends Vec<T>> {
  T add(T point);
  T sub(T point);
  T add(float scalar);
  T sub(float scalar);
  T div(float scalar);
  T mul(float scalar);
  float dot(T v);
  default T normalize() { return div(len()); }
  default float len() { return (float) Math.sqrt(lenSq()); }
  default float lenSq() { return dot((T) this); }
  default float dist(T other) { return sub(other).len(); }
  default float distSq(T other) { return sub(other).lenSq(); }
}

