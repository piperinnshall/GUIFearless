package com.piperinnshall.fluentguijava.core;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.swing.Timer;

import com.piperinnshall.fluentguijava.fearless.FrameBuilder;
import com.piperinnshall.fluentguijava.fearless.KeyBuilder;
import com.piperinnshall.fluentguijava.fearless.PanelBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Types;

class CFrameBuilder implements FrameBuilder {
  Types.Position location = null;
  boolean undecorated = false;
  Types.Opacity opacity = new Types.Opacity(1);
  Scope<KeyBuilder> keyScope = Scope.nop();
  private final Types.TimeNanos startTime = new Types.TimeNanos(System.nanoTime());
  private final List<APanelBuilder<?>> children = new ArrayList<>();

  public void start(String title, int fps, boolean maximized, boolean resizable, CompletableFuture<RuntimeException> done) {
    if (fps <= 0) throw new IllegalArgumentException("fps must be > 0, got: " + fps);
    Types.Dimension screenSize = resolveScreenSize();
    var o = new Object() {
      Consumer<RuntimeException> c = e -> {
        this.f.dispatchEvent(new WindowEvent(this.f, WindowEvent.WINDOW_CLOSING));
        done.complete(e);
      };
      volatile SerialQueue exe = new SerialQueue(c);
      CFrame f = new CFrame(title, screenSize, done, exe);
      CPanel p = buildContentPane(f, exe);
    };
    o.f.setContentPane(o.p);
    o.f.setUndecorated(undecorated);
    o.f.setResizable(resizable);
    o.f.setOpacity(opacity.o());
    o.f.setFocusable(true);
    o.f.pack();
    o.f.setVisible(true);
    if (maximized) maximize(o.f, screenSize);
    if (location != null) { o.f.setLocation(Awt.point(location)); }
    else { o.f.setLocationRelativeTo(null); }

    int delayMs = Math.round(1000.0f / fps);
    new Timer(delayMs, _ -> o.f.tick(new Types.TimeNanos(System.nanoTime() - startTime.nanos()))).start();
  }

  private CPanel buildContentPane(CFrame frame, SerialQueue queue) {
    var panel = new CPanel(Scope.nop(), frame);
    panel.setLayout(new BorderLayout());
    panel.setOpaque(false);
    keyScope.run(new CKeyBuilder(panel));
    for (var child : children) { panel.add(child.buildPanel(frame, queue)); }
    return panel;
  }
  private static Types.Dimension resolveScreenSize() {
    var b = java.awt.GraphicsEnvironment
      .getLocalGraphicsEnvironment()
      .getDefaultScreenDevice()
      .getDefaultConfiguration()
      .getBounds();
    return new Types.Dimension(new Types.Width(b.width), new Types.Height(b.height));
  }
  private void maximize(CFrame frame, Types.Dimension size) {
    frame.setSize(Awt.dimension(size));
    frame.setLocation(0, 0);
  }

  @Override public FrameBuilder location(Types.Position location) { this.location = location; return this; }
  @Override public FrameBuilder undecorated(Types.Opacity opacity) { this.undecorated = true; this.opacity = opacity; return this; }
  @Override public FrameBuilder onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public FrameBuilder flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder border(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); scope.run(pb); children.add(pb); return this; }
}
