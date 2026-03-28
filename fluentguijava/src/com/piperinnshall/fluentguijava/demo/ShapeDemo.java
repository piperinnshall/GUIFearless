package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.FluentGUI;
import com.piperinnshall.fluentguijava.fearless.Slot;
import com.piperinnshall.fluentguijava.fearless.Swing;
import com.piperinnshall.fluentguijava.fearless.Types.*;

public class ShapeDemo {
  private Color rectColor = new Color(new Red(255), new Green(100), new Blue(100));
  private Color ovalColor = new Color(new Red(100), new Green(200), new Blue(255));
  private Color lineColor = new Color(new Red(255), new Green(255), new Blue(100));

  private float lineY = 0.75f;
  private KeyStroke upKey = new KeyStroke("UP");
  private KeyStroke downKey = new KeyStroke("DOWN");
  private final Slot<Swing.Button> toggleBtn = Slot.of();

  private void updateRectColor(Color c) { rectColor = c; }
  private void updateOvalColor(Color c) { ovalColor = c; }
  private void updateLineY(float delta) { lineY = Math.max(0f, Math.min(1f, lineY + delta)); }

  private void toggleKeys() {
    upKey = upKey.k().equals("UP") ? new KeyStroke("W") : new KeyStroke("UP");
    downKey = downKey.k().equals("DOWN") ? new KeyStroke("S") : new KeyStroke("DOWN");
    toggleBtn.get().text(new Text(upKey.k().equals("UP") ? "Keys: Arrows" : "Keys: WASD"));
  }

  private void run() {
    new FluentGUI().run("Shape Demo", 60, frame -> frame
        .undecorated(new Opacity(0.8f))
        .onKey(k -> k
            .pressed(() -> new KeyStroke("R"), _ -> updateRectColor(new Color(new Red(255), new Green(0), new Blue(0))))
            .pressed(() -> new KeyStroke("G"), _ -> updateRectColor(new Color(new Red(0), new Green(255), new Blue(0))))
            .pressed(() -> upKey, _ -> updateLineY(-0.05f))
            .pressed(() -> downKey, _ -> updateLineY(0.05f)))
        .flow(panel -> panel
            .button(new Text("Keys: Arrows"), this::toggleKeys, toggleBtn)
            .size(new Dimension(new Width(800), new Height(800)))
            .background(new Color(new Red(30), new Green(30), new Blue(30)))
            .onMouse(m -> m
                .clicked(_ -> updateOvalColor(
                    new Color(
                        new Red((int) (Math.random() * 255)),
                        new Green((int) (Math.random() * 255)),
                        new Blue((int) (Math.random() * 255))))))
            .paint(ctx -> {
              var s = ctx.panelSize().toVector2();
              var w = s.x();
              var h = s.y();
              ctx.color(rectColor)
                  .rect(new Position(s.mul(new Scalar(0.10))), new Dimension(s.mul(new Scalar(0.25))))
                  .color(ovalColor)
                  .oval(new Position(s.mul(new Scalar(0.40))), new Dimension(s.mul(new Scalar(0.20))))
                  .color(lineColor)
                  .line(
                      new Position(w.mul(new Scalar(0.10)), h.mul(new Scalar(lineY))),
                      new Position(w.mul(new Scalar(0.90)), h.mul(new Scalar(lineY))));
            })));
  }
  public static void main(String[] args) {
    new ShapeDemo().run();
  }
}
