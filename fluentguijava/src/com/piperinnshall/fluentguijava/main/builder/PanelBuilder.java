package com.piperinnshall.fluentguijava.main.builder;

import com.piperinnshall.fluentguijava.main.Scope;
import com.piperinnshall.fluentguijava.main.vec.Vec2;
import com.piperinnshall.fluentguijava.main.vec.Vec3;

public interface PanelBuilder {
  PanelBuilder size(Vec2 dimension);
  PanelBuilder background(int hex);
  PanelBuilder background(Vec3 rgb);
  PanelBuilder paintable(Scope.Graphics scope);
  PanelBuilder onKey(Scope.Key scope);
  PanelBuilder onMouse(Scope.Mouse scope);
}

