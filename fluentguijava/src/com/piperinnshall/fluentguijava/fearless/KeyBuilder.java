package com.piperinnshall.fluentguijava.fearless;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.piperinnshall.fluentguijava.fearless.Types.KeyStroke;

public interface KeyBuilder {
  KeyBuilder pressed(Supplier<KeyStroke> keyStroke, Consumer<Ctx.Key> action);
  KeyBuilder released(Supplier<KeyStroke> keyStroke, Consumer<Ctx.Key> action);
}

