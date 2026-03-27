package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.FluentGUI;
import com.piperinnshall.fluentguijava.fearless.Types.*;

public class WindowDemo {
  public static void main(String[] a) {
    new FluentGUI().border("Window Demo", 60, frame -> frame
        .size(new Vec2(600, 400))
        .resizable()
        .onMouse(mouse -> mouse 
          .clicked(_ -> {})
        )
        .northFlow(top -> top
            .size(new Vec2(600, 50))
            .background(new Vec3(50, 50, 150))
            .flow(btn -> btn.size(new Vec2(80, 30)).background(new Vec3(100, 100, 200)))
            .flow(btn -> btn.size(new Vec2(80, 30)).background(new Vec3(100, 100, 200)))
        )
        .southFlow(bottom -> bottom
            .size(new Vec2(600, 50))
            .background(new Vec3(150, 50, 50))
            .flow(btn -> btn.size(new Vec2(120, 30)).background(new Vec3(200, 100, 100)))
        )
        .westFlow(sidebar -> sidebar
            .size(new Vec2(120, 300))
            .background(new Vec3(50, 150, 50))
            .flow(item -> item.size(new Vec2(100, 40)).background(new Vec3(100, 200, 100)))
            .flow(item -> item.size(new Vec2(100, 40)).background(new Vec3(80, 180, 80)))
            .flow(item -> item.size(new Vec2(100, 40)).background(new Vec3(60, 160, 60)))
        )
        .centerBorder(center -> center
            .size(new Vec2(360, 300))
            .background(new Vec3(30, 30, 30))
            .northFlow(toolbar -> toolbar
                .size(new Vec2(360, 40))
                .background(new Vec3(60, 60, 60))
                .flow(tool -> tool.size(new Vec2(50, 30)).background(new Vec3(90, 90, 90)))
                .flow(tool -> tool.size(new Vec2(50, 30)).background(new Vec3(90, 90, 90)))
            )
            .centerFlow(content -> content
                .size(new Vec2(360, 220))
                .background(new Vec3(20, 20, 20))
                .flow(card -> card.size(new Vec2(80, 80)).background(new Vec3(200, 150, 50)))
                .flow(card -> card.size(new Vec2(80, 80)).background(new Vec3(50, 150, 200)))
                .flow(card -> card.size(new Vec2(80, 80)).background(new Vec3(150, 50, 200)))
            )
        )
    );
  }
}
