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
    toggleBtn.get().text(upKey.k().equals("UP") ? "Keys: Arrows" : "Keys: WASD");
  }

  private void run() {
    var result = new FluentGUI().run("Shape Demo", 60, false, true, frame -> frame
        // .undecorated(new Opacity(1))
        .onKey(k -> k
            .pressed(() -> new KeyStroke("R"), _ -> updateRectColor(new Color(new Red(255), new Green(0), new Blue(0))))
            .pressed(() -> new KeyStroke("G"), _ -> updateRectColor(new Color(new Red(0), new Green(255), new Blue(0))))
            .pressed(() -> upKey, _ -> updateLineY(-0.05f))
            .pressed(() -> downKey, _ -> updateLineY(0.05f)))
        .flow(panel -> panel
            .button("Crash", () -> { throw new RuntimeException("Explode"); }, Slot.of())
            .button("Keys: Arrows", this::toggleKeys, toggleBtn)
            .label("Hello World!", Slot.of())
            .label("Foo Bar Baz", Slot.of())
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
              ctx.color(rectColor)
                  .rect(new Position(s.mul(0.10)), new Dimension(s.mul(0.25)))
                  .color(ovalColor)
                  .oval(new Position(s.mul(0.40)), new Dimension(s.mul(0.20)))
                  .color(lineColor)
                  .line(
                      new Position(new X((int) (s.x() * 0.10)), new Y((int) (s.y() * lineY))),
                      new Position(new X((int) (s.x() * 0.90)), new Y((int) (s.y() * lineY))));
            })));

    String out = switch(result) {
      case FluentGUIResult.Unknown() -> "Unknown";
      case FluentGUIResult.Closed() -> "Closed";
      case FluentGUIResult.Crashed(RuntimeException cause) -> "Crashed: " + cause.getLocalizedMessage();
    };

    System.out.println("Output of the GUI is: " + out);
  }
  public static void main(String[] args) {
    new ShapeDemo().run();
  }
}
