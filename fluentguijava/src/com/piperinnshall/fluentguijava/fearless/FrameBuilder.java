package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.X;
import com.piperinnshall.fluentguijava.fearless.Types.Y;
import com.piperinnshall.fluentguijava.fearless.Types.FluentGUIResult;
import com.piperinnshall.fluentguijava.fearless.Types.FPS;
import com.piperinnshall.fluentguijava.fearless.Types.Opacity;

public interface FrameBuilder {
  FrameBuilder maximized();
  FrameBuilder resizable();
  FrameBuilder undecorated(Opacity opacity);
  FrameBuilder title(String title);
  FrameBuilder fps(FPS fps);
  FrameBuilder location(X x, Y y);
  FrameBuilder flow(Scope<PanelBuilder.Flow> scope);
  FrameBuilder border(Scope<PanelBuilder.Border> scope);
  FrameBuilder onKey(Scope<KeyBuilder> scope);
  }
