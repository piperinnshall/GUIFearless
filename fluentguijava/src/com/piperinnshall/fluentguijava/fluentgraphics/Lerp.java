package com.piperinnshall.fluentguijava.fluentgraphics;

import java.awt.Dimension;
import java.awt.Point;

/* 
 * Library-only point 
 */
record Point2(int x, int y) {
  static Point2 round(Vec2 v) {
    return new Point2(Math.round(v.x()), Math.round(v.y()));
  }
  Dimension awtDimension() { return new Dimension(x, y); }
  Point awtPoint() { return new Point(x, y); }
}

record Vec2(float x, float y) {
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

sealed interface Lerp permits Lerp.F, Lerp.V {
  static F of(float start, float end, float duration, Easing easing) {
    return new F(start, end, duration, easing);
  }
  static V of(Vec2 start, Vec2 end, float duration, Easing easing) {
    return new V(start, end, duration, easing);
  }
  record F(float start, float end, float duration, Easing easing) implements Lerp {
    float at(long elapsedNanos) {
      var t = Math.clamp(elapsedNanos / (duration * 1_000_000_000f), 0f, 1f);
      return start + (end - start) * easing.apply(t);
    }
  }
  record V(Vec2 start, Vec2 end, float duration, Easing easing) implements Lerp {
    Vec2 at(long elapsedNanos) {
      var t = Math.clamp(elapsedNanos / (duration * 1_000_000_000f), 0f, 1f);
      return start.add(end.sub(start).mul(easing.apply(t)));
    }
  }
}

/**
 * Easing functions from https://easings.net/
 */
interface Easing {
  float apply(float t);

  Easing LINEAR = t -> t;

  // Polynomial
  Easing EASE_IN_QUAD = t -> t * t;
  Easing EASE_OUT_QUAD = t -> t * (2 - t);
  Easing EASE_IN_OUT_QUAD = t -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
  Easing EASE_IN_CUBIC = t -> t * t * t;
  Easing EASE_OUT_CUBIC = t -> 1 - (1 - t) * (1 - t) * (1 - t);
  Easing EASE_IN_OUT_CUBIC = t -> t < 0.5f ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
  Easing EASE_IN_QUART = t -> t * t * t * t;
  Easing EASE_OUT_QUART = t -> 1 - (1 - t) * (1 - t) * (1 - t) * (1 - t);
  Easing EASE_IN_OUT_QUART = t -> t < 0.5f ? 8 * t * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 4) / 2;
  Easing EASE_IN_QUINT = t -> t * t * t * t * t;
  Easing EASE_OUT_QUINT = t -> 1 - (1 - t) * (1 - t) * (1 - t) * (1 - t) * (1 - t);
  Easing EASE_IN_OUT_QUINT = t -> t < 0.5f ? 16 * t * t * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 5) / 2;

  // Sine
  Easing EASE_IN_SINE = t -> 1 - (float) Math.cos(t * Math.PI / 2);
  Easing EASE_OUT_SINE = t -> (float) Math.sin(t * Math.PI / 2);
  Easing EASE_IN_OUT_SINE = t -> -((float) Math.cos(Math.PI * t) - 1) / 2;

  // Expo
  Easing EASE_IN_EXPO = t -> t == 0 ? 0 : (float) Math.pow(2, 10 * t - 10);
  Easing EASE_OUT_EXPO = t -> t == 1 ? 1 : 1 - (float) Math.pow(2, -10 * t);
  Easing EASE_IN_OUT_EXPO = t -> t == 0 ? 0
      : t == 1 ? 1 : t < 0.5f ? (float) Math.pow(2, 20 * t - 10) / 2 : (2 - (float) Math.pow(2, -20 * t + 10)) / 2;
  // Circ
  Easing EASE_IN_CIRC = t -> 1 - (float) Math.sqrt(1 - t * t);
  Easing EASE_OUT_CIRC = t -> (float) Math.sqrt(1 - (t - 1) * (t - 1));
  Easing EASE_IN_OUT_CIRC = t -> t < 0.5f ? (1 - (float) Math.sqrt(1 - 4 * t * t)) / 2
      : ((float) Math.sqrt(1 - (float) Math.pow(-2 * t + 2, 2)) + 1) / 2;
  // Back
  Easing EASE_IN_BACK = t -> 2.70158f * t * t * t - 1.70158f * t * t;
  Easing EASE_OUT_BACK = t -> 1 + 2.70158f * (float) Math.pow(t - 1, 3) + 1.70158f * (float) Math.pow(t - 1, 2);
  Easing EASE_IN_OUT_BACK = t -> t < 0.5f ? ((float) Math.pow(2 * t, 2) * ((2.5949095f + 1) * 2 * t - 2.5949095f)) / 2
      : ((float) Math.pow(2 * t - 2, 2) * ((2.5949095f + 1) * (2 * t - 2) + 2.5949095f) + 2) / 2;
  // Elastic
  Easing EASE_IN_ELASTIC = t -> t == 0 ? 0
      : t == 1 ? 1 : -(float) Math.pow(2, 10 * t - 10) * (float) Math.sin((t * 10 - 10.75) * (2 * Math.PI) / 3);
  Easing EASE_OUT_ELASTIC = t -> t == 0 ? 0
      : t == 1 ? 1 : (float) Math.pow(2, -10 * t) * (float) Math.sin((t * 10 - 0.75) * (2 * Math.PI) / 3) + 1;

  // Bounce
  Easing EASE_OUT_BOUNCE = t -> {
    if (t < 1 / 2.75f)
      return 7.5625f * t * t;
    else if (t < 2 / 2.75f) {
      t -= 1.5f / 2.75f;
      return 7.5625f * t * t + 0.75f;
    } else if (t < 2.5 / 2.75) {
      t -= 2.25f / 2.75f;
      return 7.5625f * t * t + 0.9375f;
    } else {
      t -= 2.625f / 2.75f;
      return 7.5625f * t * t + 0.984375f;
    }
  };
  Easing EASE_IN_BOUNCE = t -> 1 - EASE_OUT_BOUNCE.apply(1 - t);
  Easing EASE_IN_OUT_BOUNCE = t -> t < 0.5f ? (1 - EASE_OUT_BOUNCE.apply(1 - 2 * t)) / 2
      : (1 + EASE_OUT_BOUNCE.apply(2 * t - 1)) / 2;

}
