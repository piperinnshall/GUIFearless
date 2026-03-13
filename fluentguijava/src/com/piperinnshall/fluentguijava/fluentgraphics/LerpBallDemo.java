package com.piperinnshall.fluentguijava.fluentgraphics;

record LerpBallModel(Vec2 dimension, Vec2 radius, float duration) {}

record LerpBallView(LerpBallModel m, Slot<Vec2> origin, Slot<Vec2> destination, Slot<Long> animationStart) {
  LerpBallView {
    origin.fill(m.dimension().div(2));
    destination.fill(m.dimension().div(2));
    animationStart.fill(0L);
  }
  private Vec2 currentPosition(long elapsed) {
    return Lerp.of(origin.get(), destination.get(), m.duration(), Easing.EASE_OUT_BOUNCE)
        .at(elapsed - animationStart.get());
  }
  private void reTarget(Vec2 click, long elapsed) {
    origin.fill(currentPosition(elapsed)); // Where is the ball when we click?
    destination.fill(click);
    animationStart.fill(elapsed);
  }
  String show() {
    return new GUI().run("Animated Ball", frame -> frame
        .resizable()
        .undecorated()
        .opacity(0.8f)
        .panel(panel -> panel
          .size(m.dimension())
          .fps(120)
          .background(30, 30, 30)
          .paintable((ctx, elapsed) -> ctx
            .color(255, 100, 100)
            .oval(currentPosition(elapsed).sub(m.radius().x() / 2), m.radius())
          )
          .onMouse(mouse -> mouse
            .pressed(this::reTarget)
          )
        )
        .resolve("done"));
  }
}

public class LerpBallDemo {
  public static void main(String[] a) {
    var model = new LerpBallModel(new Vec2(1200, 800), new Vec2(80), 0.6f);
    System.out.println(new LerpBallView(model, Slot.of(), Slot.of(), Slot.of()).show());
  }
};
