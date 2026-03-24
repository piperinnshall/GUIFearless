package com.piperinnshall.fluentguijava.builder;

import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

public interface PanelBuilder {
  interface Panel<T> {
    T size(Vec2 dimension);
    T background(Vec3 rgb);
    T paintable(Scope<Ctx.Graphics> scope);
    T onKey(Scope<KeyBuilder> scope);
    T onMouse(Scope<MouseBuilder> scope);
    T flow(Scope<PanelBuilder.Flow> scope);
    T border(Scope<PanelBuilder.Border> scope);
  }
  interface Flow extends Panel<PanelBuilder.Flow> {}
  interface Border extends Panel<PanelBuilder.Border> {
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
