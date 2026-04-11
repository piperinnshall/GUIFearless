package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.FluentGUI;
import com.piperinnshall.fluentguijava.fearless.*;
import com.piperinnshall.fluentguijava.fearless.Types.*;

public class EmptyDemo {
  void run() {
  Slot<Swing.Label> label = Slot.of();
    new FluentGUI().run(frame -> frame
      .flow(panel -> panel
        .size(new Dimension(new Width(200), new Height(100)))
        .label("Hello", label)
        .paint(_-> {
          label.get().opaque(true);
          label.get().background(new Color(new Red(1), new Green(100), new Blue(1)));
          })
        )
      );
    }
  public static void main() {
    new EmptyDemo().run();
  }
}
