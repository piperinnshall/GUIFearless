package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.FluentGUI;
import com.piperinnshall.fluentguijava.fearless.Slot;
import com.piperinnshall.fluentguijava.fearless.Swing;
import com.piperinnshall.fluentguijava.fearless.Types.*;

public class ShapeDemo {
  private Color rectColor = new Color(new Red(255), new Green(100), new Blue(100));
  private Color ovalColor = new Color(new Red(100), new Green(200), new Blue(255));
  private int lineY = 300;
  private KeyStroke upKey = new KeyStroke("UP");
  private KeyStroke downKey = new KeyStroke("DOWN");
  private final Slot<Swing.Button> toggleBtn = Slot.of();
  private void updateRectColor(Color c) { rectColor = c; }
  private void updateOvalColor(Color c) { ovalColor = c; }
  private void updateLineY(int delta) { lineY += delta; }
  private void toggleKeys() {
    upKey = upKey.k().equals("UP") ? new KeyStroke("W") : new KeyStroke("UP");
    downKey = downKey.k().equals("DOWN") ? new KeyStroke("S") : new KeyStroke("DOWN");
    toggleBtn.get().text(new Text(upKey.k().equals("UP") ? "Keys: Arrows" : "Keys: WASD"));
  }
  public static void main() { new ShapeDemo().run(); }
  private void run() {
    new FluentGUI().run("Shape Demo", 60, frame -> frame
        .undecorated(new Opacity(0.8f))
        .onKey(k -> k
          .pressed(() -> new KeyStroke("R"), _ -> updateRectColor(new Color(new Red(255), new Green(0), new Blue(0))))
          .pressed(() -> new KeyStroke("G"), _ -> updateRectColor(new Color(new Red(0), new Green(255), new Blue(0))))
          .pressed(() -> upKey, _ -> updateLineY(-10))
          .pressed(() -> downKey, _ -> updateLineY(10))
        )
        .flow(panel -> panel
          .size(new Dimension(new Width(400), new Height(400)))
          .background(new Color(new Red(30), new Green(30), new Blue(30)))
          .onMouse(m -> m
            .clicked(_ -> updateOvalColor(new Color(
                  new Red((int)(Math.random() * 255)),
                  new Green((int)(Math.random() * 255)),
                  new Blue((int)(Math.random() * 255))))
            )
          )
          .paint(ctx -> ctx
            .color(rectColor)
            .rect(new Position(new X(50), new Y(50)), new Dimension(new Width(100), new Height(100)))
            .color(ovalColor)
            .oval(new Position(new X(200), new Y(150)), new Dimension(new Width(80), new Height(80)))
            .color(new Color(new Red(255), new Green(255), new Blue(100)))
            .line(new Position(new X(50), new Y(lineY)), new Position(new X(350), new Y(lineY)))
          )
          .button(new Text("Keys: Arrows"), this::toggleKeys, toggleBtn)
        )
    );
  }
}
