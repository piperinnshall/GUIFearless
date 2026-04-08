package com.piperinnshall.fluentguijava.core;

import com.piperinnshall.fluentguijava.fearless.Ctx;
import com.piperinnshall.fluentguijava.fearless.KeyBuilder;
import com.piperinnshall.fluentguijava.fearless.MouseBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Swing;
import com.piperinnshall.fluentguijava.fearless.Types;
import java.awt.AWTEvent;
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
import java.util.function.Supplier;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

record CKeyBuilder(CPanel panel) implements KeyBuilder {
  private KeyBuilder bind(
      String actionKey,
      Supplier<Types.KeyStroke> stroke,
      Consumer<Ctx.Key> action
  ) {
    var current = new Object() {
      KeyStroke value = KeyStroke.getKeyStroke(stroke.get().k());
      };

    var inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(current.value, actionKey);

    panel.getActionMap().put(actionKey, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        action.accept(new CKeyCtx(
          panel.frame().elapsed(),
          panel.frame().screenSize(),
          panel.panelSize(),
          stroke.get())
          );
        }
      });

    panel.frame().addTickHook(() -> {
      var desired = KeyStroke.getKeyStroke(stroke.get().k());
      if (!desired.equals(current.value)) {
        inputMap.remove(current.value);
        inputMap.put(desired, actionKey);
        current.value = desired;
        }
      }); 
    
    return this;
  }

  @Override public KeyBuilder pressed(Supplier<Types.KeyStroke> stroke, Consumer<Ctx.Key> action) {
    return bind("pressed:" + stroke.get().k(), stroke, action);
    }
  @Override public KeyBuilder released(Supplier<Types.KeyStroke> stroke, Consumer<Ctx.Key> action) {
    return bind("released:" + stroke.get().k(), stroke, action);
    }
  }

record CMouseBuilder(CPanel panel) implements MouseBuilder {
  private Ctx.Mouse ctx(MouseEvent e) {
    return new CMouseCtx(
      panel.frame().elapsed(),
      new Types.Position(new Types.X(e.getX()), new Types.Y(e.getY())),
      panel.frame().screenSize(),
      panel.panelSize()
      );
    }

  private MouseBuilder mouse(MouseAdapter a) {
    panel.addMouseListener(a); return this;
    }
  private MouseBuilder motion(MouseMotionAdapter a) {
    panel.addMouseMotionListener(a); return this;
    }

  @Override public MouseBuilder clicked(Consumer<Ctx.Mouse> action) {
    return mouse(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        action.accept(ctx(e));
        }
      });
    }
  @Override public MouseBuilder pressed(Consumer<Ctx.Mouse> action) {
    return mouse(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        action.accept(ctx(e));
        }
      });
    }
  @Override public MouseBuilder released(Consumer<Ctx.Mouse> action) {
    return mouse(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        action.accept(ctx(e));
        }
      });
    }
  @Override public MouseBuilder moved(Consumer<Ctx.Mouse> action) {
    return motion(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        action.accept(ctx(e));
        }
      });
    }
  @Override public MouseBuilder dragged(Consumer<Ctx.Mouse> action) {
    return motion(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        action.accept(ctx(e));
        }
      });
    }
  @Override public MouseBuilder entered(Consumer<Ctx.Mouse> action) {
    return mouse(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        action.accept(ctx(e));
        }
      });
    }
  @Override public MouseBuilder exited(Consumer<Ctx.Mouse> action) {
    return mouse(new MouseAdapter() {
      public void mouseExited(MouseEvent e) {
        action.accept(ctx(e));
        }
      });
    }
  }

