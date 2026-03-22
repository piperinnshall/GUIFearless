package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.main.*;
import com.piperinnshall.fluentguijava.main.lerp.*;
import com.piperinnshall.fluentguijava.main.vec.*;

record LerpBallModel(Vec2 radius, float duration) {}
class LerpBallView {
  final LerpBallModel m;
  Vec2 origin, destination, radiusFrom;
  Vec3 colorFrom;
  long moveStart, hoverStart;
  boolean hovered;
  LerpBallView(LerpBallModel m) {
    this.m = m;
    origin = destination = new Vec2(600, 400);
    radiusFrom = m.radius();
    colorFrom = new Vec3(255, 80, 120);
    moveStart = hoverStart = 0L;
    hovered = false;
  }
  private Vec3 rainbow(Ctx ctx) {
    var h = (ctx.elapsed() % 3_000_000_000L) / 3_000_000_000f;
    return Vec3.fromHSV(h, 1f, 1f);
  }
  private Vec2 pos(Ctx ctx) {
    return Lerp.of(origin, destination, m.duration(), Easing.EASE_OUT_CUBIC)
      .at(ctx.elapsed() - moveStart);
  }
  private Vec2 radius(Ctx ctx) {
    var big = m.radius().mul(4f);
    var to = hovered ? big : m.radius();
    var distance = Math.abs(to.x() - radiusFrom.x());
    var speed = distance / (hovered ? 600f : 150f);
    return Lerp.of(radiusFrom, to, speed, Easing.EASE_OUT_CUBIC)
      .at(ctx.elapsed() - hoverStart);
  }
  private Vec3 color(Ctx ctx) {
    var to = hovered ? new Vec3(80, 230, 180) : rainbow(ctx);
    return Lerp.of(colorFrom, to, 5f, Easing.EASE_OUT_CUBIC)
      .at(ctx.elapsed() - hoverStart);
  }
  private void updateHover(Ctx.Mouse ctx) {
    var rad = radius(ctx);
    var d = ctx.pos().sub(pos(ctx));
    var r = rad.x() / 2f;
    var over = d.lenSq() < r * r;
    if (over != hovered) {
      radiusFrom = rad;
      colorFrom = color(ctx);
      hovered = over;
      hoverStart = ctx.elapsed();
    }
  }
  void build() {
    new FluentGUI().run("Animated Ball", 120, frame -> frame
        .undecorated()
        .opacity(0.85f)
        .panel(panel -> panel
            .size(new Vec2(1200, 800))
            .background(new Vec3(20, 20, 30))
            .paintable(ctx -> {
              var t = ctx.elapsed() / 1_000_000_000f;
              // orbiting rings
              for (int i = 0; i < 5; i++) {
                var angle = t * (i + 1) * 0.5f + i * (float) (Math.PI * 2 / 5);
                var ox = (float) Math.cos(angle) * 120;
                var oy = (float) Math.sin(angle) * 120;
                var center = pos(ctx).add(new Vec2(ox, oy));
                var size = new Vec2(12);
                ctx.color(Vec3.fromHSV((t * 0.1f + i * 0.2f) % 1f, 1f, 1f))
                    .oval(center.sub(size.div(2)), size);
              }
              // main ball
              ctx.color(color(ctx))
                  .oval(pos(ctx).sub(radius(ctx).div(2)), radius(ctx));
            })
            .onMouse(mouse -> mouse
                .pressed(ctx -> {
                  origin = pos(ctx);
                  destination = ctx.pos();
                  moveStart = ctx.elapsed();
                })
                .moved(this::updateHover))
            .onKey(key -> key
                .pressed("SPACE", ctx -> {
                  origin = pos(ctx);
                  destination = ctx.panelSize().div(2);
                  moveStart = ctx.elapsed();
                })))
        .resolve("done"));
  }
}

public class LerpBallDemo {
  public static void main(String[] a) {
    new LerpBallView(new LerpBallModel(new Vec2(80), 7f)).build();
  }
}
