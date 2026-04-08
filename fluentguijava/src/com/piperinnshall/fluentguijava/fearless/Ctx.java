package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.Color;
import com.piperinnshall.fluentguijava.fearless.Types.Dimension;
import com.piperinnshall.fluentguijava.fearless.Types.KeyStroke;
import com.piperinnshall.fluentguijava.fearless.Types.X;
import com.piperinnshall.fluentguijava.fearless.Types.Y;
import com.piperinnshall.fluentguijava.fearless.Types.TimeNanos;

public interface Ctx {
  TimeNanos elapsed();
  Dimension screenSize();
  Dimension panelSize();

  interface Mouse extends Ctx {
    X mouseX();
    Y mouseY();
    }
  interface Key extends Ctx {
    KeyStroke keyStroke();
    }

  interface Graphics extends Ctx {
    Graphics color(Color color);
    Graphics position(X x, Y y);
    Graphics line(X x, Y y);
    Graphics rect(Dimension dimension);
    Graphics oval(Dimension dimension);
    }
  }
