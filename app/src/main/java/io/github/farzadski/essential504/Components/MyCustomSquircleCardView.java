package io.github.farzadski.essential504.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.github.farzadski.essential504.R;

public class MyCustomSquircleCardView extends FrameLayout {

  private static final float DEFAULT_CORNER_RADIUS_DP = 24f;
  private static final float DEFAULT_CONTROL_FACTOR = 0.90f;
  private static final float DEFAULT_STROKE_WIDTH_DP = 0f; // 0 = no stroke
  private static final int DEFAULT_STROKE_COLOR = Color.BLACK;

  private final Paint backgroundPaint;
  private final Paint strokePaint;
  private final Path squirclePath; // outer shape: fill + clip
  private final Path strokePath; // inset shape: stroke sits fully inside the card

  private float topLeftRadius;
  private float topRightRadius;
  private float bottomRightRadius;
  private float bottomLeftRadius;
  private float controlFactor;
  private float strokeWidth; // px
  private int cardBackgroundColor;
  private int strokeColor;

  public MyCustomSquircleCardView(@NonNull Context context) {
    this(context, null);
  }

  public MyCustomSquircleCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MyCustomSquircleCardView(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    squirclePath = new Path();
    strokePath = new Path();
    initialize(attrs);
  }

  private void initialize(@Nullable AttributeSet attrs) {
    float defaultRadius = dpToPx(DEFAULT_CORNER_RADIUS_DP);
    topLeftRadius = defaultRadius;
    topRightRadius = defaultRadius;
    bottomRightRadius = defaultRadius;
    bottomLeftRadius = defaultRadius;
    controlFactor = DEFAULT_CONTROL_FACTOR;
    cardBackgroundColor = Color.WHITE;
    strokeWidth = dpToPx(DEFAULT_STROKE_WIDTH_DP);
    strokeColor = DEFAULT_STROKE_COLOR;

    if (attrs != null) {
      try (TypedArray array =
          getContext().obtainStyledAttributes(attrs, R.styleable.MyCustomSquircleCardView)) {

        topLeftRadius =
            array.getDimension(
                R.styleable.MyCustomSquircleCardView_squircleTopLeftRadius, defaultRadius);

        topRightRadius =
            array.getDimension(
                R.styleable.MyCustomSquircleCardView_squircleTopRightRadius, defaultRadius);

        bottomRightRadius =
            array.getDimension(
                R.styleable.MyCustomSquircleCardView_squircleBottomRightRadius, defaultRadius);

        bottomLeftRadius =
            array.getDimension(
                R.styleable.MyCustomSquircleCardView_squircleBottomLeftRadius, defaultRadius);

        controlFactor =
            array.getFloat(
                R.styleable.MyCustomSquircleCardView_squircleControlFactor, DEFAULT_CONTROL_FACTOR);

        cardBackgroundColor =
            array.getColor(
                R.styleable.MyCustomSquircleCardView_squircleBackgroundColor, Color.WHITE);

        strokeWidth =
            array.getDimension(
                R.styleable.MyCustomSquircleCardView_squircleStrokeWidth, strokeWidth);

        strokeColor =
            array.getColor(
                R.styleable.MyCustomSquircleCardView_squircleStrokeColor, DEFAULT_STROKE_COLOR);
      }
    }

    controlFactor = clamp(controlFactor);
    strokeWidth = Math.max(0f, strokeWidth);

    backgroundPaint.setStyle(Paint.Style.FILL);
    backgroundPaint.setColor(cardBackgroundColor);

    strokePaint.setStyle(Paint.Style.STROKE);
    strokePaint.setStrokeWidth(strokeWidth);
    strokePaint.setStrokeJoin(Paint.Join.ROUND);
    strokePaint.setStrokeCap(Paint.Cap.ROUND);
    strokePaint.setColor(strokeColor);

    setWillNotDraw(false);
    setClipChildren(false);
    setClipToPadding(false);
    setBackground(null);
  }

  @Override
  public Drawable getBackground() {
    return new ColorDrawable(cardBackgroundColor);
  }

