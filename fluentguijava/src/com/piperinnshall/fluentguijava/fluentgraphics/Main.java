package com.piperinnshall.fluentguijava.fluentgraphics;

class Main {
  public static void main(String[] args) {
    var bounce = new Lerp(new Vec2(150, 50), new Vec2(150, 300), 1f, Easing.EASE_IN_OUT_BOUNCE);

    new GraphicsCap().runGraphics("Sphere", g -> g
        .size(400, 400)
        .paintable((ctx, nanos) -> {
          var pos = bounce.at(nanos);
          ctx.oval(pos, new Vec2(100, 100));
        }));
  }
}
