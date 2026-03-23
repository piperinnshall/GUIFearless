package com.piperinnshall.fluentguijava.builder;

import java.util.function.Consumer;

public interface KeyBuilder {
  KeyBuilder pressed(String key, Consumer<Ctx.Key> action);
  KeyBuilder released(String key, Consumer<Ctx.Key> action);
}

