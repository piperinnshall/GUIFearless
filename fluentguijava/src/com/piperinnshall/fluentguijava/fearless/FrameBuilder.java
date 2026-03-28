package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.Opacity;
import com.piperinnshall.fluentguijava.fearless.Types.Position;

public interface FrameBuilder {
  FrameBuilder location(Position location);
  FrameBuilder undecorated(Opacity opacity);
  FrameBuilder maximized();
  FrameBuilder resizable();
  FrameBuilder flow(Scope<PanelBuilder.Flow> scope);
  FrameBuilder border(Scope<PanelBuilder.Border> scope);
  FrameBuilder onKey(Scope<KeyBuilder> scope);
}
