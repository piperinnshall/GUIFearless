package com.piperinnshall.fluentguijava.fluentgraphics;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
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
  <R> R run(String title, int fps, FrameScope<R> frame) {
    var done = new CompletableFuture<RuntimeException>();
    var fb = new CFrameBuilder<R>();
    frame.run(fb);
    SwingUtilities.invokeLater(() -> fb.start(title, fps, done));
    var tr = done.join();
    if (tr == null) return fb.resolve();
    throw tr;
  }
}

@FunctionalInterface interface FrameScope<R> { void run(FrameBuilder<R> f); }
@FunctionalInterface interface PanelScope { void run(PanelBuilder p); }
@FunctionalInterface interface KeyScope { void run(KeyBuilder k); }
@FunctionalInterface interface MouseScope { void run(MouseBuilder m); }
@FunctionalInterface interface GraphicsScope { void run(GraphicsCtx g); }

interface Ctx {
  long elapsed();
  Vec2 screenSize();
  Vec2 panelSize();
}
interface MouseCtx extends Ctx {
  Vec2 pos();
}
interface KeyCtx extends Ctx {
  String key();
}
interface GraphicsCtx extends Ctx {
  GraphicsCtx rect(Vec2 position, Vec2 dimension);
  GraphicsCtx oval(Vec2 position, Vec2 dimension);
  GraphicsCtx color(int hex);
  GraphicsCtx color(Vec3 rgb);
}

interface FrameBuilder<R> {
  FrameBuilder<R> resolve(R r);
  FrameBuilder<R> size(Vec2 dimension);
  FrameBuilder<R> location(Vec2 location);
  FrameBuilder<R> resizable();
  FrameBuilder<R> undecorated();
  FrameBuilder<R> maximized();
  FrameBuilder<R> opacity(float opacity);
  FrameBuilder<R> panel(PanelScope scope);
}

interface PanelBuilder {
  PanelBuilder size(Vec2 dimension);
  PanelBuilder background(int hex);
  PanelBuilder background(Vec3 rgb);
  PanelBuilder paintable(GraphicsScope scope);
  PanelBuilder onKey(KeyScope scope);
  PanelBuilder onMouse(MouseScope scope);
}

interface KeyBuilder {
  KeyBuilder pressed(String key, Consumer<KeyCtx> action);
  KeyBuilder released(String key, Consumer<KeyCtx> action);
}

interface MouseBuilder {
  MouseBuilder clicked(Consumer<MouseCtx> action);
  MouseBuilder pressed(Consumer<MouseCtx> action);
  MouseBuilder released(Consumer<MouseCtx> action);
  MouseBuilder moved(Consumer<MouseCtx> action);
  MouseBuilder dragged(Consumer<MouseCtx> action);
  MouseBuilder entered(Consumer<MouseCtx> action);
  MouseBuilder exited(Consumer<MouseCtx> action);
}

class CFrameBuilder<R> implements FrameBuilder<R> {
  R resolve;
  Point2 dimension;
  Point2 location;
  boolean resizable = false;
  boolean undecorated = false;
  boolean maximized = false;
  float opacity;
  private final long startTime = System.nanoTime();
  List<CPanelBuilder> pbs = new ArrayList<>();
  public void start(String title, int fps, CompletableFuture<RuntimeException> done) {
    var screenSize = resolveScreenSize();
    var frame = new CFrame(title, done);
    var panels = pbs.stream().map(pb -> buildPanel(pb, frame, screenSize)).toList();
    frame.setUndecorated(undecorated);
    frame.setResizable(resizable);
    frame.setOpacity(opacity);
    frame.setFocusable(true);
    frame.pack();
    frame.setVisible(true);
    if (maximized) frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    if (dimension != null) frame.setSize(dimension.awtDimension());
    if (location != null) frame.setLocation(location.awtPoint()); else frame.setLocationRelativeTo(null);
    new Timer(1000 / fps, _ -> panels.forEach(p -> p.repaint(System.nanoTime() - startTime))).start();
  }
  private CPanel buildPanel(CPanelBuilder pb, CFrame frame, Vec2 screenSize) {
    var panel = new CPanel(pb.paintable, screenSize);
    panel.setPreferredSize(pb.dimension.awtDimension());
    panel.setBackground(pb.col);
    frame.add(panel);
    if (pb.keyScope != null) pb.keyScope.run(new CKeyBuilder(panel));
    if (pb.mouseScope != null) pb.mouseScope.run(new CMouseBuilder(panel));
    return panel;
  }
  private static Vec2 resolveScreenSize() {
    var b = java.awt.GraphicsEnvironment
      .getLocalGraphicsEnvironment()
      .getDefaultScreenDevice()
      .getDefaultConfiguration()
      .getBounds();
    return new Vec2(b.width, b.height);
  }
  public R resolve() { return resolve; }
  @Override public FrameBuilder<R> resolve(R r) { this.resolve = r; return this; }
  @Override public FrameBuilder<R> size(Vec2 dimension) { this.dimension = Point2.round(dimension); return this; }
  @Override public FrameBuilder<R> location(Vec2 location) { this.location = Point2.round(location); return this; }
  @Override public FrameBuilder<R> resizable() { this.resizable = true; return this; }
  @Override public FrameBuilder<R> undecorated() { this.undecorated = true; return this; }
  @Override public FrameBuilder<R> maximized() { this.maximized = true; return this; }
  @Override public FrameBuilder<R> opacity(float opacity) { this.opacity = opacity; return this; }
  @Override public FrameBuilder<R> panel(PanelScope scope) {
    var pb = new CPanelBuilder();
    scope.run(pb);
    pbs.add(pb);
    return this;
  }
}

