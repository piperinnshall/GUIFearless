// package com.piperinnshall.fluentguijava.demo;
//
// import com.piperinnshall.fluentguijava.fluentgraphics.Easing;
// import com.piperinnshall.fluentguijava.fluentgraphics.Lerp;
// import com.piperinnshall.fluentguijava.fluentgraphics.Vec2;
//
// public class LerpBallNativeDemo {
//   Vec2 origin = new Vec2(600, 400);
//   Vec2 destination = new Vec2(600, 400);
//   long animationStart = 0L;
//
//   Vec2 currentPosition(long elapsed) {
//     return Lerp.of(origin, destination, 0.6f, Easing.EASE_OUT_BOUNCE)
//         .at(elapsed - animationStart);
//   }
//
//   void reTarget(Vec2 click, long elapsed) {
//     origin = currentPosition(elapsed);
//     destination = click;
//     animationStart = elapsed;
//   }
//
//   void run() {
//     new GUI().run("Animated Ball", frame -> frame
//         .resizable()
//         .undecorated()
//         .opacity(0.8f)
//         .panel(panel -> panel
//             .size(new Vec2(1200, 800))
//             .fps(120)
//             .background(30, 30, 30)
//             .paintable((ctx, elapsed) -> ctx
//                 .color(255, 100, 100)
//                 .oval(currentPosition(elapsed).sub(40), new Vec2(80)))
//             .onMouse(mouse -> mouse
//                 .pressed(this::reTarget)
//             )
//         )
//     );
//   }
//
//   public static void main(String[] a) {
//     new LerpBallNativeDemo().run();
//   }
// }