record CGraphicsCtx(
    Graphics2D g2d,
    Types.TimeNanos elapsed,
    Types.Dimension screenSize,
    Types.Dimension panelSize
) implements Ctx.Graphics {

  private static int x(Types.Position x) {
    return x.x().x();
    }
  private static int y(Types.Position y) {
    return y.y().y();
    }
  private static int w(Types.Dimension w) {
    return w.w().w();
    }
  private static int h(Types.Dimension h) {
    return h.h().h();
    }

  @Override public Ctx.Graphics rect(Types.Position pos, Types.Dimension dim) {
    g2d.fillRect(x(pos), y(pos), w(dim), h(dim)); return this;
    }
  @Override public Ctx.Graphics oval(Types.Position pos, Types.Dimension dim) {
    g2d.fillOval(x(pos), y(pos), w(dim), h(dim)); return this;
    }
  @Override public Ctx.Graphics line(Types.Position from, Types.Position to) {
    g2d.drawLine(x(from), y(from), x(to), y(to)); return this;
    }
  @Override public Ctx.Graphics color(Types.Color color) {
    g2d.setColor(Awt.color(color)); return this;
    }
  }

record CMouseCtx(
    Types.TimeNanos elapsed,
    Types.Position mousePosition,
    Types.Dimension screenSize,
    Types.Dimension panelSize
) implements Ctx.Mouse {}

record CKeyCtx(
    Types.TimeNanos elapsed,
    Types.Dimension screenSize,
    Types.Dimension panelSize,
    Types.KeyStroke keyStroke
) implements Ctx.Key {}

class CFrame extends JFrame {
  private Types.TimeNanos elapsed;
  private final Types.Dimension screenSize;
  private final List<Runnable> tickHooks = new ArrayList<>();

  CFrame(
      String title,
      Types.Dimension screenSize,
      CompletableFuture<RuntimeException> done,
      SerialQueue queue,
      Consumer<Types.FluentGUIResult> onResult,
      Timer t
  ) {
    super(title);
    this.screenSize = screenSize;
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        t.stop();
        onResult.accept(new Types.FluentGUIResult.Closed());
        queue.closeAndWait();
        done.complete(null);
        }
      });
    }

  void addTickHook(Runnable hook) {
    tickHooks.add(hook);
    }
  void tick(Types.TimeNanos elapsed) {
    this.elapsed = elapsed;
    tickHooks.forEach(Runnable::run);
    repaint();
    }

  Types.TimeNanos elapsed() {
    return elapsed;
    }
  Types.Dimension screenSize() {
    return screenSize;
    }
  }

class CPanel extends JPanel {
  private final CFrame frame;
  private final Scope<Ctx.Graphics> paintable;

  CPanel(Scope<Ctx.Graphics> paintable, CFrame frame) {
    this.paintable = paintable;
    this.frame = frame;
    }

  CFrame frame() {
    return frame;
    }
  Types.Dimension panelSize() {
    return new Types.Dimension(new Types.Width(getWidth()), new Types.Height(getHeight()));
    }

  @Override public void paintComponent(Graphics g) {
    super.paintComponent(g);
    paintable.run(new CGraphicsCtx(
      (Graphics2D) g, 
      frame.elapsed(),
      frame.screenSize(), 
      panelSize())
      );
    }
  }

record CButton(JButton b) implements Swing.Button {
  @Override public String text() {
    return b.getText();
    }
  @Override public void text(String text) {
    b.setText(text);
    }
  @Override public void foreground(Types.Color c) {
    b.setForeground(Awt.color(c));
    }
  @Override public void background(Types.Color c) {
    b.setBackground(Awt.color(c));
    }
  @Override public void opaque(boolean opaque) {
    b.setOpaque(opaque);
    }
  }

record CLabel(JLabel l) implements Swing.Label {
  @Override public String text() {
    return l.getText();
    }
  @Override public void text(String text) {
    l.setText(text);
    }
  @Override public void foreground(Types.Color c) {
    l.setForeground(Awt.color(c));
    }
  @Override public void background(Types.Color c) {
    l.setBackground(Awt.color(c));
    }
  @Override public void opaque(boolean opaque) {
    l.setOpaque(opaque);
    }
  }
