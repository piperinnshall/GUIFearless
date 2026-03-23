package com.piperinnshall.fluentguijava.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import com.piperinnshall.fluentguijava.main.builder.FrameBuilder;
import com.piperinnshall.fluentguijava.main.builder.KeyBuilder;
import com.piperinnshall.fluentguijava.main.builder.MouseBuilder;
import com.piperinnshall.fluentguijava.main.builder.PanelBuilder;
import com.piperinnshall.fluentguijava.main.builder.Scope;
import com.piperinnshall.fluentguijava.main.vec.Vec2;
import com.piperinnshall.fluentguijava.main.vec.Vec3;

interface Awt {
  static Dimension dimension(Vec2 v) { return new Dimension(Math.round(v.x()), Math.round(v.y())); }
  static Point point(Vec2 v) { return new Point(Math.round(v.x()), Math.round(v.y())); }
  static Color color(Vec3 v) { return new Color(Math.round(v.x()), Math.round(v.y()), Math.round(v.z())); }
}

class CFrameBuilder<R> implements FrameBuilder<R> {
  R resolve;
  Vec2 dimension;
  Vec2 location;
  boolean resizable = false;
  boolean undecorated = false;
  boolean maximized = false;
  float opacity = 1f;
  private final long startTime = System.nanoTime();
  private final CPanelBuilder root = new CPanelBuilder();
  List<CPanelBuilder> pbs = new ArrayList<>();

  public void start(String title, int fps, CompletableFuture<RuntimeException> done) {
    var screenSize = resolveScreenSize();
    var frame = new CFrame(title, screenSize, done);
    var rootPanel = buildPanel(root, frame);
    frame.setContentPane(rootPanel);
    pbs.forEach(pb -> rootPanel.add(buildPanel(pb, frame)));
    frame.setUndecorated(undecorated);
    frame.setResizable(resizable);
    if (undecorated) { frame.setOpacity(opacity); }
    frame.setFocusable(true);
    frame.pack();
    frame.setVisible(true);
    if (maximized) { maximize(frame); }
    if (dimension != null) { frame.setSize(Awt.dimension(dimension)); }
    if (location != null) { frame.setLocation(Awt.point(location)); } else { frame.setLocationRelativeTo(null); }
    new Timer(1000 / fps, _ -> frame.tick(System.nanoTime() - startTime)).start();
  }

  private CPanel buildPanel(CPanelBuilder pb, CFrame frame) {
    var panel = new CPanel(pb.paintable, frame);
    panel.setPreferredSize(Awt.dimension(pb.dimension));
    panel.setBackground(pb.col);
    pb.keyScope.run(new CKeyBuilder(panel));
    pb.mouseScope.run(new CMouseBuilder(panel));
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

  private void maximize(CFrame frame) {
    var size = resolveScreenSize();
    frame.setSize(Awt.dimension(size));
    frame.setLocation(0, 0);
  }

  public R resolve() { return resolve; }
  @Override public FrameBuilder<R> resolve(R r) { this.resolve = r; return this; }
  @Override public FrameBuilder<R> size(Vec2 dimension) { this.dimension = dimension; return this; }
  @Override public FrameBuilder<R> location(Vec2 location) { this.location = location; return this; }
  @Override public FrameBuilder<R> resizable() { this.resizable = true; return this; }
  @Override public FrameBuilder<R> undecorated() { this.undecorated = true; return this; }
  @Override public FrameBuilder<R> maximized() { this.maximized = true; return this; }
  @Override public FrameBuilder<R> opacity(float opacity) { this.opacity = opacity; return this; }
  @Override public FrameBuilder<R> panel(Scope<PanelBuilder> scope) { var pb = new CPanelBuilder(); scope.run(pb); pbs.add(pb); return this; }
  @Override public FrameBuilder<R> background(int hex) { root.background(hex); return this; }
  @Override public FrameBuilder<R> background(Vec3 rgb) { root.background(rgb); return this; }
  @Override public FrameBuilder<R> paintable(Scope<Ctx.Graphics> scope) { root.paintable(scope); return this; }
  @Override public FrameBuilder<R> onKey(Scope<KeyBuilder> scope) { root.onKey(scope); return this; }
  @Override public FrameBuilder<R> onMouse(Scope<MouseBuilder> scope) { root.onMouse(scope); return this; }
}

class CPanelBuilder implements PanelBuilder {
  Vec2 dimension = new Vec2(100, 100);
  Color col = Color.BLACK;
  Scope<Ctx.Graphics> paintable = Scope.nop();
  Scope<KeyBuilder> keyScope = Scope.nop();
  Scope<MouseBuilder> mouseScope = Scope.nop();

  @Override public PanelBuilder size(Vec2 dimension) { this.dimension = dimension; return this; }
  @Override public PanelBuilder background(int hex) { this.col = new Color(hex); return this; }
  @Override public PanelBuilder background(Vec3 rgb) { this.col = Awt.color(rgb); return this; }
  @Override public PanelBuilder paintable(Scope<Ctx.Graphics> scope) { this.paintable = scope; return this; }
  @Override public PanelBuilder onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public PanelBuilder onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return this; }
}