  @Override
  protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    super.onSizeChanged(width, height, oldWidth, oldHeight);
    updatePath(width, height);
  }

  private void updatePath(int width, int height) {
    if (width <= 0 || height <= 0) {
      return;
    }

    // Outer shape (fill + clip), full bounds.
    buildSquirclePath(squirclePath, 0f, width, height, 0f);

    // Stroke shape: pulled in by half the stroke width so the whole line
    // stays inside the card and never touches the clip edge.
    buildSquirclePath(strokePath, strokeWidth / 2f, width, height, strokeWidth / 2f);

    invalidate();
  }

  /**
   * Builds the squircle into {@code path}.
   *
   * @param inset distance from each edge of the view to the path
   * @param radiusShrink amount subtracted from every corner radius, so the inset shape keeps
   *     parallel curves with the outer one
   */
  private void buildSquirclePath(
      Path path, float inset, int width, int height, float radiusShrink) {
    float l = inset;
    float t = inset;
    float r = width - inset;
    float b = height - inset;

    float maxRadius = Math.min(r - l, b - t) / 2f;
    float tl = clampRadius(topLeftRadius - radiusShrink, maxRadius);
    float tr = clampRadius(topRightRadius - radiusShrink, maxRadius);
    float br = clampRadius(bottomRightRadius - radiusShrink, maxRadius);
    float bl = clampRadius(bottomLeftRadius - radiusShrink, maxRadius);

    float ctl = tl * controlFactor;
    float ctr = tr * controlFactor;
    float cbr = br * controlFactor;
    float cbl = bl * controlFactor;

    path.reset();

    // TOP LEFT
    path.moveTo(l, t + tl);
    path.cubicTo(l, t + tl - ctl, l + tl - ctl, t, l + tl, t);

    // TOP EDGE
    path.lineTo(r - tr, t);

    // TOP RIGHT
    path.cubicTo(r - tr + ctr, t, r, t + tr - ctr, r, t + tr);

    // RIGHT EDGE
    path.lineTo(r, b - br);

    // BOTTOM RIGHT
    path.cubicTo(r, b - br + cbr, r - br + cbr, b, r - br, b);

    // BOTTOM EDGE
    path.lineTo(l + bl, b);

    // BOTTOM LEFT
    path.cubicTo(l + bl - cbl, b, l, b - bl + cbl, l, b - bl);

    // LEFT EDGE
    path.lineTo(l, t + tl);
    path.close();
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    canvas.drawPath(squirclePath, backgroundPaint);
  }

  @Override
  protected void dispatchDraw(@NonNull Canvas canvas) {
    // 1) children, clipped to the squircle
    int saveCount = canvas.save();
    canvas.clipPath(squirclePath);
    super.dispatchDraw(canvas);
    canvas.restoreToCount(saveCount);

    // 2) stroke on top of everything, so it covers the whole card edge
    if (strokeWidth > 0f && Color.alpha(strokeColor) > 0) {
      canvas.drawPath(strokePath, strokePaint);
    }
  }

  // ---------------------------------------------------------
  // Top Left
  // ---------------------------------------------------------

  public float getTopLeftRadius() {
    return pxToDp(topLeftRadius);
  }

  public void setTopLeftRadius(float radiusDp) {
    topLeftRadius = dpToPx(radiusDp);
    updatePath(getWidth(), getHeight());
  }

  // ---------------------------------------------------------
  // Top Right
  // ---------------------------------------------------------

  public float getTopRightRadius() {
    return pxToDp(topRightRadius);
  }

  public void setTopRightRadius(float radiusDp) {
    topRightRadius = dpToPx(radiusDp);
    updatePath(getWidth(), getHeight());
  }

  // ---------------------------------------------------------
  // Bottom Right
  // ---------------------------------------------------------

  public float getBottomRightRadius() {
    return pxToDp(bottomRightRadius);
  }

  public void setBottomRightRadius(float radiusDp) {
    bottomRightRadius = dpToPx(radiusDp);
    updatePath(getWidth(), getHeight());
  }

  // ---------------------------------------------------------
  // Bottom Left
  // ---------------------------------------------------------

  public float getBottomLeftRadius() {
    return pxToDp(bottomLeftRadius);
  }

  public void setBottomLeftRadius(float radiusDp) {
    bottomLeftRadius = dpToPx(radiusDp);
    updatePath(getWidth(), getHeight());
  }

  // ---------------------------------------------------------
  // Control Factor
  // ---------------------------------------------------------

  public float getControlFactor() {
    return controlFactor;
  }

  public void setControlFactor(float controlFactor) {
    this.controlFactor = clamp(controlFactor);
    updatePath(getWidth(), getHeight());
  }

  // ---------------------------------------------------------
  // Background
  // ---------------------------------------------------------

  public int getCardBackgroundColor() {
    return cardBackgroundColor;
  }

  public void setCardBackgroundColor(@ColorInt int color) {
    cardBackgroundColor = color;
    backgroundPaint.setColor(cardBackgroundColor);
    invalidate();
  }

  // ---------------------------------------------------------
  // Stroke
  // ---------------------------------------------------------

  /** Stroke width in dp. */
  public float getStrokeWidth() {
    return pxToDp(strokeWidth);
  }

  /** Sets the stroke width in dp. Use 0 to remove the stroke. */
  public void setStrokeWidth(float widthDp) {
    strokeWidth = Math.max(0f, dpToPx(widthDp));
    strokePaint.setStrokeWidth(strokeWidth);
    updatePath(getWidth(), getHeight());
  }

  @ColorInt
  public int getStrokeColor() {
    return strokeColor;
  }

  public void setStrokeColor(@ColorInt int color) {
    strokeColor = color;
    strokePaint.setColor(strokeColor);
    invalidate();
  }

  /** Convenience: set width (dp) and color together. */
  public void setStroke(float widthDp, @ColorInt int color) {
    strokeColor = color;
    strokePaint.setColor(color);
    setStrokeWidth(widthDp);
  }

  // ---------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------

  private float clampRadius(float radius, float maxRadius) {
    return Math.min(Math.max(radius, 0f), Math.max(maxRadius, 0f));
  }

  private float clamp(float value) {
    return Math.max(0f, Math.min(value, 1f));
  }

  private float dpToPx(float dp) {
    return dp * getResources().getDisplayMetrics().density;
  }

  private float pxToDp(float px) {
    return px / getResources().getDisplayMetrics().density;
  }
}
