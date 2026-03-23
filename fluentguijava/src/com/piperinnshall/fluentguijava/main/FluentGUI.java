package com.piperinnshall.fluentguijava.main;

import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import com.piperinnshall.fluentguijava.main.builder.FrameBuilder;
import com.piperinnshall.fluentguijava.main.builder.Scope;

public class FluentGUI {
  public void run(String title, int fps, Scope<FrameBuilder> frame) {
    var done = new CompletableFuture<RuntimeException>();
    var fb = new CFrameBuilder();
    frame.run(fb);
    SwingUtilities.invokeLater(() -> fb.start(title, fps, done));
    var tr = done.join();
    if (tr == null) { return; }
    throw tr;
  }
}

