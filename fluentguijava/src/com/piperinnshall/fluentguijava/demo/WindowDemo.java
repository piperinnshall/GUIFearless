package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.main.*;
import com.piperinnshall.fluentguijava.main.vec.*;

public class WindowDemo {
  public static void main(String[] a) {
    new FluentGUI().run("Window Demo", 60, frame -> frame
        .maximized()
        .size(new Vec2(800, 600))
        .location(new Vec2(100, 100))
        .undecorated()
        .resizable()
        .opacity(0.95f)
        .background(new Vec3(30, 30, 40))
        .paintable(ctx -> {
          var t = ctx.elapsed() / 1_000_000_000f;
          var cx = ctx.panelSize().x() / 2;
          var cy = ctx.panelSize().y() / 2;

          ctx.color(new Vec3(80, 60, 120));
          ctx.rect(new Vec2(0, 0), ctx.panelSize());

          ctx.color(Vec3.fromHSV((t * 0.2f) % 1f, 0.8f, 1f));
          ctx.oval(new Vec2(cx - 60, cy - 60), new Vec2(120, 120));

          ctx.color(new Vec3(255, 255, 255));
          ctx.line(new Vec2(0, cy), new Vec2(ctx.panelSize().x(), cy));
          ctx.line(new Vec2(cx, 0), new Vec2(cx, ctx.panelSize().y()));
        })
        .onKey(k -> k
            .pressed("ESCAPE", _ -> System.exit(0))
            .pressed("F", ctx -> System.out.println("elapsed: " + ctx.elapsed()))
        )
        .onMouse(m -> m
            .clicked(ctx -> System.out.println("clicked at: " + ctx.pos()))
            .moved(ctx -> System.out.println("moved to: " + ctx.pos()))
        )
        .panel(panel -> panel
            .size(new Vec2(200, 100))
            .background(new Vec3(60, 40, 80))
            .paintable(ctx -> {
              ctx.color(Vec3.fromHSV(0.5f, 0.7f, 1f));
              ctx.rect(new Vec2(10, 10), new Vec2(180, 80));
            })
        )
    );
  }
}
