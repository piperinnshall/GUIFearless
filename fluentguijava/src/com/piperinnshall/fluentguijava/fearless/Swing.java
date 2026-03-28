package com.piperinnshall.fluentguijava.fearless;

import com.piperinnshall.fluentguijava.fearless.Types.Text;

public interface Swing {
  interface Button {
    Text text();
    void text(Text text);
  }
}
