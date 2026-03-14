package com.piperinnshall.fluentguijava.fluentgraphics;
record LerpBallModel(Vec2 dimension, Vec2 radius, float duration) {}
record LerpBallView(LerpBallModel m, Slot<Vec2> origin, Slot<Vec2> destination, Slot<Long> animationStart) {
  LerpBallView {
    origin.fill(m.dimension().div(2));
    destination.fill(m.dimension().div(2));
    animationStart.fill(0L);
  }
  private Vec2 pos(Ctx ctx) {
    return Lerp.of(origin.get(), destination.get(), m.duration(), Easing.EASE_OUT_CUBIC).at(ctx.elapsed() - animationStart.get());
  }
  String build() {
    return new GUI().run("Animated Ball", 120, frame -> frame
        .undecorated()
        .opacity(0.8f)
        .panel(panel -> panel
            .size(m.dimension())
            .background(30, 30, 30)
            .paintable(ctx -> ctx
                .color(255, 100, 100)
                .oval(pos(ctx).sub(m.radius().div(2)), m.radius()))
            .onMouse(mouse -> mouse
                .pressed(ctx -> origin.fill(pos(ctx)))
                .pressed(ctx -> destination.fill(ctx.pos()))
                .pressed(ctx -> animationStart.fill(ctx.elapsed())))
            .onKey(key -> key
                .pressed("SPACE", ctx -> {
                  origin.fill(pos(ctx));
                  destination.fill(m.dimension().div(2));
                  animationStart.fill(ctx.elapsed());
                }))
            )
        .resolve("done"));
  }
}
public class LerpBallDemo {
  public static void main(String[] a) {
    var model = new LerpBallModel(new Vec2(1200, 800), new Vec2(80), 5f);
    System.out.println(new LerpBallView(model, Slot.of(), Slot.of(), Slot.of()).build());
  }
};
