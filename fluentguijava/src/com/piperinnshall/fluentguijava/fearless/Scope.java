package com.piperinnshall.fluentguijava.fearless;

@FunctionalInterface
public interface Scope<B> {
  void run(B builder);
  static <B> Scope<B> nop() { return _ -> {}; }
}
