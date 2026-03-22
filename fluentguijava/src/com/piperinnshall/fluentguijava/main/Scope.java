package com.piperinnshall.fluentguijava.main;

import com.piperinnshall.fluentguijava.main.builder.FrameBuilder;
import com.piperinnshall.fluentguijava.main.builder.KeyBuilder;
import com.piperinnshall.fluentguijava.main.builder.MouseBuilder;
import com.piperinnshall.fluentguijava.main.builder.PanelBuilder;

public interface Scope {
  @FunctionalInterface interface Frame<R> { void run(FrameBuilder<R> f); }
  @FunctionalInterface interface Panel{ void run(PanelBuilder p); }
  @FunctionalInterface interface Key{ void run(KeyBuilder k); static Key nop() { return _ -> { }; } }
  @FunctionalInterface interface Mouse{ void run(MouseBuilder m); static Mouse nop() { return _ -> { }; } }
  @FunctionalInterface interface Graphics{ void run(Ctx.Graphics g); static Graphics nop() { return _ -> { }; } }
}
