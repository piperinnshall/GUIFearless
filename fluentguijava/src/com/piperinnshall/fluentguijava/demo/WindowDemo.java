package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.*;
import com.piperinnshall.fluentguijava.vec.*;

public class WindowDemo {
  public static void main(String[] a) {
    new FluentGUI().flow("Nesting Demo", 60, frame -> frame
        // .maximized()
        .size(new Vec2(600, 300))
        .undecorated()
        .opacity(0.5f)
        .background(new Vec3(30, 30, 40))
        .flow(p -> p
            .size(new Vec2(200, 200))
            .background(new Vec3(255, 0, 0))
            .flow(inner -> inner.background(new Vec3(0, 255, 0)))
        )
        .flow(p -> p
            .size(new Vec2(200))
            .background(new Vec3(255, 0, 0))
            .flow(inner -> inner.size(new Vec2(80)))
            .flow(inner -> inner.background(new Vec3(0, 255, 0)))
        )
    );
  }
}
