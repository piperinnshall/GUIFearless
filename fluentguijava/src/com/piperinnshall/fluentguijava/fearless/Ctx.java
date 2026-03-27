package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.Color;
import com.piperinnshall.fluentguijava.fearless.Types.Dimension;
import com.piperinnshall.fluentguijava.fearless.Types.KeyStroke;
import com.piperinnshall.fluentguijava.fearless.Types.Position;

public interface Ctx {
  long elapsed();
  Dimension screenSize();
  Dimension panelSize();

  interface Mouse extends Ctx {
    Position position();
  }

  interface Key extends Ctx {
    KeyStroke keyStroke();
  }

  interface Graphics extends Ctx {
    Graphics rect(Position position, Dimension dimension);
    Graphics oval(Position position, Dimension dimension);
    Graphics line(Position from, Position to);
    Graphics color(Color color);
  }
}
