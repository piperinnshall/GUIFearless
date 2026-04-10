package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.FluentGUI;
import com.piperinnshall.fluentguijava.fearless.*;
import com.piperinnshall.fluentguijava.fearless.Types.*;

public class EmptyDemo {
  public static void main() {
    // new FluentGUI().run(_ -> {});
    Slot<Swing.Button> btn = Slot.of();
    var toggled = new boolean[]{false};

    new FluentGUI().run(frame -> frame
      .flow(panel -> panel
        .button("Off", () -> {
            toggled[0] = !toggled[0];
            btn.get().text(toggled[0] ? "On" : "Off");
        }, btn)
      )
    );
    }
  }
