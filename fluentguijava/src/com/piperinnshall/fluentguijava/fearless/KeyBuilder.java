package com.piperinnshall.fluentguijava.fearless;

import java.util.function.Consumer;

import com.piperinnshall.fluentguijava.fearless.Types.KeyStroke;

public interface KeyBuilder {
  KeyBuilder pressed(KeyStroke key, Consumer<Ctx.Key> action);
  KeyBuilder released(KeyStroke key, Consumer<Ctx.Key> action);
}

