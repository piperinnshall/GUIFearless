package com.piperinnshall.fluentguijava.fluentgraphics;

public class LissajousDemo {
  public static void main(String[] a) {
    record Curve(int fx, int fy, float phase) { }
    var curves = new Curve[] {
        new Curve(3, 2, 0f),
        new Curve(5, 4, 0.3f),
        new Curve(7, 6, 0.6f),
        new Curve(5, 3, 0.9f),
        new Curve(4, 3, 1.2f),
        new Curve(7, 5, 1.5f),
        new Curve(3, 4, 1.8f),
    };
    new GUI().run("Lissajous", 120, frame -> frame
        .undecorated()
        .maximized()
        .background(new Vec3(20, 13, 26))
        .paintable(ctx -> {
          var t = ctx.elapsed() / 1_000_000_000f;
          var cx = ctx.panelSize().x() / 2;
          var cy = ctx.panelSize().y() / 2;
          var r = 500f;
          var steps = 300;
          for (int i = 0; i < curves.length; i++) {
            var curve = curves[i];
            var hue = (t * 0.1f + i * (1f / curves.length)) % 1f;
            ctx.color(Vec3.fromHSV(hue, 0.9f, 1f));
            Vec2 prev = null;
            for (int s = 0; s <= steps; s++) {
              var angle = (s / (float) steps) * (float) (Math.PI * 2);
              var x = cx + (float) Math.cos(curve.fx() * angle + curve.phase() + t * 0.15f) * r;
              var y = cy + (float) Math.sin(curve.fy() * angle + t * 0.1f) * r;
              var curr = new Vec2(x, y);
              if (prev != null) {
                ctx.line(prev, curr);
              }
              prev = curr;
            }
          }
        })
    );
  }
}
