package com.piperinnshall.fluentguijava.core;

import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.FrameBuilder;

public class FluentGUI {
  public void flow(String title, int fps, Scope<FrameBuilder.Flow> frame) {
    var fb = new CFrameBuilderFlow();
    frame.run(fb);
    run(title, fps, fb);
  }

  public void border(String title, int fps, Scope<FrameBuilder.Border> frame) {
    var fb = new CFrameBuilderBorder();
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
