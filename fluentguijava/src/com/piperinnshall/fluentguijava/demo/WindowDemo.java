package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.*;
import com.piperinnshall.fluentguijava.vec.*;

public class WindowDemo {
  public static void main(String[] a) {
    new FluentGUI().flow("Window Demo", 60, frame -> frame
        .size(new Vec2(600, 300))
        .flow(panel -> panel
            .size(new Vec2(200))
            .background(new Vec3(255, 0, 0))
            .flow(_ -> {})
        )
        .flow(panel -> panel
            .size(new Vec2(200))
            .background(new Vec3(255, 0, 0))
            .flow(panel1 -> panel1.size(new Vec2(80)))
            .flow(_ -> {})
        )
    );
  }
}
