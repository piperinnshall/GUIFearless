# Design

No exposing swing types?

3 sibling Windows

```
JFrame
JDialog
JWindow
```

Each are children of `awt.Window`.

In FluentGUI.java:

```
FluentWindow = Frame.of();
FluentWindow = Dialog.of();
FluentWindow = Window.of();
```

# Sequential buttons and closing

when do we close application? after 1 event or all queue is complete

when we click button one can it make button 2 need to be clicked if its just a queue of event?
