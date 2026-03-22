# Design

No exposing swing types?

3 sibling Windows

``` JFrame JDialog JWindow ```

Each are children of `awt.Window`.

In FluentGUI.java:

``` FluentWindow = Frame.of(); FluentWindow = Dialog.of(); FluentWindow =
Window.of(); 
```

# Sequential buttons and closing

When do we close application? After 1 event or all queue is complete

When we click button one can it make button 2 need to be clicked if its just a
queue of event?

# GUI

Call paint on the Frame not the Panel! Test multiple panels

Turn null check off in keyScope and mouseScope with null object pattern.

Cant move between frames

Resolve should just be a function that does something 

Maybe has a Ctx? The exit Ctx that tells us how we exited

Fix map to forEach

Fix screensize and elapsed in panel

paintable.run operation internally

Instead of having panel and seperate setlayout, you have layout components

Top level has rootPanel that repaints everything

special layout panel that uses the frame layouts.

State machine compile time error for panels? (Borderlayout)

Grid: Add and go into its rightful place. Error when we dont fill the layout.

when super.paintComponent() do we do shapes on top or not. super before or
after paint?. add a button and what happens?

Use the absolute position layout that allows us to have stuff in positions.

Recompute layout every time there is a resize: hopefully not every repaint.

Is focus only keys? or mouse.

Do panels focus when you click.

Can click work wihtout requesting focus?

Key and mouse top level.

Find a way to have in addition to the list of panels, where is the mouse in
each panel.


Global context list of rich panel can ask for mouse info, size of panel, know
if mouse is in the panel by bound checks or implicit

Any focusable thing like text area also triggers binds. try to expose the
boolean choice. 

Do reset the focus to top level or not in text boxes or not, document clearly?

Somehow we have a way that will press this button to cause the model event and
the model can call gui.exit

we will need a family of gui operations:
exit
resetFocus

any text area will have .focus to aqcuire focus


Step 1: Support label.text() via slots
Step 2: Support text area.focus and .text and stuff
Step 3: Somehow we need a label  and text area that represent the whole frame.

# TODO
- Decide and document: super.paintComponent before or after paintable.run

# DONE
- Fix screenSize and elapsed owned by frame
- Fix map to forEach
- Call repaint on the frame via tick
- Null object pattern for keyScope, mouseScope, and paintable
- CFrameBuilder implements FrameBuilder and PanelBuilder via root CPanelBuilder
- FrameBuilder overrides PanelBuilder methods with covariant return to preserve chain
- Root panel set as content pane, child panels added into it
