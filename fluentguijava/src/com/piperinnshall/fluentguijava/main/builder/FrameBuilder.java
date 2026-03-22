package com.piperinnshall.fluentguijava.main.builder;

import com.piperinnshall.fluentguijava.main.Scope;
import com.piperinnshall.fluentguijava.main.vec.Vec2;
import com.piperinnshall.fluentguijava.main.vec.Vec3;

public interface FrameBuilder<R> extends PanelBuilder {
  FrameBuilder<R> resolve(R r);
  FrameBuilder<R> size(Vec2 dimension);
  FrameBuilder<R> location(Vec2 location);
  FrameBuilder<R> resizable();
  FrameBuilder<R> undecorated();
  FrameBuilder<R> maximized();
  FrameBuilder<R> opacity(float opacity);
  FrameBuilder<R> panel(Scope.Panel scope);
  @Override FrameBuilder<R> background(int hex);
  @Override FrameBuilder<R> background(Vec3 rgb);
  @Override FrameBuilder<R> paintable(Scope.Graphics scope);
  @Override FrameBuilder<R> onKey(Scope.Key scope);
  @Override FrameBuilder<R> onMouse(Scope.Mouse scope);
}

