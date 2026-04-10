#import "@preview/charged-ieee:0.1.4": ieee

#import "@preview/booktabs:0.0.4": *
#import "@preview/codly:1.3.0": *
#import "@preview/codly-languages:0.1.1": *
#import "@preview/lilaq:0.6.0" as lq
#import "@preview/lovelace:0.3.1": *

#show: ieee.with(
  title: [Assignment 1: Fluent GUI Java Library],
  authors: (
    (
      name: "Piper Inns Hall",
      email: "innshpipe@myvuw.ac.nz"
    ),
    (
      name: "Supervisor: Dr Marco Servetto",
      email: "marco.servetto@vuw.ac.nz"
    ),
  ),
  figure-supplement: [Fig.],
)

#show: codly-init.with()
#codly(
  languages: codly-languages,
  zebra-fill: none,
  stroke: none,
  display-name: false,
  lang-stroke: none,
  lang-fill: (lang) => white,
)

= Design and Implementation

== Package Separation

A core design principle of this library is the strict separation of
Swing from the public API. The `fearless/` package contains only
interfaces and pure data records with no Swing imports, meaning
user code has zero dependencies on Swing. The `core/` package
contains the concrete Swing implementation. Because the public API
depends only on interfaces and records, the Swing-backed
implementation can be replaced without changing user-facing code.

== Threading

Swing runs on the Event Dispatch Thread. User actions are
intentionally kept off of the EDT, while only Swing rendering
executes on it. Button callbacks and other user actions are
dispatched off the EDT onto a dedicated `SerialQueue` thread, a
custom single-threaded task queue for serial execution. This keeps
user logic separate from rendering. The queue drains cleanly on
close via a "poison pill" argument, which is used as a shutdown
signal, and unhandled exceptions propagate back through the GUI
application as `FluentGUIResult.Crashed`.

== Scope and Slot

`Scope<B>` is a functional interface (`void run(B builder)`) used
as the lambda entry point for every builder. It has a `nop()` null
object pattern method, so configuration fields that are optional,
such as `onKey` and `onMouse`, need no null checks.

`Slot<T>` is a write-once reference that lets users capture a
handle to a widget during building and mutate it later. For
example, a `Slot` can be used to update a button's label in
response to an event or change the color of the button. Rather than
returning the widget directly from the builder, which would make it
freely accessible, the caller must explicitly provide a `Slot` to
receive the handle. This ensures mutable references are only
reachable by code that was intentionally given them, which is a
requirement for the object capability model that Fearless uses in
Part 2.

`Scope` and `Slot` are the core of how the fluent API actually
works. They are what a user encounters first and most often.

= Guide

FluentGUI is configured by passing lambdas to builder objects.
Every builder method returns the builder itself, enabling fluent
chained calls. The entry point is `FluentGUI#run`, which takes a
`Scope<FrameBuilder>`, blocks the calling thread until the window
closes, and returns a `FluentGUIResult`.

#figure(
  placement: none,
  caption: [Minimal valid FluentGUI program],
)[
```java
public class ... {
  public static void main(...) {
    FluentGUIResult result = new FluentGUI().run(_ -> {});
    }
  }
```
]

Discarding the builder with `_` produces a window with all
defaults: no title, 60 fps, and not resizable. The returned
`FluentGUIResult` is a sealed type with three cases: `Closed`,
`Unknown`, and `Crashed(var cause)`, handled with a `switch`
expression.

== Configuring the frame

`FrameBuilder` exposes methods for window properties. All calls are
optional, and the example below shows every available option.

#figure(
  placement: none,
  caption: [Frame configuration],
)[
```java
new FluentGUI().run(frame -> frame
  .title("My App")
  .fps(new FPS(60))
  .maximized()
  .location(new X(10), new Y(10))
  .undecorated(new Opacity(0.8f))
  .resizable()
);
```
]

Note that `.resizable()` has no effect when `.undecorated(...)` is
also set, because an undecorated frame cannot be resized by the
user.

== Adding a panel and components

A panel is added via `.flow(...)`, which takes a
`Scope<PanelBuilder>`. Inside that scope you can add buttons,
labels, and a custom paint callback. The API also provides other
panel layout and event options, so this example shows only a small
subset of what is available.

#figure(
  placement: none,
  caption: [Panel with a button and a label],
)[
```java
new FluentGUI().run(frame -> frame
  .title("Hello")
  .flow(panel -> panel
    .background(new Color(new Red(30), new Green(30), new Blue(30)))
    .size(new Dimension(new Width(400), new Height(400)))
    .button("Click me", () -> System.out.println("clicked"), Slot.of())
    .label("Hello World!", Slot.of())
  )
);
```
]

`Slot.of()` is used here as a discard, because the caller does not
need a handle to these components. When a handle is needed, a named
`Slot` is passed instead, as shown in @slot-section.

== Capturing Component Handles with Slots <slot-section>

`Slot<T>` is a write-once reference populated by the builder.
Passing a named slot to a builder method captures the widget so it
can be mutated later from callback code.

