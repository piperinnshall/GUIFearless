package com.piperinnshall.fluentguijava.fluentgraphics;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CompletableFuture;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

class GraphicsCap {
  void runGraphics(String title, GraphicsScope graphics) {
    var done = new CompletableFuture<RuntimeException>();
    var builder = new CGraphicsBuilder();
    graphics.run(builder);
    SwingUtilities.invokeLater(() -> builder.start(title, done));
    var tr = done.join();
    if (tr != null) {
      throw tr;
    }
  }
}

@FunctionalInterface
interface GraphicsScope {
  void run(GraphicsBuilder g);
}

@FunctionalInterface
interface Paintable {
  void paint(GraphicsCtx graphicsCtx, long elapsedNanos);
}


interface GraphicsBuilder {
  GraphicsBuilder fps(int fps);
  GraphicsBuilder background(int rgb);
  GraphicsBuilder background(int r, int g, int b);
  GraphicsBuilder size(int width, int height);
  GraphicsBuilder paintable(Paintable p);
}

interface GraphicsCtx {
  GraphicsCtx rect(Vec2 position, Vec2 dimension);
  GraphicsCtx oval(Vec2 position, Vec2 dimension);
}

class CGraphicsBuilder implements GraphicsBuilder {
  int fps = 60;
  Dimension dim = new Dimension(100, 100);
  Color col;
  Paintable paintable;

  public void start(String title, CompletableFuture<RuntimeException> done) {
    var panel = new FPanel(paintable);
    var frame = new FFrame(title, done);

    panel.setPreferredSize(dim);

    frame.add(panel);
    frame.setResizable(false);
    frame.setFocusable(true);
    frame.pack();
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
  }

  @Override public GraphicsBuilder fps(int fps) {
    this.fps = fps;
    return this;
  }
  @Override public GraphicsBuilder background(int rgb) {
    this.col = new Color(rgb);
    return this;
  }
  @Override public GraphicsBuilder background(int r, int g, int b) {
    this.col = new Color(r, g, b);
    return this;
  }
  @Override public GraphicsBuilder size(int width, int height) {
    this.dim = new Dimension(width, height);
    return this;
  }
  @Override public GraphicsBuilder paintable(Paintable paintable) {
    this.paintable = paintable;
    return this;
  }

}

record CGraphicsCtx(Graphics2D g) implements GraphicsCtx {
  @Override
  public GraphicsCtx rect(Vec2 pos, Vec2 dim) {
    Point p = Point.round(pos), d = Point.round(dim);
    g.fillRect(p.x(), p.y(), d.x(), d.y());
    return this;
  }
  @Override
  public GraphicsCtx oval(Vec2 pos, Vec2 dim) {
    Point p = Point.round(pos), d = Point.round(dim);
    g.fillOval(p.x(), p.y(), d.x(), d.y());
    return this;
  }
}


class FFrame extends JFrame {
  FFrame(String title, CompletableFuture<RuntimeException> done) {
    super(title);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        done.complete(null);
      }
    });
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}

class FPanel extends JPanel {
  private final long startTime = System.nanoTime();
  private final Paintable paintable;

  FPanel(Paintable paintable) {
    this.paintable = paintable;
  }

  @Override
  public void paintComponent(Graphics g) {
    var g2d = (Graphics2D) g;
    super.paintComponent(g2d);
    if (paintable != null) {
      paintable.paint(new CGraphicsCtx(g2d), System.nanoTime() - startTime);
    }
  }
}
