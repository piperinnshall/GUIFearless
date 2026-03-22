package com.piperinnshall.fluentguijava.main;

import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

public class FluentGUI {
  public <R> R run(String title, int fps, Scope.Frame<R> frame) {
    var done = new CompletableFuture<RuntimeException>();
    var fb = new CFrameBuilder<R>();
    frame.run(fb);
    SwingUtilities.invokeLater(() -> fb.start(title, fps, done));
    var tr = done.join();
    if (tr == null) {
      return fb.resolve();
    }
    throw tr;
  }
}

