# Design

No exposing swing types?

3 sibling Windows

`JFrame JDialog JWindow`

Each are children of `awt.Window`.

In FluentGUI.java:

``` 
FluentWindow = Frame.of(); FluentWindow = Dialog.of(); FluentWindow = Window.of(); 
```

# Sequential buttons and closing

When do we close application? After 1 event or all queue is complete

When we click button one can it make button 2 need to be clicked if its just a
queue of event?

# GUI

- [x] Call paint on the Frame not the Panel! Test multiple panels
- [x] Turn null check off in keyScope and mouseScope with null object pattern.
- [x] Fix map to forEach
- [x] Fix screensize and elapsed in panel
- [x] Top level has rootPanel that repaints everything
- [x] Key and mouse top level.
- [x] Do panels focus when you click: NO.
- [x] Can click work wihtout requesting focus: YES
- [x] special layout panel that uses the frame layouts.
- [x] Cant move between frames: YES
- [ ] Resolve should just be a function that does something
- [ ] Maybe has a Ctx? The exit Ctx that tells us how we exited
- [ ] paintable.run operation internally
- [ ] Instead of having panel and seperate setlayout, you have layout components
- [ ] State machine compile time error for panels? (Borderlayout)
- [ ] Grid: Add and go into its rightful place. Error when we dont fill the layout.
- [ ] when super.paintComponent() do we do shapes on top or not. super before or after paint?. add a button and what happens?
- [ ] Use the absolute position layout that allows us to have stuff in positions.
- [ ] Recompute layout every time there is a resize: hopefully not every repaint.
- [ ] Is focus only keys? or mouse.
- [ ] Find a way to have in addition to the list of panels, where is the mouse in each panel.
- [ ] Global context list of rich panel can ask for mouse info, size of panel, know if mouse is in the panel by bound checks or implicit
- [ ] Any focusable thing like text area also triggers binds. try to expose the boolean choice.
- [ ] Do reset the focus to top level or not in text boxes or not, document clearly?
- [ ] Somehow we have a way that will press this button to cause the model event and the model can call gui.exit
- [ ] we will need a family of gui operations: exit resetFocus
- [ ] any text area will have .focus to aqcuire focus

- Step 1: Support label.text() via slots
- Step 2: Support text area.focus and .text and stuff
- Step 3: Somehow we need a label and text area that represent the whole
  frame.  

Panel with abs pos
3 elements each a panel
Transperant panels/elements

# TODO

- Design combinator interface generator for borderLayout
- Decide and document: super.paintComponent before or after paintable.run
- Layout manager on root panel so child panels are visible 
- Own panel and frame resolution correctly.
- Rebuild and swap - root scene recompute

- Fast version of lenSq

- What layouts do we want

# DONE

- Fix screenSize and elapsed owned by frame
- Fix map to forEach
- Call repaint on the frame via tick
- Null object pattern for keyScope, mouseScope, and paintable
- CFrameBuilder implements FrameBuilder and PanelBuilder via root CPanelBuilder
- FrameBuilder overrides PanelBuilder methods with covariant return to preserve
  chain
- Root panel set as content pane, child panels added into it
- maximize() uses resolveScreenSize, MAXIMIZED_BOTH dropped
- opacity only applied when undecorated
- undecorated and maximized are orthogonal 
- Separate public API between packages
- Scope unified to single generic Scope with nop() replacing names subtypes
- Fixed map to forEach in panel building
- Add border layout to frame and panel builder interface
- Refactor concrete so that CFrameBuilder extends CPanelBuilder
- Panels can nest arbitrarily deep
- Panels and frames are now separated by layout
- Layout builders on FluentGUI class
- Minimize duplication with self referential generic polymorphism tricks
- Use setContentPane: JFrame.add(comp) delegates to getContentPane().add(comp)
- Work out uses of float in vec2/3 
- Implement Color etc.
- Move key and mouse to top pane
- Move mouse back to all panels
- Live updating keybinds
- Position and Dimension interop with Vector and Scalar for transforms
- Properly pin SerialQueue to Frame
- Label and Button working

# DOCUMENTATION

- Size and location override maximized
- Not using java maximize, broken on MacOs
- opacity requires undecorated
- Every call replaces the last
- Mouse coords are per-panel

# Global Mouse Issues:

Screen of 100 pixels
2 panels, 50 px wide
vertically split
`drawSquare(0,0,10,10)`
clicking on the square returns: 50, 0.
So mouse needs to be per panel, while key presses are global.
