package com.piperinnshall.fluentguijava.core;

import java.awt.Color;
import java.awt.FlowLayout;
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
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import com.piperinnshall.fluentguijava.builder.Ctx;
import com.piperinnshall.fluentguijava.builder.FrameBuilder;
import com.piperinnshall.fluentguijava.builder.KeyBuilder;
import com.piperinnshall.fluentguijava.builder.MouseBuilder;
import com.piperinnshall.fluentguijava.builder.PanelBuilder;
import com.piperinnshall.fluentguijava.builder.Scope;
import com.piperinnshall.fluentguijava.vec.Vec2;
import com.piperinnshall.fluentguijava.vec.Vec3;

abstract class CPanelBuilder implements PanelBuilder {
  Vec2 dimension = new Vec2(100, 100);
  Color col = Color.BLACK;
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
  @Override public PanelBuilder size(Vec2 dimension) { this.dimension = dimension; return this; }
  @Override public PanelBuilder background(int hex) { this.col = new Color(hex); return this; }
  @Override public PanelBuilder background(Vec3 rgb) { this.col = Awt.color(rgb); return this; }
  @Override public PanelBuilder paintable(Scope<Ctx.Graphics> scope) { this.paintable = scope; return this; }
  @Override public PanelBuilder onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public PanelBuilder onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return this; }
  @Override public PanelBuilder flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return this; }
}

class CPanelBuilderFlow extends CPanelBuilder implements PanelBuilder.Flow {
  @Override CPanel buildPanel(CFrame frame) {
    var panel = basePanel(frame);
    panel.setLayout(new FlowLayout());
    children.forEach(child -> panel.add(child.buildPanel(frame)));
    return panel;
  }
}

abstract class CFrameBuilder extends CPanelBuilder implements FrameBuilder {
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
  @Override public FrameBuilder size(Vec2 dimension) { this.frameSize = dimension; return this; }
  @Override public FrameBuilder location(Vec2 location) { this.location = location; return this; }
  @Override public FrameBuilder resizable() { this.resizable = true; return this; }
  @Override public FrameBuilder undecorated() { this.undecorated = true; return this; }
  @Override public FrameBuilder maximized() { this.maximized = true; return this; }
  @Override public FrameBuilder opacity(float opacity) { this.opacity = opacity; return this; }
  @Override public FrameBuilder background(int hex) { this.col = new Color(hex); return this; }
  @Override public FrameBuilder background(Vec3 rgb) { this.col = Awt.color(rgb); return this; }
  @Override public FrameBuilder paintable(Scope<Ctx.Graphics> scope) { this.paintable = scope; return this; }
  @Override public FrameBuilder onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return this; }
  @Override public FrameBuilder onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return this; }
  @Override public FrameBuilder flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return this; }
}

class CFrameBuilderFlow extends CFrameBuilder implements FrameBuilder.Flow {
  @Override CPanel buildPanel(CFrame frame) {
    var panel = basePanel(frame);
    panel.setLayout(new FlowLayout());
    children.forEach(child -> panel.add(child.buildPanel(frame)));
    return panel;
  }
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
