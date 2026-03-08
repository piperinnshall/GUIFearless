package com.piperinnshall.fluentguijava.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

class Timer {
  static TimerBuilder of(int fps) {
    return new CTimerBuilder(fps);
  }
}

interface TimerBuilder {
  TimerBuilder raw(Runnable task);
  TimerBuilder edt(Runnable task);
  void start();
  void stop();
}

class CTimerBuilder implements TimerBuilder {
  private final int fps;
  private final List<Runnable> rawTasks = new ArrayList<>();
  private final List<Runnable> edtTasks = new ArrayList<>();
  private Thread t;

  CTimerBuilder(int fps) {
    this.fps = fps;
  }

  public TimerBuilder raw(Runnable task) {
    rawTasks.add(task);
    return this;
  }

  public TimerBuilder edt(Runnable task) {
    edtTasks.add(task);
    return this;
  }

  public void start() {
    t = new Thread(() -> {
      long next = System.nanoTime();
      for (;;) {
        next += 1_000_000_000L / fps;
        rawTasks.forEach(Runnable::run);
        SwingUtilities.invokeLater(() -> edtTasks.forEach(Runnable::run));
        long delay = next - System.nanoTime();
        if (delay > 0)
          try {
            Thread.sleep(Math.round(delay / 1_000_000.0));
          } catch (InterruptedException _) {
            break;
          }
      }
    });
    t.start();
  }

  public void stop() {
    t.interrupt();
  }
}
