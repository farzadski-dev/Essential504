package io.github.farzadski.essential504.Components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import io.github.farzadski.essential504.R;

public class MyCustomGlowBackgroundView extends View {

  private static final float DEFAULT_GLOW_1_X = 0.593f;
  private static final float DEFAULT_GLOW_1_Y = 0.064f;
  private static final float DEFAULT_GLOW_1_WIDTH = 0.309f;
  private static final float DEFAULT_GLOW_1_HEIGHT = 0.394f;

  private static final float DEFAULT_GLOW_2_X = 0.593f;
  private static final float DEFAULT_GLOW_2_Y = 0.336f;
  private static final float DEFAULT_GLOW_2_WIDTH = 0.309f;
  private static final float DEFAULT_GLOW_2_HEIGHT = 0.366f;

  private final Paint glow1Paint = createPaint();
  private final Paint glow2Paint = createPaint();

  private final RectF glow1Rect = new RectF();
  private final RectF glow2Rect = new RectF();

  private int backgroundColor;
  private int glow1Color;
  private int glow2Color;

  private float glow1X;
  private float glow1Y;
  private float glow1Width;
  private float glow1Height;
  private float glow1BlurRadius;
  private float glow1Opacity;
  private float glow1Rotation;

  private float glow2X;
  private float glow2Y;
  private float glow2Width;
  private float glow2Height;
  private float glow2BlurRadius;
  private float glow2Opacity;
  private float glow2Rotation;

  private boolean glowEnabled;
  private boolean glowAnimated;

  private float animationDistance;
  private int animationDuration;

  private float animationProgress;
  private ValueAnimator animator;

  public MyCustomGlowBackgroundView(Context context) {
    super(context);
    initialize(null);
  }

