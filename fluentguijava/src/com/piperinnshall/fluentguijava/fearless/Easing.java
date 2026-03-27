package com.piperinnshall.fluentguijava.fearless;

/**
 * Easing functions from https://easings.net/
 */
public interface Easing {
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
