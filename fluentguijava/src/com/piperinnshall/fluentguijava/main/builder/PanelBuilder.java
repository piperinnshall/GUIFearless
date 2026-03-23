package com.piperinnshall.fluentguijava.main.builder;

import com.piperinnshall.fluentguijava.main.Ctx;
import com.piperinnshall.fluentguijava.main.vec.Vec2;
import com.piperinnshall.fluentguijava.main.vec.Vec3;

import com.piperinnshall.fluentguijava.main.builder.PanelBuilder;

public interface PanelBuilder {
  PanelBuilder size(Vec2 dimension);
  PanelBuilder background(int hex);
  PanelBuilder background(Vec3 rgb);
  PanelBuilder paintable(Scope<Ctx.Graphics> scope);
  PanelBuilder onKey(Scope<KeyBuilder> scope);
  PanelBuilder onMouse(Scope<MouseBuilder> scope);
}

