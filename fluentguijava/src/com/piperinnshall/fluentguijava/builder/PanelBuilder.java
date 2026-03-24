package com.piperinnshall.fluentguijava.builder;

import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

public interface PanelBuilder {
  interface Flow {
    PanelBuilder.Flow size(Vec2 dimension);
    PanelBuilder.Flow background(Vec3 rgb);
    PanelBuilder.Flow paintable(Scope<Ctx.Graphics> scope);
    PanelBuilder.Flow onKey(Scope<KeyBuilder> scope);
    PanelBuilder.Flow onMouse(Scope<MouseBuilder> scope);
    PanelBuilder.Flow flow(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Flow border(Scope<PanelBuilder.Border> scope);
  }
  interface Border {
    PanelBuilder.Border size(Vec2 dimension);
    PanelBuilder.Border background(Vec3 rgb);
    PanelBuilder.Border paintable(Scope<Ctx.Graphics> scope);
    PanelBuilder.Border onKey(Scope<KeyBuilder> scope);
    PanelBuilder.Border onMouse(Scope<MouseBuilder> scope);
    PanelBuilder.Border flow(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border border(Scope<PanelBuilder.Border> scope);
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
