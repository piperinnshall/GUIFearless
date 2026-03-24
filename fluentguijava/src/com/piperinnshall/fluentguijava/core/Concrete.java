package com.piperinnshall.fluentguijava.core;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.Timer;

import com.piperinnshall.fluentguijava.builder.Ctx;
import com.piperinnshall.fluentguijava.builder.FrameBuilder;
import com.piperinnshall.fluentguijava.builder.KeyBuilder;
import com.piperinnshall.fluentguijava.builder.MouseBuilder;
import com.piperinnshall.fluentguijava.builder.PanelBuilder;
import com.piperinnshall.fluentguijava.builder.Scope;
import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

abstract class CPanelBuilder {
  Vec2 dimension = new Vec2(100, 100);
  Color col = Color.BLACK;
  String constraint;
  Scope<Ctx.Graphics> paintable = Scope.nop();
  Scope<KeyBuilder> keyScope = Scope.nop();
  Scope<MouseBuilder> mouseScope = Scope.nop();
  List<CPanelBuilder> children = new ArrayList<>();

  abstract CPanel buildPanel(CFrame frame);
  protected CPanel basePanel(CFrame frame) {
    var panel = new CPanel(paintable, frame);
    panel.setPreferredSize(Awt.dimension(dimension));
    panel.setBackground(col);
    keyScope.run(new CKeyBuilder(panel));
    mouseScope.run(new CMouseBuilder(panel));
    return panel;
  }
}

abstract class CFrameBuilder extends CPanelBuilder {
  Vec2 location;
  Vec2 frameSize;
  boolean resizable = false;
  boolean undecorated = false;
  boolean maximized = false;
  float opacity = 1f;
  private final long startTime = System.nanoTime();

  public void start(String title, int fps, CompletableFuture<RuntimeException> done) {
    var screenSize = resolveScreenSize();
    var frame = new CFrame(title, screenSize, done);
    var rootPanel = buildPanel(frame);
    frame.setContentPane(rootPanel);
    frame.setUndecorated(undecorated);
    frame.setResizable(resizable);
    if (undecorated) { frame.setOpacity(opacity); }
    frame.setFocusable(true);
    frame.pack();
    frame.setVisible(true);
    if (maximized) { maximize(frame); }
    if (frameSize != null) { frame.setSize(Awt.dimension(frameSize)); }
    if (location != null) { frame.setLocation(Awt.point(location)); } else { frame.setLocationRelativeTo(null); }
    new Timer(1000 / fps, _ -> frame.tick(System.nanoTime() - startTime)).start();
  }
  private static Vec2 resolveScreenSize() {
    var b = java.awt.GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice()
        .getDefaultConfiguration()
        .getBounds();
    return new Vec2(b.width, b.height);
  }
  private void maximize(CFrame frame) {
    var size = resolveScreenSize();
    frame.setSize(Awt.dimension(size));
    frame.setLocation(0, 0);
  }
}

class CPanelBuilderFlow extends CPanelBuilder implements PanelBuilder.Flow {
  @Override CPanel buildPanel(CFrame frame) {
    var panel = basePanel(frame);
    panel.setLayout(new FlowLayout());
    children.forEach(child -> panel.add(child.buildPanel(frame)));
    return panel;
  }
  @Override public PanelBuilder.Flow size(Vec2 dimension) { this.dimension = dimension; return this; }
  @Override public PanelBuilder.Flow background(Vec3 rgb) { this.col = Awt.color(rgb); return this; }
  @Override public PanelBuilder.Flow paintable(Scope<Ctx.Graphics> scope) { this.paintable = scope; return this; }
  @Override public PanelBuilder.Flow onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public PanelBuilder.Flow onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return this; }
  @Override public PanelBuilder.Flow flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Flow border(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); scope.run(pb); children.add(pb); return this; }
}

