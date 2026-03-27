package com.piperinnshall.fluentguijava.core;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import com.piperinnshall.fluentguijava.fearless.Types;
import com.piperinnshall.fluentguijava.fearless.Ctx;
import com.piperinnshall.fluentguijava.fearless.KeyBuilder;
import com.piperinnshall.fluentguijava.fearless.MouseBuilder;
import com.piperinnshall.fluentguijava.fearless.PanelBuilder;
import com.piperinnshall.fluentguijava.fearless.Scope;

abstract class CPanelBuilder<T extends CPanelBuilder<T>> {
  Types.Dimension dimension = new Types.Dimension(new Types.Width(100), new Types.Height(100));
  Types.Color color = new Types.Color(new Types.Red(0), new Types.Green(0), new Types.Blue(0));
  String constraint = BorderLayout.CENTER;
  Scope<Ctx.Graphics> paintable = Scope.nop();
  Scope<KeyBuilder> keyScope = Scope.nop();
  Scope<MouseBuilder> mouseScope = Scope.nop();
  List<CPanelBuilder<?>> children = new ArrayList<>();

  abstract T self();
  abstract CPanel buildPanel(CFrame frame);
  protected CPanel basePanel(CFrame frame) {
    var panel = new CPanel(paintable, frame);
    panel.setPreferredSize(Awt.dimension(dimension));
    panel.setBackground(Awt.color(color));
    keyScope.run(new CKeyBuilder(panel));
    mouseScope.run(new CMouseBuilder(panel));
    return panel;
  }

  public T size(Types.Dimension dimension) { this.dimension = dimension; return self(); }
  public T background(Types.Color color) { this.color = color; return self(); }
  public T paintable(Scope<Ctx.Graphics> scope) { this.paintable = scope; return self(); }
  public T onKey(Scope<KeyBuilder> scope) { this.keyScope = scope; return self(); }
  public T onMouse(Scope<MouseBuilder> scope) { this.mouseScope = scope; return self(); }
  public T flow(Scope<PanelBuilder.Flow> scope) { var pb = new CPanelBuilderFlow(); scope.run(pb); children.add(pb); return self(); }
  public T border(Scope<PanelBuilder.Border> scope) { var pb = new CPanelBuilderBorder(); scope.run(pb); children.add(pb); return self(); }
}

class CPanelBuilderFlow extends CPanelBuilder<CPanelBuilderFlow> implements PanelBuilder.Flow {
  @Override CPanel buildPanel(CFrame frame) {
    var panel = basePanel(frame);
    panel.setLayout(new FlowLayout());
    children.forEach(child -> panel.add(child.buildPanel(frame)));
    return panel;
  }
  @Override CPanelBuilderFlow self() { return this; }
}

class CPanelBuilderBorder extends CPanelBuilder<CPanelBuilderBorder> implements PanelBuilder.Border {
  @Override CPanel buildPanel(CFrame frame) {
    var panel = basePanel(frame);
    panel.setLayout(new BorderLayout());
    children.forEach(child -> panel.add(child.buildPanel(frame), child.constraint));
    return panel;
  }
  @Override CPanelBuilderBorder self() { return this; }
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
