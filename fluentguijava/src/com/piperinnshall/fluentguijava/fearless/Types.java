package com.piperinnshall.fluentguijava.fearless;

public interface Types {

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
  record Time(long nanos){}

  record Scalar(double s) {
    public Scalar { if (Double.isNaN(s) || Double.isInfinite(s)) throw new IllegalArgumentException("Scalar must be finite: " + s); }
    Scalar add(Scalar o) { return new Scalar(s + o.s); }
    Scalar sub(Scalar o) { return new Scalar(s - o.s); }
    Scalar mul(Scalar o) { return new Scalar(s * o.s); }
    Scalar div(Scalar o) { return new Scalar(s / o.s); }
    Scalar sqrt() { return new Scalar(Math.sqrt(s)); }
    public int toInt() { return (int) s; }
  }

  record Position(X x, Y y) { 
    public Position(Scalar x, Scalar y) { this(new X(x.toInt()), new Y(y.toInt())); } 
    public Position(Vector2 v) { this(v.x(), v.y()); } 
  }

  record Dimension(Width w, Height h) {
    public Dimension(Scalar x, Scalar y) { this(new Width(x.toInt()), new Height(y.toInt())); } 
    public Dimension(Vector2 v) { this(v.x(), v.y()); } 
  }

  public record LerpScalar(Scalar start, Scalar end, Time duration, Easing easing) {
    public Scalar at(long elapsedNanos) {
      float t = elapsedNanos / (duration.nanos() * 1_000_000_000f);
      t = Math.max(0f, Math.min(1f, t));
      return start.add(end.sub(start).mul(new Scalar(easing.apply(t))));
    }
  }

  public record LerpVector2(Vector2 start, Vector2 end, Time duration, Easing easing) {
    public Vector2 at(long elapsedNanos) {
      Scalar x = new LerpScalar(start.x(), end.x(), duration, easing).at(elapsedNanos);
      Scalar y = new LerpScalar(start.y(), end.y(), duration, easing).at(elapsedNanos);
      return new Vector2(x, y);
    }
  }

  public record LerpVector3(Vector3 start, Vector3 end, Time duration, Easing easing) {
    public Vector3 at(long elapsedNanos) {
      Scalar x = new LerpScalar(start.x(), end.x(), duration, easing).at(elapsedNanos);
      Scalar y = new LerpScalar(start.y(), end.y(), duration, easing).at(elapsedNanos);
      Scalar z = new LerpScalar(start.z(), end.z(), duration, easing).at(elapsedNanos);
      return new Vector3(x, y, z);
    }
  }

  record Vector2(Scalar x, Scalar y) {
    public Vector2(Scalar s) { this(s, s); }
    public Vector2 add(Vector2 o) { return new Vector2(x.add(o.x), y.add(o.y)); }
    public Vector2 sub(Vector2 o) { return new Vector2(x.sub(o.x), y.sub(o.y)); }
    public Vector2 add(Scalar s) { return new Vector2(x.add(s), y.add(s)); }
    public Vector2 sub(Scalar s) { return new Vector2(x.sub(s), y.sub(s)); }
    public Vector2 mul(Scalar s) { return new Vector2(x.mul(s), y.mul(s)); }
    public Vector2 div(Scalar s) { return new Vector2(x.div(s), y.div(s)); }
    public Scalar dot(Vector2 v) { return x.mul(v.x).add(y.mul(v.y)); }
    public Scalar lenSq() { return dot(this); }
    public Scalar len() { return lenSq().sqrt(); }
    public Vector2 normalize() { return div(len()); }
    public Scalar dist(Vector2 o) { return sub(o).len(); }
    public Scalar distSq(Vector2 o) { return sub(o).lenSq(); }
  }

  record Vector3(Scalar x, Scalar y, Scalar z) {
    public Vector3(Scalar s) { this(s, s, s); }
    public Vector3 add(Vector3 o) { return new Vector3(x.add(o.x), y.add(o.y), z.add(o.z)); }
    public Vector3 sub(Vector3 o) { return new Vector3(x.sub(o.x), y.sub(o.y), z.sub(o.z)); }
    public Vector3 add(Scalar s) { return new Vector3(x.add(s), y.add(s), z.add(s)); }
    public Vector3 sub(Scalar s) { return new Vector3(x.sub(s), y.sub(s), z.sub(s)); }
    public Vector3 mul(Scalar s) { return new Vector3(x.mul(s), y.mul(s), z.mul(s)); }
    public Vector3 div(Scalar s) { return new Vector3(x.div(s), y.div(s), z.div(s)); }
    public Scalar dot(Vector3 v) { return x.mul(v.x).add(y.mul(v.y)).add(z.mul(v.z)); }
    public Vector3 cross(Vector3 v) {
      return new Vector3(
          y.mul(v.z).sub(z.mul(v.y)),
          z.mul(v.x).sub(x.mul(v.z)),
          x.mul(v.y).sub(y.mul(v.x))
          );
    }
    public Scalar lenSq() { return dot(this); }
    public Scalar len() { return lenSq().sqrt(); }
    public Vector3 normalize() { return div(len()); }
    public Scalar dist(Vector3 o) { return sub(o).len(); }
    public Scalar distSq(Vector3 o) { return sub(o).lenSq(); }
  }

  record Color(Red r, Green g, Blue b, Alpha a) {
    public Color(Red r, Green g, Blue b) { this(r, g, b, new Alpha(255)); }
    public Color(Hue h, Saturation s, Value v) { this(fromHSV(h, s, v)); }
    private Color(Color c) { this(c.r, c.g, c.b, c.a); }
    private static Color fromHSV(Hue h, Saturation s, Value v) {
      if (s.s() == 0) {
        int c = (int) v.v() * 255;
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

}
