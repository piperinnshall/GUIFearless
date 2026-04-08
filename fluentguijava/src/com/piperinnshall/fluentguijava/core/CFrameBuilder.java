package com.piperinnshall.fluentguijava.core;

import com.piperinnshall.fluentguijava.fearless.FrameBuilder;
import com.piperinnshall.fluentguijava.fearless.KeyBuilder;
import com.piperinnshall.fluentguijava.fearless.MouseBuilder;
import com.piperinnshall.fluentguijava.fearless.PanelBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Types;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.Timer;

class CFrameBuilder implements FrameBuilder {
  private final Types.TimeNanos startTime                       = new Types.TimeNanos(System.nanoTime());
  private Types.FluentGUIResult result                          = new Types.FluentGUIResult.Unknown();
  private Types.Opacity opacity                                 = new Types.Opacity(1);
  private Types.Dimension screenSize                            = resolveScreenSize();
  private Types.Position location                               = null;
  private Scope<KeyBuilder> keyScope                            = Scope.nop();
  private Scope<MouseBuilder> mouseScope                        = Scope.nop();
  private List<BiConsumer<JComponent, SerialQueue>> components  = new ArrayList<>();

  private CFrame frame;

  void start(
      String title,
      int fps,
      boolean maximized,
      boolean resizable,
      boolean undecorated,
      CompletableFuture<RuntimeException> done
  ) {
    if (fps <= 0) {
      throw new IllegalArgumentException("fps must be > 0, got: " + fps);
      }

    var o = new Object() {
      Timer t = new Timer(
        Math.round(1000.0f / fps),
        _ -> this.f.tick(new Types.TimeNanos(System.nanoTime() - startTime.nanos()))
        );

      private Consumer<RuntimeException> c = e -> {
        this.f.dispatchEvent(new WindowEvent(this.f, WindowEvent.WINDOW_CLOSING));
        t.stop();
        result = new Types.FluentGUIResult.Crashed(e);
        done.complete(e);
        };

      volatile SerialQueue exe = new SerialQueue(c);
      CFrame f = new CFrame(title, screenSize, done, exe, r -> result = r, t);
      CPanel p = buildContentPane(f, exe);
      CPanel g = buildGlassPane(f);
      };

    o.f.setContentPane(o.p);
    o.f.setUndecorated(undecorated);
    o.f.setResizable(resizable);
    o.f.setOpacity(opacity.o());
    o.f.setFocusable(true);
    o.f.pack();
    o.f.setVisible(true);

    if (maximized) {
      maximize(o.f, screenSize);
      }

    if (location != null) { 
      o.f.setLocation(Awt.point(location)); 
      } 
    else { 
      o.f.setLocationRelativeTo(null); 
      }

    o.t.start();
    }

  private CPanel buildContentPane(CFrame frame, SerialQueue queue) {
    this.frame = frame;
    var panel = new CPanel(Scope.nop(), frame);
    panel.setLayout(new BorderLayout());
    panel.setOpaque(false);
    panel.setVisible(true);
    keyScope.run(new CKeyBuilder(panel));
    components.forEach(r -> r.accept(panel, queue));
    return panel;
    }
  private CPanel buildGlassPane(CFrame frame) {
    var panel = new CPanel(Scope.nop(), frame);
    panel.setOpaque(false);
    panel.setVisible(true);
    return panel;
    }

  private static Types.Dimension resolveScreenSize() {
    var b = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
      .getDefaultScreenDevice()
      .getDefaultConfiguration()
      .getBounds();
    return new Types.Dimension(new Types.Width(b.width), new Types.Height(b.height));
    }
  private void maximize(CFrame frame, Types.Dimension size) {
    frame.setSize(Awt.dimension(size));
    frame.setLocation(0, 0);
    }

  @Override public Types.FluentGUIResult result() {
    return result;
    }
  @Override public FrameBuilder location(Types.Position location) {
    this.location = location; return this;
    }
  @Override public FrameBuilder opacity(Types.Opacity opacity) {
    this.opacity = opacity; return this;
    }
  @Override public FrameBuilder onKey(Scope<KeyBuilder> scope) {
    this.keyScope = scope; return this;
    }
  @Override public FrameBuilder onMouse(Scope<MouseBuilder> scope) {
    this.mouseScope = scope; return this;
    }

  private FrameBuilder add(BiConsumer<JComponent, SerialQueue> component) {
    components.add(component); return this;
    }

  @Override public FrameBuilder flow(Scope<PanelBuilder.Flow> scope) {
    return add((parent, queue) -> {
      var pb = new CPanelBuilderFlow();
      scope.run(pb);
      parent.add(pb.buildPanel(frame, queue));
      });
    }
  @Override public FrameBuilder border(Scope<PanelBuilder.Border> scope) {
    return add((parent, queue) -> {
      var pb = new CPanelBuilderBorder();
      scope.run(pb);
      parent.add(pb.buildPanel(frame, queue));
      });
    }
  }
