package com.piperinnshall.fluentguijava.fluentgraphics;

/*
 * A = start position.
 *
 * dt = delta = end - start. The total distance to travel.
 *
 * t = how far through the animation you are as a fraction (0.0 to 1.0)
 * t = elapsedNanos / duration
 *
 * f(t) = Easing(t). Warps t into a new fraction. Must start at 0, end at 1.
 * All values in between can be anything. For example, bounce goes past 1.
 *
 * C = the current position at any time.
 *
 * Formula: C = A + dt * f(t)
*/
public sealed interface Lerp permits Lerp.F, Lerp.V {
  static F of(float start, float end, float duration, Easing easing) {
    return new F(start, end, duration, easing);
  }
  static <T extends Vec<T>> V<T> of(T start, T end, float duration, Easing easing) {
    return new V<>(start, end, duration, easing);
  }
  record F(float start, float end, float duration, Easing easing) implements Lerp {
    float at(long elapsedNanos) {
      var t = Math.clamp(elapsedNanos / (duration * 1_000_000_000f), 0f, 1f);
      return start + (end - start) * easing.apply(t);
    }
  }
  record V<T extends Vec<T>>(T start, T end, float duration, Easing easing) implements Lerp {
    T at(long elapsedNanos) {
      var t = Math.clamp(elapsedNanos / (duration * 1_000_000_000f), 0f, 1f);
      return start.add(end.sub(start).mul(easing.apply(t)));
    }
  }
}
