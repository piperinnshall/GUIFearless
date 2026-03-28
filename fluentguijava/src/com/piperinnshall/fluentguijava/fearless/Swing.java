package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.Text;

public interface Swing {
  interface Button {
    Text text();
    void text(Text text);

    void foreground(Types.Color c);
    void background(Types.Color c); 
    void opaque(boolean opaque);
  }
  interface Label {
    Text text();
    void text(Text text);

    void foreground(Types.Color c);
    void background(Types.Color c); 
    void opaque(boolean opaque);
  }
}
