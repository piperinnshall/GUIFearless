package com.piperinnshall.fluentguijava.builder;

import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

import com.piperinnshall.fluentguijava.builder.PanelBuilder;

public interface PanelBuilder {
  PanelBuilder size(Vec2 dimension);
  PanelBuilder background(int hex);
  PanelBuilder background(Vec3 rgb);
  PanelBuilder paintable(Scope<Ctx.Graphics> scope);
  PanelBuilder onKey(Scope<KeyBuilder> scope);
  PanelBuilder onMouse(Scope<MouseBuilder> scope);
  PanelBuilder flow(Scope<PanelBuilder.Flow> scope);
  interface Flow extends PanelBuilder {}
}

