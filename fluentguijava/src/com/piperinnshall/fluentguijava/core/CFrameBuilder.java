package com.piperinnshall.fluentguijava.core;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.Timer;

import com.piperinnshall.fluentguijava.fearless.FrameBuilder;
import com.piperinnshall.fluentguijava.fearless.KeyBuilder;
import com.piperinnshall.fluentguijava.fearless.MouseBuilder;
import com.piperinnshall.fluentguijava.fearless.PanelBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Types;

class CFrameBuilder implements FrameBuilder {
  Types.Position location = null;
  boolean resizable = false;
  boolean undecorated = false;
  boolean maximized = false;
  Types.Opacity opacity = new Types.Opacity(1);
  Scope<KeyBuilder> keyScope = Scope.nop();
  Scope<MouseBuilder> mouseScope = Scope.nop();
  private final Types.Time startTime = new Types.Time(System.nanoTime());
  private final List<APanelBuilder<?>> children = new ArrayList<>();

  public void start(String title, int fps, CompletableFuture<RuntimeException> done) {
    if (fps <= 0) throw new IllegalArgumentException("fps must be > 0, got: " + fps);
    Types.Dimension screenSize = resolveScreenSize();
    var frame = new CFrame(title, screenSize, done);
    var topPane = buildTopPane(frame);
    var glassPane = buildGlassPane(frame);
    frame.setContentPane(topPane);
    frame.setGlassPane(glassPane);
    frame.setUndecorated(undecorated);
    frame.setResizable(resizable);
    frame.setOpacity(opacity.o());
    frame.setFocusable(true);
    frame.pack();
    frame.setVisible(true);
    if (maximized) maximize(frame, screenSize);
    if (location != null) { frame.setLocation(Awt.point(location)); }
    else { frame.setLocationRelativeTo(null); }
    int delayMs = Math.round(1000.0f / fps);
    new Timer(delayMs, _ -> frame.tick(new Types.Time(System.nanoTime() - startTime.t()))).start();
  }

  private CPanel buildTopPane(CFrame frame) {
    var panel = new CPanel(Scope.nop(), frame);
    panel.setLayout(new BorderLayout());
    panel.setOpaque(false);
    for (var child : children) { panel.add(child.buildPanel(frame)); }
    return panel;
  }

  private CPanel buildGlassPane(CFrame frame) {
    var glass = new CPanel(Scope.nop(), frame);
    glass.setOpaque(false);
    glass.setVisible(true);
    glass.setLayout(null);
    keyScope.run(new CKeyBuilder(glass));
    mouseScope.run(new CMouseBuilder(glass));
    return glass;
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
  @Override public FrameBuilder maximized() { this.maximized = true; return this; }
  @Override public FrameBuilder resizable() { this.resizable = true; return this; }
  @Override public FrameBuilder undecorated(Types.Opacity opacity) { this.undecorated = true; this.opacity = opacity; return this; }
  @Override public FrameBuilder onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public FrameBuilder onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return this; }
  @Override public FrameBuilder flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder border(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); scope.run(pb); children.add(pb); return this; }
}
