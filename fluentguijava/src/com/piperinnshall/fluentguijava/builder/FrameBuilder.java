package com.piperinnshall.fluentguijava.builder;

import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

public interface FrameBuilder {
  interface Flow extends PanelBuilder.Flow {
    FrameBuilder.Flow location(Vec2 location);
    FrameBuilder.Flow resizable();
    FrameBuilder.Flow undecorated();
    FrameBuilder.Flow maximized();
    FrameBuilder.Flow opacity(float opacity);
    @Override FrameBuilder.Flow size(Vec2 dimension);
    @Override FrameBuilder.Flow background(Vec3 rgb);
    @Override FrameBuilder.Flow paintable(Scope<Ctx.Graphics> scope);
    @Override FrameBuilder.Flow onKey(Scope<KeyBuilder> scope);
    @Override FrameBuilder.Flow onMouse(Scope<MouseBuilder> scope);
    @Override FrameBuilder.Flow flow(Scope<PanelBuilder.Flow> scope);
    @Override FrameBuilder.Flow border(Scope<PanelBuilder.Border> scope);
  }
  interface Border extends PanelBuilder.Border {
    FrameBuilder.Border location(Vec2 location);
    FrameBuilder.Border resizable();
    FrameBuilder.Border undecorated();
    FrameBuilder.Border maximized();
    FrameBuilder.Border opacity(float opacity);
    @Override FrameBuilder.Border size(Vec2 dimension);
    @Override FrameBuilder.Border background(Vec3 rgb);
    @Override FrameBuilder.Border paintable(Scope<Ctx.Graphics> scope);
    @Override FrameBuilder.Border onKey(Scope<KeyBuilder> scope);
    @Override FrameBuilder.Border onMouse(Scope<MouseBuilder> scope);
    @Override FrameBuilder.Border flow(Scope<PanelBuilder.Flow> scope);
    @Override FrameBuilder.Border border(Scope<PanelBuilder.Border> scope);
    @Override FrameBuilder.Border northFlow(Scope<PanelBuilder.Flow> scope);
    @Override FrameBuilder.Border southFlow(Scope<PanelBuilder.Flow> scope);
    @Override FrameBuilder.Border eastFlow(Scope<PanelBuilder.Flow> scope);
    @Override FrameBuilder.Border westFlow(Scope<PanelBuilder.Flow> scope);
    @Override FrameBuilder.Border centerFlow(Scope<PanelBuilder.Flow> scope);
    @Override FrameBuilder.Border northBorder(Scope<PanelBuilder.Border> scope);
    @Override FrameBuilder.Border southBorder(Scope<PanelBuilder.Border> scope);
    @Override FrameBuilder.Border eastBorder(Scope<PanelBuilder.Border> scope);
    @Override FrameBuilder.Border westBorder(Scope<PanelBuilder.Border> scope);
    @Override FrameBuilder.Border centerBorder(Scope<PanelBuilder.Border> scope);
  }
}
