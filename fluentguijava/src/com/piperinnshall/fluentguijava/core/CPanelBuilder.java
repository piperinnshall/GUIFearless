package com.piperinnshall.fluentguijava.core;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
  Types.Dimension dimension = new Types.Dimension(new Types.Width(100), new Types.Height(100));
  Types.Color color = new Types.Color(new Types.Red(0), new Types.Green(0), new Types.Blue(0));
  String constraint = BorderLayout.CENTER;
  Scope<Ctx.Graphics> paint = Scope.nop();
  Scope<MouseBuilder> mouseScope = Scope.nop();
  List<APanelBuilder<?>> children = new ArrayList<>();
  List<BiConsumer<JComponent, SerialQueue>> components = new ArrayList<>();

  abstract T self();
  abstract LayoutManager layout();

  CPanel buildPanel(CFrame frame, SerialQueue queue) {
    var panel = new CPanel(paint, frame);
    panel.setPreferredSize(Awt.dimension(dimension));
    panel.setBackground(Awt.color(color));
    panel.setLayout(layout());
    mouseScope.run(new CMouseBuilder(panel));
    components.forEach(r -> r.accept(panel, queue));
    addChildren(panel, frame, queue);
    return panel;
  }
  protected void addChildren(CPanel panel, CFrame frame, SerialQueue queue) {
    children.forEach(child -> panel.add(child.buildPanel(frame, queue)));
  }
  public T size(Types.Dimension dimension) { this.dimension = dimension; return self(); }
  public T background(Types.Color color) { this.color = color; return self(); }
  public T paint(Scope<Ctx.Graphics> scope) { this.paint = scope; return self(); }
  public T flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return self(); }
  public T border(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); scope.run(pb); children.add(pb); return self(); }
  public T onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return self(); }

  public T button(Types.Text text, Runnable r, Slot<Swing.Button> s) {
    components.add((parent, queue) -> {
      var jb = new JButton(text.t());
      jb.addActionListener(_ -> queue.submit(r));
      jb.setOpaque(true);
      s.fill(new CButton(jb));
      parent.add(jb);
    });
    return self();
  }

  public T label(Types.Text text, Slot<Swing.Label> s) {
    components.add((parent, _) -> {
      var jl = new JLabel(text.t());
      jl.setOpaque(true);
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
  @Override LayoutManager layout() { return new BorderLayout(); }
  @Override CPanelBuilderBorder self() { return this; }
  @Override protected void addChildren(CPanel panel, CFrame frame, SerialQueue queue) {
    children.forEach(child -> panel.add(child.buildPanel(frame, queue), child.constraint));
  }
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
