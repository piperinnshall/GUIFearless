package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.Opacity;
import com.piperinnshall.fluentguijava.fearless.Types.Position;

public interface FrameBuilder {
  FrameBuilder location(Position location);
  FrameBuilder maximized(Opacity opacity);
  FrameBuilder resizable();
  FrameBuilder undecorated();
}
