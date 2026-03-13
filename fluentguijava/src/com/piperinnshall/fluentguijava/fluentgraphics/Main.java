package com.piperinnshall.fluentguijava.fluentgraphics;

record BallModel(int width, int height, Vec2 dim) {}

record BallView(BallModel m, Slot<Vec2> pos) {
  BallView {
    pos.fill(new Vec2(m.width() / 2, m.height() / 2));
  }
  String build() {
    return new GraphicsCap().runGraphics("Ball", g -> g
        .size(m.width(), m.height())
        .background(30, 30, 30)
        .paintable((ctx, _) -> ctx
            .color(255, 100, 100)
            .oval(pos.get().sub(m.dim().x() / 2), m.dim()))
        .onMouse(ms -> ms.pressed(pos::fill))
        .resolve("GUI has finished!"));
  }
}

public class Main {
  public static void main(String[] a) {
    var model = new BallModel(800, 800, new Vec2(300));
    System.out.println(new BallView(model, Slot.of()).build());
  }
}
