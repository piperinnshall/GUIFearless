package com.piperinnshall.fluentguijava.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import com.piperinnshall.fluentguijava.fearless.Types;

interface Awt {
  static Dimension dimension(Types.Dimension d) {
    return new Dimension(Math.round(d.w().w()), Math.round(d.h().h()));
  }
  static Point point(Types.Position p) {
    return new Point(p.x().x(), p.y().y());
  }
  static Color color(Types.Color c) {
    return new Color(c.r().r(), c.g().g(), c.b().b(), c.a().a());
  }
}

