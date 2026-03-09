package com.piperinnshall.fluentguijava.swing;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

class Timer {
  static TimerBuilder of(int fps) {
    return new CTimerBuilder(fps);
  }
}

interface Updatable { void update(float dt); }
interface Condition { boolean test(float dt); }
record ConditionalTask(Condition condition, Runnable task) {}

interface TimerBuilder {
  TimerBuilder raw(Updatable task);
  TimerBuilder edt(Runnable task);
  TimerBuilder edtPredicate(Condition condition, Runnable task);
  void start();
  void stop();
}

class CTimerBuilder implements TimerBuilder {
  private final int fps;
  private final List<Updatable> rawTasks = new ArrayList<>();
  private final List<Runnable> edtTasks = new ArrayList<>();
  private final List<ConditionalTask> conditionalEdtTasks = new ArrayList<>();
  private Thread t;

  CTimerBuilder(int fps) {
    this.fps = fps;
  }

  public TimerBuilder raw(Updatable task) {
    rawTasks.add(task);
    return this;
  }

  public TimerBuilder edt(Runnable task) {
    edtTasks.add(task);
    return this;
  }

  public TimerBuilder edtPredicate(Condition condition, Runnable task) {
    conditionalEdtTasks.add(new ConditionalTask(condition, task));
    return this;
  }

  public void start() {
    t = new Thread(() -> {
      long next = System.nanoTime();
      long last = next;
      for (;;) {
        next += 1_000_000_000L / fps;
        long now = System.nanoTime();
        float dt = (now - last) / 1_000_000_000f;
        last = now;
        rawTasks.forEach(t -> t.update(dt));
        SwingUtilities.invokeLater(() -> edtTasks.forEach(Runnable::run));
        conditionalEdtTasks.forEach(e -> {
          if (e.condition().test(dt))
            SwingUtilities.invokeLater(e.task());
        });
        long delay = next - System.nanoTime();
        if (delay > 0)
          try {
            Thread.sleep(Math.round(delay / 1_000_000.0));
          } catch (InterruptedException _) {
            break;
          }
      }
    });
    t.setDaemon(true);
    t.start();
  }

  public void stop() {
    t.interrupt();
  }
}
