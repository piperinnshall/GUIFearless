package com.piperinnshall.fluentguijava.main.builder;

import com.piperinnshall.fluentguijava.main.Ctx;
import com.piperinnshall.fluentguijava.main.vec.Vec2;
import com.piperinnshall.fluentguijava.main.vec.Vec3;

import com.piperinnshall.fluentguijava.main.builder.PanelBuilder;

public interface PanelBuilder {
  PanelBuilder panel(Scope<PanelBuilder> scope);
  PanelBuilder size(Vec2 dimension);
  PanelBuilder background(int hex);
  PanelBuilder background(Vec3 rgb);
  PanelBuilder paintable(Scope<Ctx.Graphics> scope);
  PanelBuilder onKey(Scope<KeyBuilder> scope);
  PanelBuilder onMouse(Scope<MouseBuilder> scope);

  interface Border<N, S, E, W, C> extends PanelBuilder {
    Border<Present, S, E, W, C> north(Scope<PanelBuilder> scope);
    Border<N, Present, E, W, C> south(Scope<PanelBuilder> scope);
    Border<N, S, Present, W, C> east(Scope<PanelBuilder> scope);
    Border<N, S, E, Present, C> west(Scope<PanelBuilder> scope);
    Border<N, S, E, W, Present> center(Scope<PanelBuilder> scope);
  }
}

