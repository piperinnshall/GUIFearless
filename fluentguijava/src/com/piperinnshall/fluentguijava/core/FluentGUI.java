package com.piperinnshall.fluentguijava.core;

import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.FrameBuilder;

public class FluentGUI {
  public void run(String title, int fps, boolean maximized, boolean resizable, Scope<FrameBuilder> frame) {
    var fb = new CFrameBuilder();
    frame.run(fb);
    run(title, fps, maximized, resizable, fb);
  }

  public void run(String title, int fps, Scope<FrameBuilder> frame) {
    var fb = new CFrameBuilder();
    frame.run(fb);
    run(title, fps, false, false, fb);
  }

  private void run(String title, int fps, boolean maximized, boolean resizable, CFrameBuilder fb) {
    var done = new CompletableFuture<RuntimeException>();
    SwingUtilities.invokeLater(() -> fb.start(title, fps, maximized, resizable, done));
    var tr = done.join();
    if (tr != null) { throw tr; }
  }
}
