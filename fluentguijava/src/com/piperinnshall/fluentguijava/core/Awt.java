package com.piperinnshall.fluentguijava.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

interface Awt {
  static Dimension dimension(Vec2 v) { return new Dimension(Math.round(v.x()), Math.round(v.y())); }
  static Point point(Vec2 v) { return new Point(Math.round(v.x()), Math.round(v.y())); }
  static Color color(Vec3 v) { return new Color(Math.round(v.x()), Math.round(v.y()), Math.round(v.z())); }
}