  public MyCustomGlowBackgroundView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize(attrs);
  }

  public MyCustomGlowBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(attrs);
  }

  private static Paint createPaint() {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setStyle(Paint.Style.FILL);
    paint.setDither(true);
    return paint;
  }

  private void initialize(AttributeSet attrs) {
    // BlurMaskFilter uses software rendering.
    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    setWillNotDraw(false);

    // Defaults matching your original SVG.
    backgroundColor = android.graphics.Color.TRANSPARENT;

    glow1Color = 0xFF2196F3;
    glow1X = DEFAULT_GLOW_1_X;
    glow1Y = DEFAULT_GLOW_1_Y;
    glow1Width = DEFAULT_GLOW_1_WIDTH;
    glow1Height = DEFAULT_GLOW_1_HEIGHT;
    glow1BlurRadius = dp(96);
    glow1Opacity = 1f;
    glow1Rotation = 0f;

    glow2Color = 0xFFEF5350;
    glow2X = DEFAULT_GLOW_2_X;
    glow2Y = DEFAULT_GLOW_2_Y;
    glow2Width = DEFAULT_GLOW_2_WIDTH;
    glow2Height = DEFAULT_GLOW_2_HEIGHT;
    glow2BlurRadius = dp(74);
    glow2Opacity = 1f;
    glow2Rotation = 0f;

    glowEnabled = true;
    glowAnimated = false;
    animationDistance = dp(12);
    animationDuration = 4000;

    if (attrs != null) {
      TypedArray array =
          getContext().obtainStyledAttributes(attrs, R.styleable.MyCustomGlowBackgroundView);

      try {
        backgroundColor =
            array.getColor(
                R.styleable.MyCustomGlowBackgroundView_glowBackgroundColor, backgroundColor);

        glowEnabled =
            array.getBoolean(R.styleable.MyCustomGlowBackgroundView_glowEnabled, glowEnabled);

        glow1Color = array.getColor(R.styleable.MyCustomGlowBackgroundView_glow1Color, glow1Color);
        glow1X = array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow1X, glow1X);
        glow1Y = array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow1Y, glow1Y);
        glow1Width = array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow1Width, glow1Width);
        glow1Height =
            array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow1Height, glow1Height);
        glow1BlurRadius =
            array.getDimension(
                R.styleable.MyCustomGlowBackgroundView_glow1BlurRadius, glow1BlurRadius);
        glow1Opacity =
            array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow1Opacity, glow1Opacity);
        glow1Rotation =
            array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow1Rotation, glow1Rotation);

        glow2Color = array.getColor(R.styleable.MyCustomGlowBackgroundView_glow2Color, glow2Color);
        glow2X = array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow2X, glow2X);
        glow2Y = array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow2Y, glow2Y);
        glow2Width = array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow2Width, glow2Width);
        glow2Height =
            array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow2Height, glow2Height);
        glow2BlurRadius =
            array.getDimension(
                R.styleable.MyCustomGlowBackgroundView_glow2BlurRadius, glow2BlurRadius);
        glow2Opacity =
            array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow2Opacity, glow2Opacity);
        glow2Rotation =
            array.getFloat(R.styleable.MyCustomGlowBackgroundView_glow2Rotation, glow2Rotation);

        glowAnimated =
            array.getBoolean(R.styleable.MyCustomGlowBackgroundView_glowAnimated, glowAnimated);
        animationDistance =
            array.getDimension(
                R.styleable.MyCustomGlowBackgroundView_glowAnimationDistance, animationDistance);
        animationDuration =
            array.getInt(
                R.styleable.MyCustomGlowBackgroundView_glowAnimationDuration, animationDuration);

      } finally {
        array.recycle();
      }
    }

    updatePaints();
  }

  private void updatePaints() {
    glow1Paint.setColor(applyOpacity(glow1Color, glow1Opacity));
    glow2Paint.setColor(applyOpacity(glow2Color, glow2Opacity));

    glow1Paint.setMaskFilter(
        glow1BlurRadius > 0
            ? new BlurMaskFilter(glow1BlurRadius, BlurMaskFilter.Blur.NORMAL)
            : null);

    glow2Paint.setMaskFilter(
        glow2BlurRadius > 0
            ? new BlurMaskFilter(glow2BlurRadius, BlurMaskFilter.Blur.NORMAL)
            : null);

    invalidate();
  }

  private int applyOpacity(int color, float opacity) {
    float alpha = Math.max(0f, Math.min(1f, opacity));
    int originalAlpha = android.graphics.Color.alpha(color);
    int resultAlpha = Math.round(originalAlpha * alpha);

    return (color & 0x00FFFFFF) | (resultAlpha << 24);
  }

  private float dp(float value) {
    return value * getResources().getDisplayMetrics().density;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.drawColor(backgroundColor);

    if (!glowEnabled || getWidth() == 0 || getHeight() == 0) {
      return;
    }

    float width = getWidth();
    float height = getHeight();

    float movement = glowAnimated ? (animationProgress * 2f - 1f) * animationDistance : 0f;

    drawGlow(
        canvas,
        glow1Paint,
        glow1Rect,
        glow1X,
        glow1Y,
        glow1Width,
        glow1Height,
        width,
        height,
        glow1Rotation,
        movement);

    drawGlow(
        canvas,
        glow2Paint,
        glow2Rect,
        glow2X,
        glow2Y,
        glow2Width,
        glow2Height,
        width,
        height,
        glow2Rotation,
        -movement);
  }

  private void drawGlow(
      Canvas canvas,
      Paint paint,
      RectF rect,
      float x,
      float y,
      float rectWidth,
      float rectHeight,
      float viewWidth,
      float viewHeight,
      float rotation,
      float translation) {
    rect.set(
        x * viewWidth, y * viewHeight, (x + rectWidth) * viewWidth, (y + rectHeight) * viewHeight);

    int saveCount = canvas.save();

    canvas.translate(translation, -translation * 0.5f);

    if (rotation != 0f) {
      canvas.rotate(rotation, rect.centerX(), rect.centerY());
    }

    canvas.drawRect(rect, paint);
    canvas.restoreToCount(saveCount);
  }

  private void startGlowAnimation() {
    if (!glowAnimated || animator != null || !glowEnabled) {
      return;
    }

    animator = ValueAnimator.ofFloat(0f, 1f);
    animator.setDuration(Math.max(1, animationDuration));
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.REVERSE);
    animator.setInterpolator(new AccelerateDecelerateInterpolator());

    animator.addUpdateListener(
        valueAnimator -> {
          animationProgress = (float) valueAnimator.getAnimatedValue();
          invalidate();
        });

    animator.start();
  }

  private void stopGlowAnimation() {
    if (animator != null) {
      animator.cancel();
      animator = null;
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    startGlowAnimation();
  }

  @Override
  protected void onDetachedFromWindow() {
    stopGlowAnimation();
    super.onDetachedFromWindow();
  }

  // Public API for runtime customization.

  public void setGlow1Color(int color) {
    glow1Color = color;
    updatePaints();
  }

  public void setGlow2Color(int color) {
    glow2Color = color;
    updatePaints();
  }

  public void setGlowEnabled(boolean enabled) {
    glowEnabled = enabled;

    if (enabled) {
      startGlowAnimation();
    } else {
      stopGlowAnimation();
    }

    invalidate();
  }

  public void setGlowAnimated(boolean animated) {
    glowAnimated = animated;

    if (animated) {
      startGlowAnimation();
    } else {
      stopGlowAnimation();
      animationProgress = 0f;
    }

    invalidate();
  }

  public void setGlow1Opacity(float opacity) {
    glow1Opacity = opacity;
    updatePaints();
  }

  public void setGlow2Opacity(float opacity) {
    glow2Opacity = opacity;
    updatePaints();
  }

  public void setGlow1BlurRadius(float radiusPx) {
    glow1BlurRadius = Math.max(0f, radiusPx);
    updatePaints();
  }

  public void setGlow2BlurRadius(float radiusPx) {
    glow2BlurRadius = Math.max(0f, radiusPx);
    updatePaints();
  }

  public void setGlowBackgroundColor(int color) {
    backgroundColor = color;
    invalidate();
  }
}
