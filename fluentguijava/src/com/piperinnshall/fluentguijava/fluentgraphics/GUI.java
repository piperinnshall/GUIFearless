package com.piperinnshall.fluentguijava.fluentgraphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

class GUI {
  <R> R run(String title, FrameScope<R> frame) {
    var done = new CompletableFuture<RuntimeException>();
    var fb = new CFrameBuilder<R>();
    frame.run(fb);
    SwingUtilities.invokeLater(() -> fb.start(title, done));
    var tr = done.join();
    if (tr == null) { return fb.resolve(); }
    throw tr;
  }
}

class Slot<T> {
  private Optional<T> inner = Optional.empty();
  static <T> Slot<T> of() { return new Slot<>(); }
  void fill(T t) { inner = Optional.of(t); }
  T get() { return inner.get(); }
}

@FunctionalInterface interface FrameScope<R> { void run(FrameBuilder<R> f); }
@FunctionalInterface interface PanelScope { void run(PanelBuilder p); }
@FunctionalInterface interface KeyScope { void run(KeyBuilder k); }
@FunctionalInterface interface MouseScope { void run(MouseBuilder m); }
@FunctionalInterface interface Paintable { void paint(GraphicsCtx graphicsCtx, long elapsedNanos); }

interface FrameBuilder<R> {
  FrameBuilder<R> resolve(R r);
  FrameBuilder<R> location(Vec2 location);
  FrameBuilder<R> resizable();
  FrameBuilder<R> panel(PanelScope scope);
}

interface PanelBuilder {
  PanelBuilder size(Vec2 dimension);
  PanelBuilder fps(int fps);
  PanelBuilder background(int rgb);
  PanelBuilder background(int r, int g, int b);
  PanelBuilder paintable(Paintable p);
  PanelBuilder onKey(KeyScope scope);
  PanelBuilder onMouse(MouseScope scope);
}

interface KeyBuilder {
  KeyBuilder pressed(String key, Runnable action);
  KeyBuilder released(String key, Runnable action);
}

interface MouseBuilder {
  MouseBuilder clicked(Consumer<Vec2> action);
  MouseBuilder pressed(Consumer<Vec2> action);
  MouseBuilder released(Consumer<Vec2> action);
  MouseBuilder moved(Consumer<Vec2> action);
  MouseBuilder dragged(Consumer<Vec2> action);
  MouseBuilder entered(Runnable action);
  MouseBuilder exited(Runnable action);
}

interface GraphicsCtx {
  GraphicsCtx rect(Vec2 position, Vec2 dimension);
  GraphicsCtx oval(Vec2 position, Vec2 dimension);
  GraphicsCtx color(int r, int g, int b);
  GraphicsCtx color(int rgb);
}

class CFrameBuilder<R> implements FrameBuilder<R> {
  R resolve;
  Point location;
  boolean resizable = false;
  CPanelBuilder pb = new CPanelBuilder();
  public void start(String title, CompletableFuture<RuntimeException> done) {
    var panel = new FPanel(pb.paintable);
    var frame = new FFrame(title, done);
    panel.setPreferredSize(new Dimension(pb.dimension.x(), pb.dimension.y()));
    panel.setBackground(pb.col);
    frame.add(panel);
    frame.setResizable(resizable);
    frame.setFocusable(true);
    frame.pack();
    frame.setVisible(true);
    if (location == null) frame.setLocationRelativeTo(null);
    else frame.setLocation(location.x(), location.y());
    if (pb.keyScope != null) pb.keyScope.run(new CKeyBuilder(panel));
    if (pb.mouseScope != null) pb.mouseScope.run(new CMouseBuilder(panel));
    new Timer(1000 / pb.fps, _ -> panel.repaint()).start();
  }
  public R resolve() { return resolve; }  // Doesn't actually make sense, needs to be an 'event handler'.
  @Override public FrameBuilder<R> resolve(R r) { this.resolve = r; return this; }
  @Override public FrameBuilder<R> location(Vec2 location) { this.location = Point.round(location); return this; }
  @Override public FrameBuilder<R> resizable() { this.resizable = true; return this; }
  @Override public FrameBuilder<R> panel(PanelScope scope) { scope.run(pb); return this; }
}

