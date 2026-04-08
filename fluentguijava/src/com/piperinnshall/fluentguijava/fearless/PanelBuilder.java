package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Swing.Button;
import com.piperinnshall.fluentguijava.fearless.Swing.Label;
import com.piperinnshall.fluentguijava.fearless.Types.Color;
import com.piperinnshall.fluentguijava.fearless.Types.Dimension;

public interface PanelBuilder {
  interface Panel<T> {
    T transparent();
    T size(Dimension dimension);
    T background(Color color);
    T paint(Scope<Ctx.Graphics> scope);
    T flow(Scope<PanelBuilder.Flow> scope);
    T border(Scope<PanelBuilder.Border> scope);
    T onMouse(Scope<MouseBuilder> scope);

    T button(String text, Runnable r, Slot<Button> s);
    T label(String text, Slot<Label> s);
    }
  interface Flow extends Panel<PanelBuilder.Flow> {}
  interface Border extends Panel<PanelBuilder.Border> {
    PanelBuilder.Border north(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border south(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border east(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border west(Scope<PanelBuilder.Flow> scope);
    PanelBuilder.Border center(Scope<PanelBuilder.Flow> scope);
    }
  }
