package com.piperinnshall.fluentguijava.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.piperinnshall.fluentguijava.fearless.Types.*;
import com.piperinnshall.fluentguijava.fearless.Ctx;
import com.piperinnshall.fluentguijava.fearless.KeyBuilder;
import com.piperinnshall.fluentguijava.fearless.MouseBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;

record CKeyBuilder(CPanel panel) implements KeyBuilder {
  private KeyBuilder bind(String stroke, String key, Consumer<Ctx.Key> action) {
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(stroke), stroke);
    panel.getActionMap().put(stroke, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        action.accept(new CKeyCtx(panel.frame().elapsed(), panel.frame().screenSize(),
              new Vector2(panel.getWidth(), panel.getHeight()), key));
      }
    });
    return this;
  }
  @Override public KeyBuilder pressed(String key, Consumer<Ctx.Key> action) { return bind(key, key, action); }
  @Override public KeyBuilder released(String key, Consumer<Ctx.Key> action) { return bind("released " + key, key, action); }
}

record CMouseBuilder(CPanel panel) implements MouseBuilder {
  private Ctx.Mouse ctx(MouseEvent e) {
    return new CMouseCtx(panel.frame().elapsed(), new Vector2(e.getX(), e.getY()), panel.frame().screenSize(),
        new Vector2(panel.getWidth(), panel.getHeight()));
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

record CGraphicsCtx(Graphics2D g2d, long elapsed, Vector2 screenSize, Vector2 panelSize) implements Ctx.Graphics {
  private static int r(float v) { return Math.round(v); }
  @Override public Ctx.Graphics rect(Vector2 pos, Vector2 dim) { g2d.fillRect(r(pos.x()), r(pos.y()), r(dim.x()), r(dim.y())); return this; }
  @Override public Ctx.Graphics oval(Vector2 pos, Vector2 dim) { g2d.fillOval(r(pos.x()), r(pos.y()), r(dim.x()), r(dim.y())); return this; }
  @Override public Ctx.Graphics line(Vector2 from, Vector2 to) { g2d.drawLine(r(from.x()), r(from.y()), r(to.x()), r(to.y())); return this; }
  @Override public Ctx.Graphics color(Vector3 rgb) { g2d.setColor(Awt.color(rgb)); return this; }
  @Override public Ctx.Graphics color(int hex) { g2d.setColor(new Color(hex)); return this; }
}
record CMouseCtx(long elapsed, Vector2 pos, Vector2 screenSize, Vector2 panelSize) implements Ctx.Mouse {}
record CKeyCtx(long elapsed, Vector2 screenSize, Vector2 panelSize, String key) implements Ctx.Key {}

class CFrame extends JFrame {
  private long elapsed;
  private final Vector2 screenSize;
  CFrame(String title, Vector2 screenSize, CompletableFuture<RuntimeException> done) {
    super(title);
    this.screenSize = screenSize;
    addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent e) { done.complete(null); } });
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
  void tick(long elapsed) { this.elapsed = elapsed; repaint(); }
  long elapsed() { return elapsed; }
  Vector2 screenSize() { return screenSize; }
}

class CPanel extends JPanel {
  private final CFrame frame;
  private final Scope<Ctx.Graphics> paintable;
  CPanel(Scope<Ctx.Graphics> paintable, CFrame frame) { this.paintable = paintable; this.frame = frame; }
  CFrame frame() { return frame; }
  @Override public void paintComponent(Graphics g) {
    super.paintComponent(g);
    paintable.run(new CGraphicsCtx((Graphics2D) g, frame.elapsed(), frame.screenSize(), new Vector2(getWidth(), getHeight())));
  }
}
