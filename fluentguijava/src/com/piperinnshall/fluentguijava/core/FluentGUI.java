package com.piperinnshall.fluentguijava.core;

import com.piperinnshall.fluentguijava.fearless.FrameBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Types.FluentGUIResult;
import java.util.concurrent.CompletableFuture;
import javax.swing.SwingUtilities;

public class FluentGUI {
  public FluentGUIResult run(Scope<FrameBuilder> frame) {
    var fb = new CFrameBuilder();
    frame.run(fb);
    return runGUI(fb);
    }

  private FluentGUIResult runGUI(CFrameBuilder fb) {
    var done = new CompletableFuture<RuntimeException>();
    SwingUtilities.invokeLater(() -> fb.start(done));
    done.join();
    return fb.result();
    }
  }