class CPanelBuilder implements PanelBuilder {
  Point2 dimension = new Point2(100, 100);
  Color col;
  GraphicsScope paintable;
  KeyScope keyScope;
  MouseScope mouseScope;
  @Override public PanelBuilder size(Vec2 dimension) { this.dimension = Point2.round(dimension); return this; }
  @Override public PanelBuilder background(int hex) { this.col = new Color(hex); return this; }
  @Override public PanelBuilder background(Vec3 rgb) { this.col = Point3.round(rgb).awtColor(); return this; }
  @Override public PanelBuilder paintable(GraphicsScope scope) { this.paintable = scope; return this; }
  @Override public PanelBuilder onKey(KeyScope keyScope) { this.keyScope = keyScope; return this; }
  @Override public PanelBuilder onMouse(MouseScope mouseScope) { this.mouseScope = mouseScope; return this; }
}

class CKeyBuilder implements KeyBuilder {
  final CPanel panel;
  CKeyBuilder(CPanel panel) { this.panel = panel; }
  private KeyBuilder bind(String stroke, String key, Consumer<KeyCtx> action) {
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(stroke), stroke);
    panel.getActionMap().put(stroke, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        action.accept(new CKeyCtx(panel.elapsedNow(), panel.screenSize(), new Vec2(panel.getWidth(), panel.getHeight()), key));
      }});
    return this;
  }
  @Override public KeyBuilder pressed(String key, Consumer<KeyCtx> action) { return bind(key, key, action); }
  @Override public KeyBuilder released(String key, Consumer<KeyCtx> action) { return bind("released " + key, key, action); }
}

class CMouseBuilder implements MouseBuilder {
  final CPanel panel;
  CMouseBuilder(CPanel panel) { this.panel = panel; }
  private MouseCtx ctx(MouseEvent e) {
    return new CMouseCtx(panel.elapsedNow(), new Vec2(e.getX(), e.getY()), panel.screenSize(), new Vec2(panel.getWidth(), panel.getHeight()));
  }
  private MouseBuilder mouse(MouseAdapter a) { panel.addMouseListener(a); return this; }
  private MouseBuilder motion(MouseMotionAdapter a) { panel.addMouseMotionListener(a); return this; }
  @Override public MouseBuilder clicked(Consumer<MouseCtx> action) {
    return mouse(new MouseAdapter() { public void mouseClicked(MouseEvent e) { action.accept(ctx(e)); }});
  }
  @Override public MouseBuilder pressed(Consumer<MouseCtx> action) {
    return mouse(new MouseAdapter() { public void mousePressed(MouseEvent e) { action.accept(ctx(e)); }});
  }
  @Override public MouseBuilder released(Consumer<MouseCtx> action) {
    return mouse(new MouseAdapter() { public void mouseReleased(MouseEvent e) { action.accept(ctx(e)); }});
  }
  @Override public MouseBuilder moved(Consumer<MouseCtx> action) {
    return motion(new MouseMotionAdapter() { public void mouseMoved(MouseEvent e) { action.accept(ctx(e)); }});
  }
  @Override public MouseBuilder dragged(Consumer<MouseCtx> action) {
    return motion(new MouseMotionAdapter() { public void mouseDragged(MouseEvent e) { action.accept(ctx(e)); }});
  }
  @Override public MouseBuilder entered(Consumer<MouseCtx> action) {
    return mouse(new MouseAdapter() { public void mouseEntered(MouseEvent e) { action.accept(ctx(e)); }});
  }
  @Override public MouseBuilder exited(Consumer<MouseCtx> action) {
    return mouse(new MouseAdapter() { public void mouseExited(MouseEvent e) { action.accept(ctx(e)); }});
  }
}

record CGraphicsCtx(Graphics2D g2d, long elapsed, Vec2 screenSize, Vec2 panelSize) implements GraphicsCtx {
  @Override public GraphicsCtx rect(Vec2 pos, Vec2 dim) {
    Point2 p = Point2.round(pos), d = Point2.round(dim);
    g2d.fillRect(p.x(), p.y(), d.x(), d.y()); return this;
  }
  @Override public GraphicsCtx oval(Vec2 pos, Vec2 dim) {
    Point2 p = Point2.round(pos), d = Point2.round(dim);
    g2d.fillOval(p.x(), p.y(), d.x(), d.y()); return this;
  }
  @Override public GraphicsCtx color(Vec3 rgb) { g2d.setColor(Point3.round(rgb).awtColor()); return this; }
  @Override public GraphicsCtx color(int hex) { g2d.setColor(new Color(hex)); return this; }
}
record CMouseCtx(long elapsed, Vec2 pos, Vec2 screenSize, Vec2 panelSize) implements MouseCtx {}
record CKeyCtx(long elapsed, Vec2 screenSize, Vec2 panelSize, String key) implements KeyCtx {}

class CFrame extends JFrame {
  CFrame(String title, CompletableFuture<RuntimeException> done) {
    super(title);
    addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent e) { done.complete(null); }});
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}
class CPanel extends JPanel {
  private long elapsed;
  private final Vec2 screenSize;
  private final GraphicsScope paintable;
  CPanel(GraphicsScope paintable, Vec2 screenSize) {
    this.paintable = paintable;
    this.screenSize = screenSize;
  }
  Vec2 screenSize() { return screenSize; }
  public void repaint(long elapsed) { this.elapsed = elapsed; repaint(); }
  long elapsedNow() { return elapsed; }
  @Override public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (paintable == null) return;
    paintable.run(new CGraphicsCtx((Graphics2D) g, elapsed, screenSize, new Vec2(getWidth(), getHeight())));
  }
}
