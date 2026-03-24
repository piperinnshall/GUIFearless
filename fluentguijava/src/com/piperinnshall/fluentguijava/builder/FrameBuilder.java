package com.piperinnshall.fluentguijava.builder;

import com.piperinnshall.fluentguijava.vec.Vec2;

public interface FrameBuilder {
  interface Frame<T> {
    T location(Vec2 location);
    T resizable();
    T undecorated();
    T maximized();
    T opacity(float opacity);
  }
  interface Flow extends PanelBuilder.Panel<FrameBuilder.Flow>, Frame<FrameBuilder.Flow> {}
  interface Border extends PanelBuilder.Panel<FrameBuilder.Border>, Frame<FrameBuilder.Border> {
    FrameBuilder.Border northFlow(Scope<PanelBuilder.Flow> scope);
    FrameBuilder.Border southFlow(Scope<PanelBuilder.Flow> scope);
    FrameBuilder.Border eastFlow(Scope<PanelBuilder.Flow> scope);
    FrameBuilder.Border westFlow(Scope<PanelBuilder.Flow> scope);
    FrameBuilder.Border centerFlow(Scope<PanelBuilder.Flow> scope);
    FrameBuilder.Border northBorder(Scope<PanelBuilder.Border> scope);
    FrameBuilder.Border southBorder(Scope<PanelBuilder.Border> scope);
    FrameBuilder.Border eastBorder(Scope<PanelBuilder.Border> scope);
    FrameBuilder.Border westBorder(Scope<PanelBuilder.Border> scope);
    FrameBuilder.Border centerBorder(Scope<PanelBuilder.Border> scope);
  }
}