#figure(
  placement: none,
  caption: [Using a Slot to update a button's label],
)[
```java
Slot<Swing.Button> btn = Slot.of();
var toggled = new boolean[]{false};

new FluentGUI().run(frame -> frame
  .flow(panel -> panel
    .button("Off", () -> {
        toggled[0] = !toggled[0];
        btn.get().text(toggled[0] ? "On" : "Off");
    }, btn)
  )
);
```
]

Only code that was explicitly handed the `Slot` can reach the
widget. This is intentional because it enforces the
object-capability discipline that Part 2 requires.

== Keyboard input

Key bindings are registered with `.onKey(...)` on the frame
builder. Each binding is a `pressed` call that takes a `KeyStroke`
supplier and a callback. Using a supplier rather than a plain value
allows the binding to follow a mutable key at runtime.

#figure(
  placement: none,
  caption: [Rebindable key input],
)[
```java
KeyStroke upKey   = new KeyStroke("UP");
KeyStroke downKey = new KeyStroke("DOWN");

new FluentGUI().run(frame -> frame
  .onKey(k -> k
    .pressed(() -> upKey,   _ -> moveUp())
    .pressed(() -> downKey, _ -> moveDown())
  )
  .flow(panel -> panel
    .button("Swap to WASD", () -> {
        upKey   = new KeyStroke("W");
        downKey = new KeyStroke("S");
    }, Slot.of())
  )
);
```
]

== Custom painting

The `.paint(...)` scope on a panel receives a `PaintContext` that
exposes the current panel dimensions, a colour setter, a position
setter, and shape drawing methods. All values are in logical
pixels. More options are available through the API, but this
example focuses on the basic drawing flow.

#figure(
  placement: none,
  caption: [Drawing a centred oval that moves with a field variable],
)[
```java
float ovalY = 0.5f; // normalised 0–1

panel.paint(ctx -> {
    Width  w      = ctx.panelSize().w();
    Height h      = ctx.panelSize().h();
    double size   = Math.min(w.w(), h.h()) * 0.20;
    int    radius = (int) (size * 0.5);
    int    minY   = radius;
    int    maxY   = h.h() - radius;
    int    x      = (int) (w.w() * 0.5 - size * 0.5);
    int    y      = (int) (minY + (maxY - minY) * ovalY - radius);

    ctx.color(new Color(new Red(100), new Green(200), new Blue(255)))
       .position(new X(x), new Y(y))
       .oval(new Dimension(new Width((int) size), new Height((int) size)));
});
```
]

The paint callback is invoked on every frame. Reading `ovalY`
directly means any code that mutates the field, such as a key
callback, will be reflected on the next repaint with no additional
wiring.

== Handling the result

`FluentGUIResult` is a sealed interface. Switching over it gives
exhaustive case coverage at compile time.

#figure(
  placement: none,
  caption: [Handling the exit result],
)[
```java
FluentGUIResult result = new FluentGUI().run(/*...*/);

System.out.println(switch (result) {
    case Unknown() -> "Closed for unknown reason";
    case Closed() -> "User closed the window";
    case Crashed(var cause) -> cause;
});
```
]

A callback that throws an unchecked exception causes `run` to
return `Crashed(cause)` rather than propagating the exception up
the call stack. This means user code can deliberately crash, for
example during development, and the main program remains in
control.

= Documentation

== Transparent panels

Calling `.transparent()` on a panel makes its background
transparent. After that, any `.background(...)` call has no visible
effect because the backing Swing component no longer paints its
background.

== Size and location override maximized

If `.size(...)` or `.location(...)` is called on a frame that has
also had `.maximized()` set, the explicit size and location take
precedence. The window opens at the specified dimensions and
position rather than filling the screen.

== Maximized is not native

`.maximized()` does not delegate to the platform’s native maximise
mechanism. This is intentional because Java’s native maximise
behaviour is broken on macOS and produces incorrect window bounds.
The library instead sets the frame size to match the screen
dimensions directly.

== Opacity requires an undecorated frame

`Opacity` is only respected when `.undecorated(...)` has been set
on the frame. Passing an `Opacity` value to a decorated frame has
no effect because the platform compositor does not permit per-pixel
alpha on frames that include a title bar and border.

== Every configuration call replaces the last

Builder methods are not additive. Each call to `.title(...)`,
`.fps(...)`, `.background(...)`, and so on replaces the value set
by any previous call to the same method. Only the final call in the
chain takes effect.

== Mouse coordinates are per-panel

The `X` and `Y` values supplied to a mouse callback are relative to
the top-left corner of the panel that received the event, not to
the frame or the screen. When working with multiple panels,
coordinates must be interpreted in each panel’s own local space.

== `FluentGUI#run` blocks and returns a result

`FluentGUI#run` blocks the calling thread for the lifetime of the
window. When the window is closed, control returns to the caller
with a `FluentGUIResult`. The three possible values are `Closed`,
`Unknown`, and `Crashed(var cause)`, where `cause` is the message
of the unhandled exception that terminated the application.
