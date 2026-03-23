package com.piperinnshall.fluentguijava.main.builder;

import com.piperinnshall.fluentguijava.main.Ctx;
import com.piperinnshall.fluentguijava.main.vec.Vec2;
import com.piperinnshall.fluentguijava.main.vec.Vec3;

public interface FrameBuilder extends PanelBuilder {
  FrameBuilder location(Vec2 location);
  FrameBuilder resizable();
  FrameBuilder undecorated();
  FrameBuilder maximized();
  FrameBuilder opacity(float opacity);
  FrameBuilder panel(Scope<PanelBuilder> scope);
  @Override FrameBuilder size(Vec2 dimension);
  @Override FrameBuilder background(int hex);
  @Override FrameBuilder background(Vec3 rgb);
  @Override FrameBuilder paintable(Scope<Ctx.Graphics> scope);
  @Override FrameBuilder onKey(Scope<KeyBuilder> scope);
  @Override FrameBuilder onMouse(Scope<MouseBuilder> scope);

  interface Border<R, N, S, E, W, C> extends FrameBuilder, PanelBuilder.Border<N, S, E, W, C> {}
}

