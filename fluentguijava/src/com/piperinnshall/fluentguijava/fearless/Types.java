package com.piperinnshall.fluentguijava.fearless;
public interface Types {
  record X(int x){}
  record Y(int y){}

  record Width(int w){}
  record Height(int h){}
  record Red(int r) {}
  record Green(int g) {}
  record Blue(int b) {}
  record Alpha(int a) {}
  record Hue(int h) {}
  record Saturation(int s) {}
  record Value(int v) {}

  record Scalar(double s) {
    Scalar add(Scalar o) { return new Scalar(s + o.s); }
    Scalar sub(Scalar o) { return new Scalar(s - o.s); }
    Scalar mul(Scalar o) { return new Scalar(s * o.s); }
    Scalar div(Scalar o) { return new Scalar(s / o.s); }
    Scalar sqrt() { return new Scalar(Math.sqrt(s)); }
  }

  record Position(X x, Y y) {}
  record Dimension(Width width, Height height) {}

  interface Vector<T extends Vector<T>> {
    T self();
    T add(T point);
    T sub(T point);
    T add(Scalar scalar);
    T sub(Scalar scalar);
    T div(Scalar scalar);
    T mul(Scalar scalar);
    Scalar dot(T v);
    default T normalize()          { return div(len()); }
    default Scalar len()           { return lenSq().sqrt(); }
    default Scalar lenSq()         { return dot(self()); }
    default Scalar dist(T other)   { return sub(other).len(); }
    default Scalar distSq(T other) { return sub(other).lenSq(); }
  }
  record Vector2(Scalar x, Scalar y) implements Vector<Vector2> {
    Vector2(Scalar s) { this(s, s); }
    public Vector2 self() { return this; }
    public Vector2 add(Vector2 p) { return new Vector2(x.add(p.x), y.add(p.y)); }
    public Vector2 sub(Vector2 p) { return new Vector2(x.sub(p.x), y.sub(p.y)); }
    public Vector2 add(Scalar s) { return new Vector2(x.add(s), y.add(s)); }
    public Vector2 sub(Scalar s) { return new Vector2(x.sub(s), y.sub(s)); }
    public Vector2 mul(Scalar s) { return new Vector2(x.mul(s), y.mul(s)); }
    public Vector2 div(Scalar s) { return new Vector2(x.div(s), y.div(s)); }
    public Scalar dot(Vector2 v) { return x.mul(v.x).add(y.mul(v.y)); }
  }
  record Vector3(Scalar x, Scalar y, Scalar z) implements Vector<Vector3> {
    Vector3(Scalar s) { this(s, s, s); }
    public Vector3 self() { return this; }
    public Vector3 add(Vector3 p) { return new Vector3(x.add(p.x), y.add(p.y), z.add(p.z)); }
    public Vector3 sub(Vector3 p) { return new Vector3(x.sub(p.x), y.sub(p.y), z.sub(p.z)); }
    public Vector3 add(Scalar s) { return new Vector3(x.add(s), y.add(s), z.add(s)); }
    public Vector3 sub(Scalar s) { return new Vector3(x.sub(s), y.sub(s), z.sub(s)); }
    public Vector3 mul(Scalar s) { return new Vector3(x.mul(s), y.mul(s), z.mul(s)); }
    public Vector3 div(Scalar s) { return new Vector3(x.div(s), y.div(s), z.div(s)); }
    public Scalar dot(Vector3 v) { return x.mul(v.x).add(y.mul(v.y)).add(z.mul(v.z)); }
    public Vector3 cross(Vector3 v) {
      return new Vector3(y.mul(v.z).sub(z.mul(v.y)), z.mul(v.x).sub(x.mul(v.z)), x.mul(v.y).sub(y.mul(v.x)));
    }
  }

  record Color(Red r, Green g, Blue b, Alpha a) {
    Color(Red r, Green g, Blue b) { this(r, g, b, new Alpha(255)); }
    Color(Hue h, Saturation s, Value v) { this(fromHSV(h, s, v)); }
    private Color(Color c) { this(c.r, c.g, c.b, c.a); }
    private static Color fromHSV(Hue h, Saturation s, Value v) {
      if (s.s() == 0) {
        int c = v.v() * 255;
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
