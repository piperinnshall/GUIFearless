package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.FluentGUI;
import com.piperinnshall.fluentguijava.fearless.Types.*;

public class WindowDemo {
  public static void main() {
    new FluentGUI().run(frame -> frame
      .title("Window Demo")
      .fps(new FPS(60))
      .onKey(k->k.pressed(() -> new KeyStroke("A"), ctx->System.out.println(ctx.elapsed())))
      .border(b -> b
        .size(new Dimension(new Width(601), new Height(400)))
        .background(new Color(new Red(240), new Green(240), new Blue(240)))
        .north(top -> top
          .size(new Dimension(new Width(300), new Height(50)))
          .background(new Color(new Red(200), new Green(210), new Blue(255)))
          .flow(btn -> btn.size(new Dimension(new Width(80), new Height(30))).background(new Color(new Red(100), new Green(140), new Blue(255))))
          .flow(btn -> btn.size(new Dimension(new Width(80), new Height(30))).background(new Color(new Red(100), new Green(140), new Blue(255))))
          )
        .south(bottom -> bottom
          .size(new Dimension(new Width(600), new Height(50)))
          .background(new Color(new Red(220), new Green(220), new Blue(220)))
          .flow(btn -> btn.size(new Dimension(new Width(120), new Height(30))).background(new Color(new Red(160), new Green(160), new Blue(160))))
          )
        .west(sidebar -> sidebar
          .size(new Dimension(new Width(120), new Height(300)))
          .background(new Color(new Red(50), new Green(60), new Blue(80)))
          .flow(item -> item.size(new Dimension(new Width(100), new Height(40))).background(new Color(new Red(70), new Green(85), new Blue(110))))
          .flow(item -> item.size(new Dimension(new Width(100), new Height(40))).background(new Color(new Red(70), new Green(85), new Blue(110))))
          .flow(item -> item.size(new Dimension(new Width(100), new Height(40))).background(new Color(new Red(70), new Green(85), new Blue(110))))
          )
        .center(center -> center
          .border(cb -> cb
            .size(new Dimension(new Width(360), new Height(300)))
            .background(new Color(new Red(255), new Green(255), new Blue(255)))
            .north(toolbar -> toolbar
              .size(new Dimension(new Width(360), new Height(40)))
              .background(new Color(new Red(230), new Green(230), new Blue(230)))
              .flow(tool -> tool.size(new Dimension(new Width(50), new Height(30))).background(new Color(new Red(180), new Green(180), new Blue(180))))
              .flow(tool -> tool.size(new Dimension(new Width(50), new Height(30))).background(new Color(new Red(180), new Green(180), new Blue(180))))
              )
            .center(content -> content
              .size(new Dimension(new Width(360), new Height(220)))
              .background(new Color(new Red(245), new Green(245), new Blue(250)))
              .flow(card -> card.size(new Dimension(new Width(80), new Height(80))).background(new Color(new Red(100), new Green(180), new Blue(140))))
              .flow(card -> card.size(new Dimension(new Width(80), new Height(80))).background(new Color(new Red(100), new Green(180), new Blue(140))))
              .flow(card -> card.size(new Dimension(new Width(80), new Height(80))).background(new Color(new Red(100), new Green(180), new Blue(140))))
              )
            )
          )
        )
      );
    }
  }
