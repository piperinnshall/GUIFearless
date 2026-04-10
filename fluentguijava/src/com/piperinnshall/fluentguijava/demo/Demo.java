package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.FluentGUI;
import com.piperinnshall.fluentguijava.fearless.Slot;
import com.piperinnshall.fluentguijava.fearless.Swing;
import com.piperinnshall.fluentguijava.fearless.Types.*;
import com.piperinnshall.fluentguijava.fearless.Types.FluentGUIResult.*;

public class Demo {
  float ovalY         = 0.5f;
  KeyStroke upKey     = new KeyStroke("UP");
  KeyStroke downKey   = new KeyStroke("DOWN");
  Slot<Swing.Button> toggle = Slot.of();

  private void moveOval(float delta) { 
    ovalY = Math.max(0f, Math.min(1f, ovalY + delta)); 
    }

  private void toggleKeys() {
    upKey   = upKey.k().equals("UP")      ? new KeyStroke("W")  : new KeyStroke("UP");
    downKey = downKey.k().equals("DOWN")  ? new KeyStroke("S")  : new KeyStroke("DOWN");

    toggle.get().text(upKey.k().equals("UP") ? "Keys: Arrows" : "Keys: WASD");
    }

  private void run() {
    FluentGUIResult result = new FluentGUI().run(frame -> frame
      .title("Demo")
      .fps(new FPS(60))
      .maximized()
      .location(new X(10), new Y(10))
      .undecorated(new Opacity(0.8f))
      .resizable() // Does not work due to frame being undecorated.
      .onKey(k -> k
        .pressed(() -> upKey,   _ -> moveOval(-0.05f))
        .pressed(() -> downKey, _ -> moveOval( 0.05f))
        )
      .flow(panel -> panel
        .transparent()
        // Does not work due to panel being transparent
        .background(new Color(new Red(30), new Green(30), new Blue(30))) 
        .button("Crash", () -> { throw new RuntimeException("Crash"); }, Slot.of())
        .button("Keys: Arrows", this::toggleKeys, toggle)
        .label("Hello World!",  Slot.of())
        .size(new Dimension(new Width(800), new Height(800)))
        .paint(ctx -> {
          Width width = ctx.panelSize().w();
          Height height = ctx.panelSize().h();

          double size = Math.min(width.w(), height.h()) * 0.20;
          int radius = (int) (size * 0.5);

          int minY = radius;
          int maxY = height.h() - radius;

          int x = (int) (width.w() * 0.5 - size * 0.5);
          int y = (int) (minY + (maxY - minY) * ovalY - radius);

          ctx.color(new Color(new Red(100), new Green(200), new Blue(255)))
            .position(new X(x), new Y(y))
            .oval(new Dimension(new Width((int) size), new Height((int) size)));
          })
        )
      );

    System.out.println(switch(result) {
      case Unknown() -> "Unknown";
      case Closed() -> "Closed";
      case Crashed(var cause) -> cause;
      });
    }

  public static void main() {
    new Demo().run();
    }
  }
