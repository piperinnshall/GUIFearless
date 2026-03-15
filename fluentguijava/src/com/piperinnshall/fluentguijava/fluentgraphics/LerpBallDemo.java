package com.piperinnshall.fluentguijava.fluentgraphics;

record LerpBallModel(Vec2 radius, float duration) {}
class LerpBallView {
  final LerpBallModel m;
  Vec2 origin, destination, radiusFrom;
  Vec3 colorFrom;
  long moveStart, hoverStart;
  boolean hovered;
  static final Vec3 COLOR_HOVERED = new Vec3(80, 230, 180);
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
    var to = hovered ? COLOR_HOVERED : rainbow(ctx);
    return Lerp.of(colorFrom, to, 5f, Easing.EASE_OUT_CUBIC)
      .at(ctx.elapsed() - hoverStart);
  }
  private void updateHover(MouseCtx ctx) {
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
    new GUI().run("Animated Ball", 120, frame -> frame
        .undecorated()
        .opacity(0.85f)
        .panel(panel -> panel
          .size(new Vec2(1200, 800))
          .background(new Vec3(20, 20, 30))
          .paintable(ctx -> ctx
            .color(color(ctx))
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
