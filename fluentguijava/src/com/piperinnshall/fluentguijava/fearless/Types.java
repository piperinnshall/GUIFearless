package com.piperinnshall.fluentguijava.fearless;

public interface Types {
  sealed interface FluentGUIResult {
    record Unknown() implements FluentGUIResult {}
    record Closed() implements FluentGUIResult {}
    record Crashed(RuntimeException cause) implements FluentGUIResult {}
    }

  record FPS(int fps) { public FPS { if (fps < 0) throw new IllegalArgumentException("FPS must be non-negative: " + fps); } }

  record X(int x) {}
  record Y(int y) {}

  record Width(int w) { public Width { if (w < 0) throw new IllegalArgumentException("Width must be non-negative: " + w); } }
  record Height(int h) { public Height { if (h < 0) throw new IllegalArgumentException("Height must be non-negative: " + h); } }

  record Red(int r) { public Red { if (r < 0 || r > 255) throw new IllegalArgumentException("Red must be 0-255: " + r); } }
  record Green(int g) { public Green { if (g < 0 || g > 255) throw new IllegalArgumentException("Green must be 0-255: " + g); } }
  record Blue(int b) { public Blue { if (b < 0 || b > 255) throw new IllegalArgumentException("Blue must be 0-255: " + b); } }
  record Alpha(int a) { public Alpha { if (a < 0 || a > 255) throw new IllegalArgumentException("Alpha must be 0-255: " + a); } }

  record Hue(double h) { public Hue { if (h < 0.0 || h > 1.0) throw new IllegalArgumentException("Hue must be 0.0-1.0: " + h); } }
  record Saturation(double s) { public Saturation { if (s < 0.0 || s > 1.0) throw new IllegalArgumentException("Saturation must be 0.0-1.0: " + s); } }
  record Value(double v) { public Value { if (v < 0.0 || v > 1.0) throw new IllegalArgumentException("Value must be 0.0-1.0: " + v); } }

  record Opacity(float o) { public Opacity { if (o < 0.0 || o > 1.0) throw new IllegalArgumentException("Opacity must be 0.0-1.0: " + o); } }
  record KeyStroke(String k){}
  record TimeNanos(long nanos){}
  record TimeSeconds(long seconds){}

  record Dimension(Width w, Height h) {
    public Dimension(Vector2 v) { this(new Width((int) v.x()), new Height((int) v.y())); }
    public Vector2 toVector2() { return new Vector2(w.w(), h.h()); }
    }

  record Color(Red r, Green g, Blue b, Alpha a) {
    public Color(Red r, Green g, Blue b) { this(r, g, b, new Alpha(255)); }
    public Color(Hue h, Saturation s, Value v) { this(fromHSV(h, s, v)); }
    private Color(Color c) { this(c.r, c.g, c.b, c.a); }
    private static Color fromHSV(Hue h, Saturation s, Value v) {
      if (s.s() == 0) {
        int c = (int) (v.v() * 255);
        return new Color(new Red(c), new Green(c), new Blue(c));
        }

      double hh = (h.h() - Math.floor(h.h())) * 6.0;
      double f = hh - Math.floor(hh);
      double p = v.v() * (1.0 - s.s());
      double q = v.v() * (1.0 - s.s() * f);
      double t = v.v() * (1.0 - s.s() * (1.0 - f));

      return switch ((int) hh) { case 0 -> new Color(new Red((int) (v.v() * 255 + 0.5)), new Green((int) (t * 255 + 0.5)), new Blue((int) (p * 255 + 0.5)));
        case 1 -> new Color(new Red((int) (q * 255 + 0.5)), new Green((int) (v.v() * 255 + 0.5)), new Blue((int) (p * 255 + 0.5))); 
        case 2 -> new Color(new Red((int) (p * 255 + 0.5)), new Green((int) (v.v() * 255 + 0.5)), new Blue((int) (t * 255 + 0.5)));
        case 3 -> new Color(new Red((int) (p * 255 + 0.5)), new Green((int) (q * 255 + 0.5)), new Blue((int) (v.v() * 255 + 0.5)));
        case 4 -> new Color(new Red((int) (t * 255 + 0.5)), new Green((int) (p * 255 + 0.5)), new Blue((int) (v.v() * 255 + 0.5)));
        default -> new Color(new Red((int) (v.v() * 255 + 0.5)), new Green((int) (p * 255 + 0.5)), new Blue((int) (q * 255 + 0.5)));
        };
      }
    }

