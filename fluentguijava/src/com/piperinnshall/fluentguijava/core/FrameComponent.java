package com.piperinnshall.fluentguijava.core;

import javax.swing.JComponent;

@FunctionalInterface
interface FrameComponent {
  void accept(CFrame frame, JComponent component, SerialQueue queue);
  }
