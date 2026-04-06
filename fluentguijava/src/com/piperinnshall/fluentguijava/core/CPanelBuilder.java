package com.piperinnshall.fluentguijava.core;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.piperinnshall.fluentguijava.fearless.Types;
import com.piperinnshall.fluentguijava.fearless.Ctx;
import com.piperinnshall.fluentguijava.fearless.PanelBuilder;
import com.piperinnshall.fluentguijava.fearless.MouseBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;
import com.piperinnshall.fluentguijava.fearless.Slot;
import com.piperinnshall.fluentguijava.fearless.Swing;
import java.util.function.BiConsumer;

abstract class APanelBuilder<T extends APanelBuilder<T>> {
  CFrame frame;
  Types.Dimension dimension = new Types.Dimension(new Types.Width(100), new Types.Height(100));
  Types.Color color = new Types.Color(new Types.Red(0), new Types.Green(0), new Types.Blue(0));
  Scope<Ctx.Graphics> paint = Scope.nop();
  Scope<MouseBuilder> mouseScope = Scope.nop();
  List<BiConsumer<JComponent, SerialQueue>> components = new ArrayList<>();

  abstract T self();
  abstract LayoutManager layout();

  CPanel buildPanel(CFrame frame, SerialQueue queue) {
    this.frame = frame;
    var panel = new CPanel(paint, frame);
    panel.setPreferredSize(Awt.dimension(dimension));
    panel.setBackground(Awt.color(color));
    panel.setLayout(layout());
    mouseScope.run(new CMouseBuilder(panel));
    components.forEach(r -> r.accept(panel, queue));
    return panel;
  }

  public T size(Types.Dimension dimension) { this.dimension = dimension; return self(); }
  public T background(Types.Color color) { this.color = color; return self(); }
  public T paint(Scope<Ctx.Graphics> scope) { this.paint = scope; return self(); }
  public T onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return self(); }

  public T flow(Scope<PanelBuilder.Flow> scope) {
    components.add((parent, queue) -> {
      var pb = new CPanelBuilderFlow();
      scope.run(pb);
      parent.add(pb.buildPanel(frame, queue));
    });
    return self();
  }

  public T border(Scope<PanelBuilder.Border> scope) {
    components.add((parent, queue) -> {
      var pb = new CPanelBuilderBorder();
      scope.run(pb);
      parent.add(pb.buildPanel(frame, queue));
    });
    return self();
  }

  public T button(String text, Runnable r, Slot<Swing.Button> s) {
    components.add((parent, queue) -> {
      var jb = new JButton(text);
      jb.addActionListener(_ -> queue.submit(r));
      jb.setOpaque(true);
      s.fill(new CButton(jb));
      parent.add(jb);
    });
    return self();
  }

  public T label(String text, Slot<Swing.Label> s) {
    components.add((parent, _) -> {
      var jl = new JLabel(text);
      jl.setOpaque(true);
      s.fill(new CLabel(jl));
      parent.add(jl);
    });
    return self();
  }
}

class CPanelBuilderFlow extends APanelBuilder<CPanelBuilderFlow> implements PanelBuilder.Flow {
  @Override LayoutManager layout() { return new FlowLayout(); }
  @Override CPanelBuilderFlow self() { return this; }
  }

class CPanelBuilderBorder extends APanelBuilder<CPanelBuilderBorder> implements PanelBuilder.Border {
  private void add(String constraint, Scope<PanelBuilder.Flow> scope) {
    components.add((parent, queue) -> {
      var pb = new CPanelBuilderFlow();
      scope.run(pb);
      parent.add(pb.buildPanel(frame, queue), constraint);
    });
  }
  @Override LayoutManager layout() { return new BorderLayout(); }
  @Override CPanelBuilderBorder self() { return this; }
  @Override public PanelBuilder.Border north(Scope<PanelBuilder.Flow> scope) { add(BorderLayout.NORTH, scope); return this; }
  @Override public PanelBuilder.Border south(Scope<PanelBuilder.Flow> scope) { add(BorderLayout.SOUTH, scope); return this; }
  @Override public PanelBuilder.Border east(Scope<PanelBuilder.Flow> scope) { add(BorderLayout.EAST, scope); return this; }
  @Override public PanelBuilder.Border west(Scope<PanelBuilder.Flow> scope) { add(BorderLayout.WEST, scope); return this; }
  @Override public PanelBuilder.Border center(Scope<PanelBuilder.Flow> scope) { add(BorderLayout.CENTER, scope); return this; } 
  }
