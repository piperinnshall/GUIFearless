package com.piperinnshall.fluentguijava.core;

import com.piperinnshall.fluentguijava.fearless.FrameBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Types.FluentGUIResult;
import java.util.concurrent.CompletableFuture;
import javax.swing.SwingUtilities;

public class FluentGUI {
  public FluentGUIResult run(
      String title,
      int fps,
      boolean maximized,
      boolean resizable,
      boolean undecorated,
      Scope<FrameBuilder> frame
  ) {
    var fb = new CFrameBuilder();
    frame.run(fb);
    return runGUI(title, fps, maximized, resizable, undecorated, fb);
    }

  public FluentGUIResult run(String title, int fps, Scope<FrameBuilder> frame) {
    var fb = new CFrameBuilder();
    frame.run(fb);
    return runGUI(title, fps, false, false, false, fb);
    }

  private FluentGUIResult runGUI(
      String title,
      int fps,
      boolean maximized,
      boolean resizable,
      boolean undecorated,
      CFrameBuilder fb
  ) {
    var done = new CompletableFuture<RuntimeException>();
    SwingUtilities.invokeLater(() -> fb.start(title, fps, maximized, resizable, undecorated, done));
    done.join();
    return fb.result();
    }
  }
