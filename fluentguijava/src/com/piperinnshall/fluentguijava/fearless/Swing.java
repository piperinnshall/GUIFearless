package com.piperinnshall.fluentguijava.fearless;

public interface Swing {
  interface Button {
    String text();
    void text(String text);

    void foreground(Types.Color c);
    void background(Types.Color c); 
    void opaque(boolean opaque);
  }
  interface Label {
    String text();
    void text(String text);

    void foreground(Types.Color c);
    void background(Types.Color c); 
    void opaque(boolean opaque);
  }
}
