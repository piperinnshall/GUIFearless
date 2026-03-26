package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.*;
import com.piperinnshall.fluentguijava.builder.*;
import com.piperinnshall.fluentguijava.vec.*;

public class ComponentDemo {

  static final Scope<PanelBuilder.Flow> BUTTON =
      b -> b.size(new Vec2(80, 30))
            .background(new Vec3(90, 90, 90));

  static final Scope<PanelBuilder.Flow> CARD =
      c -> c.size(new Vec2(80, 80))
            .background(new Vec3(100, 150, 200));

  static final Scope<PanelBuilder.Flow> TOOLBAR =
      t -> t.size(new Vec2(600, 50))
            .background(new Vec3(60, 60, 60))
            .flow(BUTTON)
            .flow(BUTTON);

  static final Scope<PanelBuilder.Flow> SIDEBAR =
      s -> s.size(new Vec2(120, 300))
            .background(new Vec3(50, 150, 50))
            .flow(CARD)
            .flow(CARD)
            .flow(CARD);

  static final Scope<PanelBuilder.Flow> CONTENT =
      c -> c.size(new Vec2(360, 220))
            .background(new Vec3(20, 20, 20))
            .flow(CARD)
            .flow(CARD)
            .flow(CARD);

  static final Scope<PanelBuilder.Flow> FOOTER =
      f -> f.size(new Vec2(600, 50))
            .background(new Vec3(150, 50, 50))
            .flow(BUTTON);

  static final Scope<PanelBuilder.Border> CENTER =
      c -> c.size(new Vec2(360, 300))
            .background(new Vec3(30, 30, 30))
            .northFlow(top -> top
                .size(new Vec2(360, 40))
                .background(new Vec3(60, 60, 60))
                .flow(BUTTON)
                .flow(BUTTON)
            )
            .centerFlow(CONTENT);

  public static void main(String[] args) {
    new FluentGUI().border("Component Demo", 60, f -> f
        .size(new Vec2(1500, 800))
        .northFlow(TOOLBAR)
        .westFlow(SIDEBAR)
        .centerBorder(CENTER)
        .southFlow(FOOTER)
    );
  }
}
