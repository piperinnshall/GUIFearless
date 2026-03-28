package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.Color;
import com.piperinnshall.fluentguijava.fearless.Types.Dimension;
import com.piperinnshall.fluentguijava.fearless.Swing.Button;

public interface PanelBuilder {
  interface Panel<T> {
    T size(Dimension dimension);
    T background(Color color);
    T paint(Scope<Ctx.Graphics> scope);
    T flow(Scope<PanelBuilder.Flow> scope);
    T border(Scope<PanelBuilder.Border> scope);
    T onMouse(Scope<MouseBuilder> scope);

    T button(Types.Text text, Runnable r, Slot<Button> s);
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
