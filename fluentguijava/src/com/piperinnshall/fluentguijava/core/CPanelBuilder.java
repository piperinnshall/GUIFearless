package com.piperinnshall.fluentguijava.core;

import com.piperinnshall.fluentguijava.fearless.Ctx;
import com.piperinnshall.fluentguijava.fearless.MouseBuilder;
import com.piperinnshall.fluentguijava.fearless.PanelBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Slot;
import com.piperinnshall.fluentguijava.fearless.Swing;
import com.piperinnshall.fluentguijava.fearless.Types;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

abstract class APanelBuilder<T extends APanelBuilder<T>> {
  private boolean opaque                    = true;
  private Types.Dimension dimension         = new Types.Dimension(new Types.Width(100), new Types.Height(100));
  private Types.Color background            = new Types.Color(new Types.Red(0), new Types.Green(0), new Types.Blue(0));
  private Scope<Ctx.Graphics> paint         = Scope.nop();
  private Scope<MouseBuilder> mouseScope    = Scope.nop();
  protected List<FrameComponent> components = new ArrayList<>();

  abstract T self();
  abstract LayoutManager layout();

  CPanel buildPanel(CFrame frame, SerialQueue queue) {
    var panel = new CPanel(paint, frame);
    panel.setLayout(layout());
    panel.setPreferredSize(Awt.dimension(dimension));
    panel.setBackground(Awt.color(background));
    panel.setOpaque(opaque);
    mouseScope.run(new CMouseBuilder(panel));
    components.forEach(r -> r.accept(frame, panel, queue));
    return panel;
    }

  public T transparent() {
    this.opaque = false; return self();
    }
  public T size(Types.Dimension dimension) {
    this.dimension = dimension; return self();
    }
  public T background(Types.Color color) {
    this.background = color; return self();
    }
  public T paint(Scope<Ctx.Graphics> scope) {
    this.paint = scope; return self();
    }
  private T add(FrameComponent component) {
    components.add(component); return self();
    }
  public T onMouse(Scope<MouseBuilder> scope) {
    this.mouseScope = scope; return self();
    }

  public T flow(Scope<PanelBuilder.Flow> scope) {
    return add((frame, parent, queue) -> {
      var pb = new CPanelBuilderFlow();
      scope.run(pb);
      parent.add(pb.buildPanel(frame, queue));
      }); 
    }
  public T border(Scope<PanelBuilder.Border> scope) {
    return add((frame, parent, queue) -> {
      var pb = new CPanelBuilderBorder();
      scope.run(pb);
      parent.add(pb.buildPanel(frame, queue));
      });
    }
  public T button(String text, Runnable r, Slot<Swing.Button> s) {
    return add((_, parent, queue) -> {
      var jb = new JButton(text);
      jb.addActionListener(_ -> queue.submit(r));
      jb.setOpaque(true);
      s.fill(new CButton(jb));
      parent.add(jb);
      });
    }
  public T label(String text, Slot<Swing.Label> s) {
    return add((_, parent, _) -> {
      var jl = new JLabel(text);
      jl.setOpaque(true);
      s.fill(new CLabel(jl));
      parent.add(jl);
      });
    }
  }

class CPanelBuilderFlow extends APanelBuilder<CPanelBuilderFlow> 
  implements PanelBuilder.Flow {

  @Override protected CPanelBuilderFlow self() {
    return this;
    }
  @Override protected LayoutManager layout() {
    return new FlowLayout();
    }
  }

class CPanelBuilderBorder extends APanelBuilder<CPanelBuilderBorder>
    implements PanelBuilder.Border {

  @Override protected CPanelBuilderBorder self() {
    return this;
    }
  @Override protected LayoutManager layout() {
    return new BorderLayout();
    }

  private PanelBuilder.Border add(String constraint, Scope<PanelBuilder.Flow> scope) {
    components.add((frame, parent, queue) -> {
      var pb = new CPanelBuilderFlow();
      scope.run(pb);
      parent.add(pb.buildPanel(frame, queue), constraint);
      }); return self();
    }

  @Override public PanelBuilder.Border north(Scope<PanelBuilder.Flow> scope) {
    return add(BorderLayout.NORTH, scope);
    }
  @Override public PanelBuilder.Border south(Scope<PanelBuilder.Flow> scope) {
    return add(BorderLayout.SOUTH, scope);
    }
  @Override public PanelBuilder.Border east(Scope<PanelBuilder.Flow> scope) {
    return add(BorderLayout.EAST, scope);
    }
  @Override public PanelBuilder.Border west(Scope<PanelBuilder.Flow> scope) {
    return add(BorderLayout.WEST, scope);
    }
  @Override public PanelBuilder.Border center(Scope<PanelBuilder.Flow> scope) {
    return add(BorderLayout.CENTER, scope);
    }
}
