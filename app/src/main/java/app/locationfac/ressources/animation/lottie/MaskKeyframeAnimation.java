package app.locationfac.ressources.animation.lottie;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

class MaskKeyframeAnimation {
  private final Path path = new Path();
  private final List<BaseKeyframeAnimation<?, Path>> maskAnimations;
  private final List<Mask> masks;

  MaskKeyframeAnimation(List<Mask> masks) {
    this.masks = masks;
    this.maskAnimations = new ArrayList<>(masks.size());
    for (int i = 0; i < masks.size(); i++) {
      this.maskAnimations.add(masks.get(i).getMaskPath().createAnimation());
    }
  }

  List<Mask> getMasks() {
    return masks;
  }

  List<BaseKeyframeAnimation<?, Path>> getMaskAnimations() {
    return maskAnimations;
  }

  Path getMaskUnionPath() {
    path.reset();
    for (BaseKeyframeAnimation<?, Path> mask : maskAnimations) {
      this.path.addPath(mask.getValue());
    }
    return path;
  }
}
