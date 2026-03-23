package com.piperinnshall.fluentguijava.builder;

import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

public interface FrameBuilder extends PanelBuilder {
  FrameBuilder location(Vec2 location);
  FrameBuilder resizable();
  FrameBuilder undecorated();
  FrameBuilder maximized();
  FrameBuilder opacity(float opacity);
  @Override FrameBuilder size(Vec2 dimension);
  @Override FrameBuilder background(Vec3 rgb);
  @Override FrameBuilder paintable(Scope<Ctx.Graphics> scope);
  @Override FrameBuilder onKey(Scope<KeyBuilder> scope);
  @Override FrameBuilder onMouse(Scope<MouseBuilder> scope);
  @Override FrameBuilder flow(Scope<PanelBuilder.Flow> scope);
  interface Flow extends FrameBuilder, PanelBuilder.Flow{}
}

