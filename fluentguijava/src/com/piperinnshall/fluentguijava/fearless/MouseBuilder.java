package com.piperinnshall.fluentguijava.fearless;

import java.util.function.Consumer;

public interface MouseBuilder {
  MouseBuilder clicked(Consumer<Ctx.Mouse> action);
  MouseBuilder pressed(Consumer<Ctx.Mouse> action);
  MouseBuilder released(Consumer<Ctx.Mouse> action);
  MouseBuilder moved(Consumer<Ctx.Mouse> action);
  MouseBuilder dragged(Consumer<Ctx.Mouse> action);
  MouseBuilder entered(Consumer<Ctx.Mouse> action);
  MouseBuilder exited(Consumer<Ctx.Mouse> action);
  }
