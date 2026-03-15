package com.piperinnshall.fluentguijava.fluentgraphics;

public class Main {
  public static void main(String[] args) {
    new GUI().run("Black", 120, frame -> frame
        .undecorated()
        .opacity(1f)
        .panel(panel -> panel
            .size(new Vec2(800, 600))
            .background(new Vec3(0, 0, 0))));
  }
}
