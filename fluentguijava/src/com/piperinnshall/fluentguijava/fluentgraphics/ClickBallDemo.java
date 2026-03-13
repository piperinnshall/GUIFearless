package com.piperinnshall.fluentguijava.fluentgraphics;

record ClickBallModel(int width, int height, Vec2 dim) {}

record ClickBallView(ClickBallModel m, Slot<Vec2> pos) {
  ClickBallView { pos.fill(new Vec2(m.width() / 2, m.height() / 2)); }
  String build() {
    return new GUI().run("Ball", frame -> frame
        // .size(new Vec2(1000, 900))
        // .location(new Vec2(10))
        .resizable()
        .undecorated()
        .opacity(0.8f)
        .maximized()
        .panel(p -> p
            // .size(new Vec2(800))
            .background(30, 30, 30)
            .paintable((ctx, _) -> ctx
                .color(255, 100, 100)
                .oval(pos.get().sub(m.dim().x() / 2), m.dim()))
            .onMouse(ms -> ms.pressed((click, _) -> pos.fill(click))))
        .resolve("GUI has finished!"));
  }
}

public class ClickBallDemo {
  public static void main(String[] a) {
    var model = new ClickBallModel(800, 800, new Vec2(200));
    System.out.println(new ClickBallView(model, Slot.of()).build());
  }
}