class CFrameBuilderFlow extends CFrameBuilder implements FrameBuilder.Flow {
  @Override CPanel buildPanel(CFrame frame) {
    var panel = basePanel(frame);
    panel.setLayout(new FlowLayout());
    children.forEach(child -> panel.add(child.buildPanel(frame)));
    return panel;
  }
  @Override public FrameBuilder.Flow size(Vec2 dimension) { this.frameSize = dimension; return this; }
  @Override public FrameBuilder.Flow background(Vec3 rgb) { this.col = Awt.color(rgb); return this; }
  @Override public FrameBuilder.Flow paintable(Scope<Ctx.Graphics> scope) { this.paintable = scope; return this; }
  @Override public FrameBuilder.Flow onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public FrameBuilder.Flow onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return this; }
  @Override public FrameBuilder.Flow flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Flow border(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Flow location(Vec2 location) { this.location = location; return this; }
  @Override public FrameBuilder.Flow resizable() { this.resizable = true; return this; }
  @Override public FrameBuilder.Flow undecorated() { this.undecorated = true; return this; }
  @Override public FrameBuilder.Flow maximized() { this.maximized = true; return this; }
  @Override public FrameBuilder.Flow opacity(float opacity) { this.opacity = opacity; return this; }
}

class CPanelBuilderBorder extends CPanelBuilder implements PanelBuilder.Border {
  @Override CPanel buildPanel(CFrame frame) {
    var panel = basePanel(frame);
    panel.setLayout(new BorderLayout());
    children.forEach(child -> panel.add(child.buildPanel(frame), child.constraint));
    return panel;
  }
  @Override public PanelBuilder.Border size(Vec2 dimension) { this.dimension = dimension; return this; }
  @Override public PanelBuilder.Border background(Vec3 rgb) { this.col = Awt.color(rgb); return this; }
  @Override public PanelBuilder.Border paintable(Scope<Ctx.Graphics> scope) { this.paintable = scope; return this; }
  @Override public PanelBuilder.Border onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public PanelBuilder.Border onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return this; }
  @Override public PanelBuilder.Border flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border border(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border northFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.NORTH; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border southFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.SOUTH; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border eastFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.EAST; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border westFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.WEST; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border centerFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.CENTER; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border northBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.NORTH; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border southBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.SOUTH; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border eastBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.EAST; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border westBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.WEST; scope.run(pb); children.add(pb); return this; }
  @Override public PanelBuilder.Border centerBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.CENTER; scope.run(pb); children.add(pb); return this; }
}

class CFrameBuilderBorder extends CFrameBuilder implements FrameBuilder.Border {
  @Override CPanel buildPanel(CFrame frame) {
    var panel = basePanel(frame);
    panel.setLayout(new BorderLayout());
    children.forEach(child -> panel.add(child.buildPanel(frame), child.constraint));
    return panel;
  }
  @Override public FrameBuilder.Border size(Vec2 dimension) { this.frameSize = dimension; return this; }
  @Override public FrameBuilder.Border background(Vec3 rgb) { this.col = Awt.color(rgb); return this; }
  @Override public FrameBuilder.Border paintable(Scope<Ctx.Graphics> scope) { this.paintable = scope; return this; }
  @Override public FrameBuilder.Border onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public FrameBuilder.Border onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return this; }
  @Override public FrameBuilder.Border flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border border(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border location(Vec2 location) { this.location = location; return this; }
  @Override public FrameBuilder.Border resizable() { this.resizable = true; return this; }
  @Override public FrameBuilder.Border undecorated() { this.undecorated = true; return this; }
  @Override public FrameBuilder.Border maximized() { this.maximized = true; return this; }
  @Override public FrameBuilder.Border opacity(float opacity) { this.opacity = opacity; return this; }
  @Override public FrameBuilder.Border northFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.NORTH; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border southFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.SOUTH; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border eastFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.EAST; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border westFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.WEST; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border centerFlow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); pb.constraint = BorderLayout.CENTER; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border northBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.NORTH; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border southBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.SOUTH; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border eastBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.EAST; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border westBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.WEST; scope.run(pb); children.add(pb); return this; }
  @Override public FrameBuilder.Border centerBorder(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); pb.constraint = BorderLayout.CENTER; scope.run(pb); children.add(pb); return this; }
}
