package com.piperinnshall.fluentguijava.main.builder;

@FunctionalInterface
public interface Scope<B> {
  void run(B builder);
  static <B> Scope<B> nop() { return _ -> {}; }
}
