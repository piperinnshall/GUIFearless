package com.piperinnshall.fluentguijava.main.builder;

import java.util.function.Consumer;

import com.piperinnshall.fluentguijava.main.Ctx;

public interface MouseBuilder {
  MouseBuilder clicked(Consumer<Ctx.Mouse> action);
  MouseBuilder pressed(Consumer<Ctx.Mouse> action);
  MouseBuilder released(Consumer<Ctx.Mouse> action);
  MouseBuilder moved(Consumer<Ctx.Mouse> action);
  MouseBuilder dragged(Consumer<Ctx.Mouse> action);
  MouseBuilder entered(Consumer<Ctx.Mouse> action);
  MouseBuilder exited(Consumer<Ctx.Mouse> action);
}

