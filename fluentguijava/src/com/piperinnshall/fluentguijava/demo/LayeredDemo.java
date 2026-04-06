package com.piperinnshall.fluentguijava.demo;

import com.piperinnshall.fluentguijava.core.FluentGUI;
import com.piperinnshall.fluentguijava.fearless.PanelBuilder.Flow;
import com.piperinnshall.fluentguijava.fearless.Slot;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Types.*;
import java.util.function.Consumer;

public class LayeredDemo {

  static Scope<Flow> flow2 = f -> f
      .background(new Color(new Red(120), new Green(150), new Blue(30)))
      .size(new Dimension(new Width(250), new Height(250)))
      .button("Crash", () -> { throw new RuntimeException("Explode"); }, Slot.of());
  ;

  static Scope<Flow> flow1 = f -> f
      .background(new Color(new Red(30), new Green(30), new Blue(30)))
      .size(new Dimension(new Width(500), new Height(500)))
      .label("Layered Mouse Handling", Slot.of())
      .flow(flow2)
      .label("Layered Mouse Handling", Slot.of())
  ;

  public static void main() {
    var result = new FluentGUI().run("", 60, gui -> gui.flow(flow1));

    String out = switch(result) {
      case FluentGUIResult.Unknown() -> "Unknown";
      case FluentGUIResult.Closed() -> "Closed";
      case FluentGUIResult.Crashed(RuntimeException cause) -> "Crashed with error: " + cause.getLocalizedMessage();
    };
    System.out.println(out);
  }
}