class CPanelBuilder implements PanelBuilder {
  Point dimension = new Point(100, 100);
  int fps = 60;
  Color col;
  Paintable paintable;
  KeyScope keyScope;
  MouseScope mouseScope;
  @Override public PanelBuilder size(Vec2 dimension) { this.dimension = Point.round(dimension); return this; }
  @Override public PanelBuilder fps(int fps) { this.fps = fps; return this; }
  @Override public PanelBuilder background(int rgb) { this.col = new Color(rgb); return this; }
  @Override public PanelBuilder background(int r, int g, int b) { this.col = new Color(r, g, b); return this; }
  @Override public PanelBuilder paintable(Paintable paintable) { this.paintable = paintable; return this; }
  @Override public PanelBuilder onKey(KeyScope keyScope) { this.keyScope = keyScope; return this; }
  @Override public PanelBuilder onMouse(MouseScope mouseScope) { this.mouseScope = mouseScope; return this; }
}

class CKeyBuilder implements KeyBuilder {
  final JPanel panel;
  CKeyBuilder(JPanel panel) { this.panel = panel; }
  private KeyBuilder bind(String stroke, Runnable action) {
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(stroke), stroke);
    panel.getActionMap().put(stroke, new AbstractAction() {
      public void actionPerformed(ActionEvent e) { action.run(); }});
    return this;
  }
  @Override public KeyBuilder pressed(String key, Runnable action) { return bind(key, action); }
  @Override public KeyBuilder released(String key, Runnable action) { return bind("released " + key, action); }
}

class CMouseBuilder implements MouseBuilder {
  final JPanel panel;
  CMouseBuilder(JPanel panel) { this.panel = panel; }
  private static Vec2 pos(MouseEvent e) { return new Vec2(e.getX(), e.getY()); }
  private MouseBuilder mouse(MouseAdapter a) { panel.addMouseListener(a); return this; }
  private MouseBuilder motion(MouseMotionAdapter a) { panel.addMouseMotionListener(a); return this; }
  @Override public MouseBuilder clicked(Consumer<Vec2> action) {
    return mouse(new MouseAdapter() { public void mouseClicked(MouseEvent e) { action.accept(pos(e)); }});
  }
  @Override public MouseBuilder pressed(Consumer<Vec2> action) {
    return mouse(new MouseAdapter() { public void mousePressed(MouseEvent e) { action.accept(pos(e)); }});
  }
  @Override public MouseBuilder released(Consumer<Vec2> action) {
    return mouse(new MouseAdapter() { public void mouseReleased(MouseEvent e) { action.accept(pos(e)); }});
  }
  @Override public MouseBuilder moved(Consumer<Vec2> action) {
    return motion(new MouseMotionAdapter() { public void mouseMoved(MouseEvent e) { action.accept(pos(e)); }});
  }
  @Override public MouseBuilder dragged(Consumer<Vec2> action) {
    return motion(new MouseMotionAdapter() { public void mouseDragged(MouseEvent e) { action.accept(pos(e)); }});
  }
  @Override public MouseBuilder entered(Runnable action) {
    return mouse(new MouseAdapter() { public void mouseEntered(MouseEvent e) { action.run(); }});
  }
  @Override public MouseBuilder exited(Runnable action) {
    return mouse(new MouseAdapter() { public void mouseExited(MouseEvent e) { action.run(); }});
  }
}

record CGraphicsCtx(Graphics2D g2d) implements GraphicsCtx {
  @Override public GraphicsCtx rect(Vec2 pos, Vec2 dim) {
    Point p = Point.round(pos), d = Point.round(dim);
    g2d.fillRect(p.x(), p.y(), d.x(), d.y());
    return this;
  }
  @Override public GraphicsCtx oval(Vec2 pos, Vec2 dim) {
    Point p = Point.round(pos), d = Point.round(dim);
    g2d.fillOval(p.x(), p.y(), d.x(), d.y());
    return this;
  }
  @Override public GraphicsCtx color(int r, int g, int b) { g2d.setColor(new Color(r, g, b)); return this; }
  @Override public GraphicsCtx color(int rgb) { g2d.setColor(new Color(rgb)); return this; }
}

class FFrame extends JFrame {
  FFrame(String title, CompletableFuture<RuntimeException> done) {
    super(title);
    addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent e) { done.complete(null); }});
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}

class FPanel extends JPanel {
  private final long startTime = System.nanoTime();
  private final Paintable paintable;
  FPanel(Paintable paintable) { this.paintable = paintable; }
  @Override public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (paintable == null) return;
    paintable.paint(new CGraphicsCtx((Graphics2D) g), System.nanoTime() - startTime);
  }
}
