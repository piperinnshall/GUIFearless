package com.piperinnshall.fluentguijava.fluentgraphics;

record Model(int width, int height, Vec2 dim) {}

record View(Model m, Slot<Vec2> pos) {
  View { pos.fill(new Vec2(m.width() / 2, m.height() / 2)); }
  String build() {
    return new GUI().run("Ball", frame -> frame
        // .size(new Vec2(1000, 900))
        // .location(new Vec2(10))
        .resizable()
        .opacity(0.8f)
        .undecorated()
        .maximized()
        .panel(p -> p
            // .size(new Vec2(800))
            .background(30, 30, 30)
            .paintable((ctx, _) -> ctx
                .color(255, 100, 100)
                .oval(pos.get().sub(m.dim().x() / 2), m.dim()))
            .onMouse(ms -> ms.pressed(pos::fill)))
        .resolve("GUI has finished!"));
  }
}

public class ClickBallDemo {
  public static void main(String[] a) {
    var model = new Model(800, 800, new Vec2(200));
    System.out.println(new View(model, Slot.of()).build());
  }
}
