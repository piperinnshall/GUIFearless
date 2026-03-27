package com.piperinnshall.fluentguijava.core;

import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.FrameBuilder;

public class FluentGUI {
  public void run(String title, int fps, Scope<FrameBuilder> frame) {
    var fb = new CFrameBuilder();
    frame.run(fb);
    run(title, fps, fb);
  }

  private void run(String title, int fps, CFrameBuilder fb) {
    var done = new CompletableFuture<RuntimeException>();
    SwingUtilities.invokeLater(() -> fb.start(title, fps, done));
    var tr = done.join();
    if (tr != null) { throw tr; }
  }
}
