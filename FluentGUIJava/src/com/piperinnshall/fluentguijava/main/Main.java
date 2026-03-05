package com.piperinnshall.fluentguijava.main;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// GuiCap is an object-capability for opening GUI windows.
// Holding a GuiCap is what grants the right to create a window.
// What you get back is narrowed.
class GuiCap {
  // Blocks the calling thread until the window is closed, then returns the
  // result.
  <R> R runGui(String title, GuiScope<R> gs) {
    var done = new CountDownLatch(1);
    var builder = new CGuiBuilder<R>();
    gs.run(builder);
    SwingUtilities.invokeLater(() -> builder.start(title, done));
    try {
      done.await();
    } catch (InterruptedException e) {
      throw new Error("Unreachable", e);
    }
    return builder.res();
  }
}

interface GuiScope<R> {
  void run(GuiBuilder<R> b);
}

// Slot is a write-once capability container.
// Passing a Slot to a builder grants it the right to fill it; holding a Slot
// grants the right to read from it.
class Slot<T> {
  private Optional<T> inner = Optional.empty();

  void fill(T t) {
    inner = Optional.of(t);
  }

  T get() {
    return inner.get();
  }
}

interface GuiBuilder<R> {
  GuiBuilder<R> res(R r);

  GuiBuilder<R> button(String text);

  GuiBuilder<R> button(String text, Runnable r);

  // The Slot is filled with a Button capability, giving the caller the right to
  // mutate that button later.
  GuiBuilder<R> button(String text, Runnable r, Slot<Button> s);
}

interface Button {
  String text(String text);

  String text();
}

record CButton(JButton b) implements Button {
  @Override
  public String text(String text) {
    b.setText(text);
    return text;
  }

  @Override
  public String text() {
    return b.getText();
  }
}

class CGuiBuilder<R> implements GuiBuilder<R> {
  R res;

  // Single-threaded executor ensures button event handlers run sequentially
  private final ThreadPoolExecutor exe = new ThreadPoolExecutor(
      1, 1, 0L, TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<>());

  // Tracks all submitted futures so we can cancel all but the last on close
  private final List<Future<?>> submitted = new ArrayList<>();

  // Deferred list of component-mounting operations, run when the frame is built.
  List<Consumer<JComponent>> rs = new ArrayList<>();

  Future<?> submit(Runnable r) {
    Future<?> f = exe.submit(r);
    submitted.add(f);
    return f;
  }

  public void start(String title, CountDownLatch done) {
    var f = new FearlessGui(title, done, exe, submitted);
    var myRoot = new JPanel();
    rs.forEach(cjc -> cjc.accept(myRoot));
    f.add(myRoot);
    f.setPreferredSize(new Dimension(300, 300));
    f.pack();
    f.setVisible(true);
  }

  public R res() {
    return res;
  }

  @Override
  public GuiBuilder<R> res(R r) {
    res = r;
    return this;
  }

  @Override
  public GuiBuilder<R> button(String text) {
    rs.add(parent -> parent.add(new JButton(text)));
    return this;
  }

  @Override
  public GuiBuilder<R> button(String text, Runnable r) {
    rs.add(parent -> {
      var b = new JButton(text);
      b.addActionListener(_ -> submit(r)); // fixed: was r.run(), bypassing exe
      parent.add(b);
    });
    return this;
  }

  @Override
  public GuiBuilder<R> button(String text, Runnable r, Slot<Button> s) {
    rs.add(parent -> {
      var b = new JButton(text);
      b.addActionListener(_ -> submit(r));
      parent.add(b);
      // Fill the slot with the Button capability so the caller can mutate this button
      // later.
      s.fill(new CButton(b));
    });
    return this;
  }
}

@SuppressWarnings("serial")
class FearlessGui extends JFrame {
  FearlessGui(String title, CountDownLatch done, ThreadPoolExecutor exe, List<Future<?>> submitted) {
    super(title);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        // Cancel all but the last submitted task
        for (int i = 0; i < submitted.size() - 1; i++) {
          submitted.get(i).cancel(false);
        }

        // Wait on the last one so the model is left in a consistent final state
        if (!submitted.isEmpty()) {
          try {
            submitted.getLast().get();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }

        exe.shutdown();
        // Release the calling thread that is blocked in GuiCap.
        done.countDown();
      }
    });
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}

/* MVC */
record Model(List<String> ss) {
  int iSeeYou(String s) {
    ss.add(s);
    return ss.size();
  }
}

record View(Model m, List<Slot<Button>> worldButtons) {
  void helloEvent() {
    m.iSeeYou("H");
  }

  void worldEvent() {
    var out = m.iSeeYou("W");
    worldButtons.get(0).get().text("" + out);
    worldButtons.get(1).get().text("" + out + 1);
  }

  int build() {
    var cap = new GuiCap();
    int five = cap.runGui("HelloWorld", b -> b
        .button("hello", this::helloEvent)
        .button("hello", this::helloEvent)
        .button("world", this::worldEvent, worldButtons.get(0))
        .button("world", this::worldEvent, worldButtons.get(1))
        .res(5));
    return five;
  }
}

public class Main {
  public static void main(String[] a) {
    var m = new Model(new ArrayList<>());
    System.out.print(new View(m, List.of(new Slot<Button>(), new Slot<Button>())).build());
    System.out.print(m);
  }
}
