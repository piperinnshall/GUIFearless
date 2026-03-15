package com.piperinnshall.fluentguijava.fluentgraphics;

record LerpBallModel(Vec2 dimension, Vec2 radius, float duration) {}

class LerpBallView {
  final LerpBallModel m;
  Vec2 origin, destination, radiusFrom;
  long moveStart, hoverStart;
  boolean hovered;

  LerpBallView(LerpBallModel m) {
    this.m = m;
    origin = destination = m.dimension().div(2);
    radiusFrom = m.radius();
    moveStart = hoverStart = 0L;
    hovered = false;
  }
  private Vec2 pos(Ctx ctx) {
    return Lerp.of(origin, destination, m.duration(), Easing.EASE_OUT_CUBIC)
        .at(ctx.elapsed() - moveStart);
  }
  private Vec2 radius(Ctx ctx) {
    var big = m.radius().mul(4f);
    var to = hovered ? big : m.radius();
    var distance = Math.abs(to.x() - radiusFrom.x());
    var speed = distance / (hovered ? 200f : 50f); // 200 / 50 units/sec
    return Lerp.of(radiusFrom, to, speed, Easing.EASE_OUT_CUBIC)
      .at(ctx.elapsed() - hoverStart);
  }
  private void updateHover(MouseCtx ctx) {
    var rad = radius(ctx);
    var d = ctx.pos().sub(pos(ctx));
    var r = rad.x() / 2f;
    var over = d.lenSq() < r * r;
    if (over != hovered) {
      radiusFrom = rad;
      hovered = over;
      hoverStart = ctx.elapsed();
    }
  }

  void build() {
    new GUI().run("Animated Ball", 120, frame -> frame
        .undecorated()
        .opacity(0.8f)
        .panel(panel -> panel
          .size(m.dimension())
          .background(30, 30, 30)
          .paintable(ctx -> ctx
            .color(255, 100, 100)
            .oval(pos(ctx).sub(radius(ctx).div(2)), radius(ctx)))
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
              destination = m.dimension().div(2);
              moveStart = ctx.elapsed();
            })))
    .resolve("done"));
  }
}

public class LerpBallDemo {
  public static void main(String[] a) {
    new LerpBallView(new LerpBallModel(new Vec2(1200, 800), new Vec2(80), 5f)).build();
  }
}
