package com.piperinnshall.fluentguijava.fearless;

import java.util.Optional;

public class Slot<T> {
  private Optional<T> inner = Optional.empty();

  public static <T> Slot<T> of() {
    return new Slot<>();
    }
  public void fill(T t) {
    inner = Optional.of(t);
    }
  public T get() {
    return inner.get();
    }
  }
