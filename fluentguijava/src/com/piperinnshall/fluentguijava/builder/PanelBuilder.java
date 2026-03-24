package com.piperinnshall.fluentguijava.builder;

import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

import com.piperinnshall.fluentguijava.builder.PanelBuilder;

public interface PanelBuilder {
  PanelBuilder size(Vec2 dimension);
  PanelBuilder background(Vec3 rgb);
  PanelBuilder paintable(Scope<Ctx.Graphics> scope);
  PanelBuilder onKey(Scope<KeyBuilder> scope);
  PanelBuilder onMouse(Scope<MouseBuilder> scope);
  PanelBuilder flow(Scope<PanelBuilder.Flow> scope);
  PanelBuilder border(Scope<PanelBuilder.Border> scope);
  interface Flow extends PanelBuilder{}
  interface Border extends PanelBuilder{
    PanelBuilder.Border northFlow(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border southFlow(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border eastFlow(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border westFlow(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border centerFlow(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border northBorder(Scope<PanelBuilder.Border> scope);
    PanelBuilder.Border southBorder(Scope<PanelBuilder.Border> scope);
    PanelBuilder.Border eastBorder(Scope<PanelBuilder.Border> scope);
    PanelBuilder.Border westBorder(Scope<PanelBuilder.Border> scope);
    PanelBuilder.Border centerBorder(Scope<PanelBuilder.Border> scope);
  }
}

