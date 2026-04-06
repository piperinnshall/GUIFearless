package com.piperinnshall.fluentguijava.core;

import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.FrameBuilder;
import com.piperinnshall.fluentguijava.fearless.Types.FluentGUIResult;

public class FluentGUI {
  public FluentGUIResult run(String title, int fps, boolean maximized, boolean resizable, Scope<FrameBuilder> frame) {
    var fb = new CFrameBuilder();
    frame.run(fb);
    return run(title, fps, maximized, resizable, fb);
  }

  public FluentGUIResult run(String title, int fps, Scope<FrameBuilder> frame) {
    var fb = new CFrameBuilder();
    frame.run(fb);
    return run(title, fps, false, false, fb);
  }

  private FluentGUIResult run(String title, int fps, boolean maximized, boolean resizable, CFrameBuilder fb) {  
    var done = new CompletableFuture<RuntimeException>();
    SwingUtilities.invokeLater(() -> fb.start(title, fps, maximized, resizable, done));
    done.join();
    return fb.result(); 
  }
}