record CKeyBuilder(CPanel panel) implements KeyBuilder {
  private KeyBuilder bind(String stroke, String key, Consumer<Ctx.Key> action) {
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(stroke), stroke);
    panel.getActionMap().put(stroke, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        action.accept(new CKeyCtx(panel.frame().elapsed(), panel.frame().screenSize(),
            new Vec2(panel.getWidth(), panel.getHeight()), key));
      }
    });
    return this;
  }
  @Override public KeyBuilder pressed(String key, Consumer<Ctx.Key> action) { return bind(key, key, action); }
  @Override public KeyBuilder released(String key, Consumer<Ctx.Key> action) { return bind("released " + key, key, action); }
}

record CMouseBuilder(CPanel panel) implements MouseBuilder {
  private Ctx.Mouse ctx(MouseEvent e) {
    return new CMouseCtx(panel.frame().elapsed(), new Vec2(e.getX(), e.getY()), panel.frame().screenSize(),
        new Vec2(panel.getWidth(), panel.getHeight()));
  }

  private MouseBuilder mouse(MouseAdapter a) { panel.addMouseListener(a); return this; }
  private MouseBuilder motion(MouseMotionAdapter a) { panel.addMouseMotionListener(a); return this; }
  @Override public MouseBuilder clicked(Consumer<Ctx.Mouse> action) { return mouse(new MouseAdapter() { public void mouseClicked(MouseEvent e) { action.accept(ctx(e)); } }); }
  @Override public MouseBuilder pressed(Consumer<Ctx.Mouse> action) { return mouse(new MouseAdapter() { public void mousePressed(MouseEvent e) { action.accept(ctx(e)); } }); }
  @Override public MouseBuilder released(Consumer<Ctx.Mouse> action) { return mouse(new MouseAdapter() { public void mouseReleased(MouseEvent e) { action.accept(ctx(e)); } }); }
  @Override public MouseBuilder moved(Consumer<Ctx.Mouse> action) { return motion(new MouseMotionAdapter() { public void mouseMoved(MouseEvent e) { action.accept(ctx(e)); } }); }
  @Override public MouseBuilder dragged(Consumer<Ctx.Mouse> action) { return motion(new MouseMotionAdapter() { public void mouseDragged(MouseEvent e) { action.accept(ctx(e)); } }); }
  @Override public MouseBuilder entered(Consumer<Ctx.Mouse> action) { return mouse(new MouseAdapter() { public void mouseEntered(MouseEvent e) { action.accept(ctx(e)); } }); }
  @Override public MouseBuilder exited(Consumer<Ctx.Mouse> action) { return mouse(new MouseAdapter() { public void mouseExited(MouseEvent e) { action.accept(ctx(e)); } }); }
}

record CGraphicsCtx(Graphics2D g2d, long elapsed, Vec2 screenSize, Vec2 panelSize) implements Ctx.Graphics {
  private static int r(float v) { return Math.round(v); }
  @Override public Ctx.Graphics rect(Vec2 pos, Vec2 dim) { g2d.fillRect(r(pos.x()), r(pos.y()), r(dim.x()), r(dim.y())); return this; }
  @Override public Ctx.Graphics oval(Vec2 pos, Vec2 dim) { g2d.fillOval(r(pos.x()), r(pos.y()), r(dim.x()), r(dim.y())); return this; }
  @Override public Ctx.Graphics line(Vec2 from, Vec2 to) { g2d.drawLine(r(from.x()), r(from.y()), r(to.x()), r(to.y())); return this; }
  @Override public Ctx.Graphics color(Vec3 rgb) { g2d.setColor(Awt.color(rgb)); return this; }
  @Override public Ctx.Graphics color(int hex) { g2d.setColor(new Color(hex)); return this; }
}

record CMouseCtx(long elapsed, Vec2 pos, Vec2 screenSize, Vec2 panelSize) implements Ctx.Mouse {}
record CKeyCtx(long elapsed, Vec2 screenSize, Vec2 panelSize, String key) implements Ctx.Key {}

class CFrame extends JFrame {
  private long elapsed;
  private final Vec2 screenSize;
  CFrame(String title, Vec2 screenSize, CompletableFuture<RuntimeException> done) {
    super(title);
    this.screenSize = screenSize;
    addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent e) { done.complete(null); } });
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
  void tick(long elapsed) { this.elapsed = elapsed; repaint(); }
  long elapsed() { return elapsed; }
  Vec2 screenSize() { return screenSize; }
}

class CPanel extends JPanel {
  private final CFrame frame;
  private final Scope<Ctx.Graphics> paintable;
  CPanel(Scope<Ctx.Graphics> paintable, CFrame frame) { this.paintable = paintable; this.frame = frame; }
  CFrame frame() { return frame; }
  @Override public void paintComponent(Graphics g) {
    super.paintComponent(g);
    paintable.run(new CGraphicsCtx((Graphics2D) g, frame.elapsed(), frame.screenSize(), new Vec2(getWidth(), getHeight())));
  }
}