  public record Lerpdouble(double start, double end, TimeSeconds duration, Easing easing) {
    public double at(TimeNanos elapsed) {
      float t = elapsed.nanos() / (duration.seconds() * 1_000_000_000f);
      t = Math.max(0f, Math.min(1f, t));
      return start + (end - start * easing.apply(t));
      }
    }

  public record LerpVector2(Vector2 start, Vector2 end, TimeSeconds duration, Easing easing) {
    public Vector2 at(TimeNanos elapsed) {
      double x = new Lerpdouble(start.x(), end.x(), duration, easing).at(elapsed);
      double y = new Lerpdouble(start.y(), end.y(), duration, easing).at(elapsed);
      return new Vector2(x, y);
      }
    }

  public record LerpVector3(Vector3 start, Vector3 end, TimeSeconds duration, Easing easing) {
    public Vector3 at(TimeNanos elapsed) {
      double x = new Lerpdouble(start.x(), end.x(), duration, easing).at(elapsed);
      double y = new Lerpdouble(start.y(), end.y(), duration, easing).at(elapsed);
      double z = new Lerpdouble(start.z(), end.z(), duration, easing).at(elapsed);
      return new Vector3(x, y, z);
      }
    }

  record Vector2(double x, double y) {
    public Vector2(double s) { this(s, s); }
    public Vector2 add(Vector2 o) { return new Vector2(x + o.x, y + o.y); }
    public Vector2 sub(Vector2 o) { return new Vector2(x - o.x, y - o.y); }
    public Vector2 add(double s) { return new Vector2(x + s, y + s); }
    public Vector2 sub(double s) { return new Vector2(x - s, y - s); }
    public Vector2 mul(double s) { return new Vector2(x * s, y * s); }
    public Vector2 div(double s) { return new Vector2(x / s, y / s); }
    public Vector2 normalize() { return div(len()); }
    public double dot(Vector2 v) { return x * v.x + y * v.y; }
    public double lenSq() { return dot(this); }
    public double len() { return Math.sqrt(lenSq()); }
    public double dist(Vector2 o) { return sub(o).len(); }
    public double distSq(Vector2 o) { return sub(o).lenSq(); }
    }

  public record Vector3(double x, double y, double z) {
    public Vector3(double s) { this(s, s, s); }
    public Vector3 add(Vector3 o) { return new Vector3(x + o.x, y + o.y, z + o.z); }
    public Vector3 sub(Vector3 o) { return new Vector3(x - o.x, y - o.y, z - o.z); }
    public Vector3 add(double s) { return new Vector3(x + s, y + s, z + s); }
    public Vector3 sub(double s) { return new Vector3(x - s, y - s, z - s); }
    public Vector3 mul(double s) { return new Vector3(x * s, y * s, z * s); }
    public Vector3 div(double s) { return new Vector3(x / s, y / s, z / s); }
    public double dot(Vector3 v) { return x * v.x + y * v.y + z * v.z; }
    public Vector3 cross(Vector3 v) {
      return new Vector3(
        y * v.z - z * v.y,
        z * v.x - x * v.z,
        x * v.y - y * v.x
        );
      }
    public double lenSq() { return dot(this); }
    public double len() { return Math.sqrt(lenSq()); }
    public Vector3 normalize() { return div(len()); }
    public double dist(Vector3 o) { return sub(o).len(); }
    public double distSq(Vector3 o) { return sub(o).lenSq(); }
    }
  }
