package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.FluentGUIResult;
import com.piperinnshall.fluentguijava.fearless.Types.Opacity;
import com.piperinnshall.fluentguijava.fearless.Types.Position;

public interface FrameBuilder {
  FluentGUIResult result();
  FrameBuilder location(Position location);
  FrameBuilder opacity(Opacity opacity);
  FrameBuilder flow(Scope<PanelBuilder.Flow> scope);
  FrameBuilder border(Scope<PanelBuilder.Border> scope);
  FrameBuilder onKey(Scope<KeyBuilder> scope);
  FrameBuilder onMouse(Scope<MouseBuilder> scope);
  }
