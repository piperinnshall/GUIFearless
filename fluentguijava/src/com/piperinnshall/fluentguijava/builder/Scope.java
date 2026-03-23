package com.piperinnshall.fluentguijava.builder;

@FunctionalInterface
public interface Scope<B> {
  void run(B builder);
  static <B> Scope<B> nop() { return _ -> {}; }
}
