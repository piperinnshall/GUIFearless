package com.piperinnshall.fluentguijava.main;

import com.piperinnshall.fluentguijava.main.vec.Vec2;
import com.piperinnshall.fluentguijava.main.vec.Vec3;

public interface Ctx {
  long elapsed();
  Vec2 screenSize();
  Vec2 panelSize();
  interface Mouse extends Ctx {
    Vec2 pos();
  }
  interface Key extends Ctx {
    String key();
  }
  interface Graphics extends Ctx {
    Graphics rect(Vec2 position, Vec2 dimension);
    Graphics oval(Vec2 position, Vec2 dimension);
    Graphics line(Vec2 from, Vec2 to);
    Graphics color(int hex);
    Graphics color(Vec3 rgb);
  }
}
