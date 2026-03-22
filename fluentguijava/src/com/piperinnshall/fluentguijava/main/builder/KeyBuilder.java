package com.piperinnshall.fluentguijava.main.builder;

import java.util.function.Consumer;

import com.piperinnshall.fluentguijava.main.Ctx;

public interface KeyBuilder {
  KeyBuilder pressed(String key, Consumer<Ctx.Key> action);
  KeyBuilder released(String key, Consumer<Ctx.Key> action);
}

