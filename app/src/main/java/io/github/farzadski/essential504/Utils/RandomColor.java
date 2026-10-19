package io.github.farzadski.essential504;

import android.graphics.Color;
import android.util.Log;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random / stable pastel colors, computed entirely in Java. No XML, no resources, no Context, no
 * Python.
 *
 * <p>Source palette: families x {50, 100, 200}. Every other level (10..400) is derived from those
 * three by interpolation/extrapolation.
 *
 * <p>Levels: "10","20","30","40","50","60","70","80","90","100","200","300","400"
 *
 * <pre>{@code
 * int a = Helper.getRandomColor();
 * int b = Helper.getRandomColor("10", "20", "30");
 * int c = Helper.getRandomColor(Helper.ALPHA_50, "50", "100", "200");
 * int d = Helper.getStableColor(lesson.getId(), "50", "100");
 * int[] e = Helper.getRandomColors(8, "50", "100");
 * int f = Helper.getColor("200", Helper.ALPHA_75);
 * }</pre>
 */
public final class RandomColor {

  public static final String[] DEFAULT_LEVELS = {"50", "100", "200"};
  public static final int ALPHA_OPAQUE = 0xFF;
  public static final int ALPHA_95 = 0xF2;
  public static final int ALPHA_90 = 0xE6;
  public static final int ALPHA_75 = 0xBF;
  public static final int ALPHA_50 = 0x80;
  public static final int ALPHA_25 = 0x40;
  public static final int ALPHA_10 = 0x1A;
  @ColorInt public static final int FALLBACK_COLOR = Color.BLACK;
  private static final String TAG = "Helper";
  // ===========================================================================
  // Palette: { shade50, shade100, shade200 } per family
  // ===========================================================================
  private static final int[][] PALETTE = {
    // Reds
    {0xFFEBEE, 0xFFCDD2, 0xEF9A9A}, // red
    {0xFFF0F3, 0xF8BBD0, 0xF48FB1}, // rose
    {0xFCE4EC, 0xF4B8C4, 0xEFA0B2}, // ruby
    {0xFDE7EA, 0xF5B9C2, 0xEFA0AA}, // crimson
    {0xFFF0EC, 0xFFD0C7, 0xFFAB91}, // coral
    {0xFFF1ED, 0xFFCCBC, 0xF8A995}, // salmon
    {0xFFE9ED, 0xFFC6CF, 0xF49AAA}, // watermelon
    {0xFCE8EB, 0xF7C1C8, 0xEFA0A8}, // cherry

    // Pinks
    {0xFCE4EC, 0xF8BBD0, 0xF48FB1}, // pink
    {0xFFF0F5, 0xFCE4EC, 0xF8BBD0}, // light_pink
    {0xFFF0F7, 0xFBCFE8, 0xF5A6D0}, // hot_pink
    {0xFFF1F3, 0xF8C8D4, 0xF0A6B9}, // blush
    {0xFFF5F7, 0xFFDCE5, 0xF7BFD0}, // baby_pink
    {0xF7E9EB, 0xEAC4CC, 0xD99AA8}, // dusty_rose
    {0xF5EAF0, 0xE8C6D6, 0xD7A0BC}, // mauve
    {0xFDE7F3, 0xF7C5DF, 0xF0A6D1}, // bubblegum

    // Purples
    {0xF3E5F5, 0xE1BEE7, 0xCE93D8}, // purple
    {0xEDE7F6, 0xD1C4E9, 0xB39DDB}, // deep_purple
    {0xF3EEFF, 0xDDD0F5, 0xC5A8ED}, // lavender
    {0xF5F0FF, 0xE2D5FF, 0xCBB5F5}, // light_violet
    {0xF1EAFE, 0xD8C5F2, 0xBCA0E8}, // violet
    {0xF7F0FF, 0xE6CCF2, 0xD0A8E8}, // lilac
    {0xF9EAF7, 0xEBC3E5, 0xDDA0D1}, // orchid
    {0xF1EAF8, 0xD7C2E8, 0xB69BD8}, // amethyst
    {0xF0E9FA, 0xD8C6F0, 0xBCA3E3}, // wisteria
    {0xF2E8F5, 0xDCC1E3, 0xC5A0D6}, // grape
    {0xEEF0FF, 0xC9CCF5, 0xAEB5EA}, // periwinkle
    {0xF5EAF2, 0xE2C0D7, 0xD2A0C2}, // plum

    // Blues
    {0xE8EAF6, 0xC5CAE9, 0x9FA8DA}, // indigo
    {0xE3F2FD, 0xBBDEFB, 0x90CAF9}, // blue
    {0xE1F5FE, 0xB3E5FC, 0x81D4FA}, // light_blue
    {0xEAF7FF, 0xB9E2FA, 0x8AC9ED}, // sky_blue
    {0xF0F8FF, 0xC5E5FA, 0x9DCFEF}, // baby_blue
    {0xEDF6FC, 0xC6E0F2, 0x9CC7E0}, // powder_blue
    {0xF1FAFF, 0xC9EAF5, 0xA2D9EB}, // ice_blue
    {0xEAF8FF, 0xB9E5FA, 0x85D0F0}, // azure
    {0xE8F5FF, 0xB9DFF5, 0x81C4E8}, // cerulean
    {0xEBF0FF, 0xC4D1F5, 0x9BAFE8}, // cornflower
    {0xEAF1FA, 0xC1D6F0, 0x91B5DE}, // denim
    {0xE8EEFA, 0xC1CEEF, 0x91A8DD}, // sapphire
    {0xE8EEFF, 0xBBCBFA, 0x8FA9F0}, // cobalt
    {0xEEF0F8, 0xC7CFE3, 0x9CAAC7}, // slate_blue

    // Cyans / aquas
    {0xE0F7FA, 0xB2EBF2, 0x80DEEA}, // cyan
    {0xE5FAF8, 0xB2E8E2, 0x80D8CF}, // aqua
    {0xE3F8F5, 0xB2E5DC, 0x80CBC4}, // turquoise
    {0xE6FFF7, 0xB7EBD9, 0x86DCC1}, // aquamarine
    {0xE8F8F1, 0xBDE8D5, 0x8ED8B8}, // mint
    {0xE8F7F0, 0xC1E9D9, 0x94D7BB}, // seafoam
    {0xE0F8F5, 0xB0E5DF, 0x75D3CA}, // robin_egg
    {0xE5F8FA, 0xB7E7EC, 0x82D4DD}, // lagoon
    {0xE0F2F1, 0xB2DFDB, 0x80CBC4}, // teal

    // Greens
    {0xE8F5E9, 0xC8E6C9, 0xA5D6A7}, // green
    {0xF1F8E9, 0xDCEDC8, 0xC5E1A5}, // light_green
    {0xF9FBE7, 0xF0F4C3, 0xE6EE9C}, // lime
    {0xEEF3E8, 0xD7E3CF, 0xB9CEAC}, // sage
    {0xF0F6E8, 0xDDEBC7, 0xC7DDA7}, // pistachio
    {0xE9F6EF, 0xC3E5D0, 0xA6D8B7}, // sea_green
    {0xE5F4EC, 0xBFE3CF, 0x98D1B1}, // jade
    {0xE6F5ED, 0xBDE5CE, 0x91D3AB}, // emerald
    {0xEDF5E9, 0xD1E5C6, 0xB5D5A2}, // fern
    {0xF3F4E5, 0xE2E7BE, 0xD2D99A}, // olive
    {0xF1F5E5, 0xDDE6BB, 0xC8D795}, // avocado
    {0xEDF7EF, 0xD0E8D4, 0xAFD9B7}, // celadon
    {0xE8F3EE, 0xC7E1D4, 0xA0CFB8}, // eucalyptus
    {0xF0F5E7, 0xDDE9C5, 0xC5D99E}, // matcha

    // Yellows
    {0xFFFDE7, 0xFFF9C4, 0xFFF59D}, // yellow
    {0xFFFDEB, 0xFFF7AD, 0xFFF176}, // lemon
    {0xFFF8E1, 0xFFF1BD, 0xFFE89A}, // butter
    {0xFFF8E7, 0xF8EEC8, 0xEEDD9D}, // vanilla
    {0xFFF9E6, 0xFFF0B3, 0xFFE082}, // banana
    {0xFFFDE5, 0xFFF6B8, 0xFFEE80}, // canary
    {0xFFF8E5, 0xF9E8B5, 0xF2D783}, // honey
    {0xFFFBE8, 0xFFF3B0, 0xFFE66D}, // sunshine
    {0xFFF9E8, 0xF8E8B6, 0xF0D783}, // corn

    // Ambers / oranges
    {0xFFF8E1, 0xFFECB3, 0xFFEE58}, // amber
    {0xFFF3E0, 0xFFE0B2, 0xFFCC80}, // orange
    {0xFBE9E7, 0xFFCCBC, 0xFFAB91}, // deep_orange
    {0xFFF0E6, 0xFFD8C2, 0xFFBFA3}, // peach
    {0xFFF1E5, 0xFFDBB9, 0xFFC38F}, // apricot
    {0xFFF0E5, 0xFFD2AC, 0xFFB574}, // tangerine
    {0xFFF4DF, 0xFFE0A3, 0xFFD06B}, // mango
    {0xFFF0E7, 0xFFD6BF, 0xFFB99A}, // papaya
    {0xFFF1E6, 0xFAD0B4, 0xF4B18B}, // pumpkin
    {0xF9EDE7, 0xE8C4B5, 0xD8A58F}, // copper
    {0xF8EAE4, 0xE8BFB1, 0xD6A08D}, // terracotta
    {0xF9F0DF, 0xE8D2A9, 0xD8BB7D}, // ochre

    // Browns / neutrals
    {0xEFEBE9, 0xD7CCC8, 0xBCAAA4}, // brown
    {0xECEFF1, 0xCFD8DC, 0xB0BEC5}, // blue_grey
    {0xFAFAFA, 0xF5F5F5, 0xEEEEEE}, // grey
    {0xF7F5F3, 0xE8E2DC, 0xD3C8BE}, // warm_grey
    {0xF3F5F7, 0xE3E8EC, 0xC5CED5}, // cool_grey
    {0xF5F3F0, 0xE5DFD7, 0xCEC5BA}, // stone
    {0xF7F1E8, 0xEDE0CB, 0xDCC8A9}, // sand
    {0xF5F0E6, 0xE9DFC9, 0xD8C7A7}, // beige
    {0xFFF9ED, 0xFFF0CF, 0xF1DDAA}, // cream
    {0xFFFFF0, 0xF5F0D9, 0xE5DDB6}, // ivory
    {0xFAF0E6, 0xF0DFD0, 0xDFC5AE}, // linen
    {0xFAF1E4, 0xF0DFC1, 0xE3C99C}, // champagne
    {0xF5EFE6, 0xE8DCCB, 0xD5C4AA}, // oat
    {0xF5EBE0, 0xE9D5C3, 0xD6B89F}, // latte
    {0xF3E9E2, 0xE4D0C5, 0xD1B5A5}, // cocoa
    {0xF1ECE7, 0xDDD2C7, 0xC7B7A8}, // taupe
    {0xF2E8E0, 0xE3CEBD, 0xD0B19A}, // mocha
    {0xF7EFE5, 0xEBDCC8, 0xDCC5A6}, // almond

    // Modern pastels
    {0xEDFBF6, 0xC8F0E2, 0xA2E5CF}, // ice_mint
    {0xFFF0FA, 0xF9D3EC, 0xF0B5DD}, // cotton_candy
    {0xF7F0FF, 0xE7D5FA, 0xD3B6F0}, // unicorn
    {0xF0F5FF, 0xD6E2FA, 0xB8C9F0}, // dreamy_blue
    {0xF5F8FC, 0xE5EBF2, 0xCDD7E3}, // cloud
    {0xF0F4F5, 0xDDE6E8, 0xC3D3D6}, // mist
    {0xFCFBF9, 0xF0EDE7, 0xDDD8CE}, // pearl
    {0xFFFAFA, 0xF8EEEE, 0xEEDDDD}, // snow
    {0xFAFAF5, 0xEEEFE5, 0xDDE0D2}, // alabaster
    {0xF3F2FA, 0xDFDDEC, 0xC8C5DD}, // moonlight
    {0xF1F8FC, 0xDCECF5, 0xBBD9E8}, // frost
    {0xF5F2F8, 0xE7DDEB, 0xD3C3D8}, // haze
  };
  // ===========================================================================
  // Level definitions: level -> {kind, t}
  //   LIGHTEN: white -> 50      BETWEEN: 50 -> 100      EXTEND: 100 -> 200 (extrapolated)
  // ===========================================================================
  private static final int LIGHTEN = 0, BETWEEN = 1, EXTEND = 2;
  private static final Map<String, double[]> LEVELS = new ConcurrentHashMap<>();

  /** Computed once per level, then reused: level -> opaque RGB per family. */
  private static final Map<String, int[]> LEVEL_CACHE = new ConcurrentHashMap<>();

  static {
    LEVELS.put("10", new double[] {LIGHTEN, 0.20});
    LEVELS.put("20", new double[] {LIGHTEN, 0.40});
    LEVELS.put("30", new double[] {LIGHTEN, 0.60});
    LEVELS.put("40", new double[] {LIGHTEN, 0.80});
    LEVELS.put("50", new double[] {LIGHTEN, 1.00});
    LEVELS.put("60", new double[] {BETWEEN, 0.20});
    LEVELS.put("70", new double[] {BETWEEN, 0.40});
    LEVELS.put("80", new double[] {BETWEEN, 0.60});
    LEVELS.put("90", new double[] {BETWEEN, 0.80});
    LEVELS.put("100", new double[] {BETWEEN, 1.00});
    LEVELS.put("200", new double[] {EXTEND, 1.00});
    LEVELS.put("300", new double[] {EXTEND, 2.00});
    LEVELS.put("400", new double[] {EXTEND, 3.00});
  }

  private RandomColor() {}

  // ===========================================================================
  // Public API — random
  // ===========================================================================

  /** Random color from {@link #DEFAULT_LEVELS}, fully opaque. */
  @ColorInt
  public static int get() {
    return get(ALPHA_OPAQUE, DEFAULT_LEVELS);
  }

  /** Random color from the given levels, fully opaque. */
  @ColorInt
  public static int get(@NonNull String... levels) {
    return get(ALPHA_OPAQUE, levels);
  }

  /** Random color from the given levels with a forced alpha (0-255). */
  @ColorInt
  public static int get(int alpha, @NonNull String... levels) {
    String level = pickRandom(levelsOrDefault(levels));
    int[] colors = colorsForLevel(level);
    if (colors == null) return FALLBACK_COLOR;
    int index = ThreadLocalRandom.current().nextInt(colors.length);
    return applyAlpha(colors[index], alpha);
  }

  // ===========================================================================
  // Public API — stable (same input -> same color)
  // ===========================================================================

  /** Same seed + levels -> same color on every call. */
  @ColorInt
  public static int getStableColor(long seed, @NonNull String... levels) {
    return getStableColor(seed, ALPHA_OPAQUE, levels);
  }

  /** Same seed + levels + alpha -> same color on every call. */
  @ColorInt
  public static int getStableColor(long seed, int alpha, @NonNull String... levels) {
    String[] pool = levelsOrDefault(levels);
    long h = mix(seed);
    String level = pool[(int) Math.floorMod(h, (long) pool.length)];
    int[] colors = colorsForLevel(level);
    if (colors == null) return FALLBACK_COLOR;
    // Use different bits than the level pick so level and family aren't correlated.
    int index = (int) Math.floorMod(h >>> 17, (long) colors.length);
    return applyAlpha(colors[index], alpha);
  }

  // ===========================================================================
  // Public API — batch
  // ===========================================================================

  /** Returns {@code count} random colors drawn from the given levels. */
  @NonNull
  public static int[] getColors(int count, @NonNull String... levels) {
    int[] out = new int[Math.max(0, count)];
    for (int i = 0; i < out.length; i++) out[i] = get(levels);
    return out;
  }

  // ===========================================================================
  // Public API — single known level
  // ===========================================================================

  /** One random color from a single level, fully opaque. */
  @ColorInt
  public static int get(@NonNull String level) {
    return get(level, ALPHA_OPAQUE);
  }

  /** One random color from a single level with a forced alpha. */
  @ColorInt
  public static int get(@NonNull String level, int alpha) {
    int[] colors = colorsForLevel(level);
    if (colors == null) return FALLBACK_COLOR;
    int index = ThreadLocalRandom.current().nextInt(colors.length);
    return applyAlpha(colors[index], alpha);
  }

  // ===========================================================================
  // Internals
  // ===========================================================================

  private static String[] levelsOrDefault(String[] levels) {
    return (levels == null || levels.length == 0) ? DEFAULT_LEVELS : levels;
  }

  private static String pickRandom(String[] values) {
    return values[ThreadLocalRandom.current().nextInt(values.length)];
  }

  /** Returns all family colors (opaque ARGB) for a level, computing them once. */
  private static int[] colorsForLevel(String level) {
    int[] cached = LEVEL_CACHE.get(level);
    if (cached != null) return cached;

    double[] def = LEVELS.get(level);
    if (def == null) {
      Log.w(TAG, "Unknown level '" + level + "'. Valid: " + LEVELS.keySet());
      return null;
    }

    int kind = (int) def[0];
    double t = def[1];
    int[] result = new int[PALETTE.length];
    for (int i = 0; i < PALETTE.length; i++) {
      result[i] = compute(PALETTE[i], kind, t);
    }
    LEVEL_CACHE.put(level, result);
    return result;
  }

  private static int compute(int[] shades, int kind, double t) {
    int c50 = shades[0], c100 = shades[1], c200 = shades[2];
    double r, g, b;
    switch (kind) {
      case LIGHTEN:
        r = lerp(255, red(c50), t);
        g = lerp(255, green(c50), t);
        b = lerp(255, blue(c50), t);
        break;
      case BETWEEN:
        r = lerp(red(c50), red(c100), t);
        g = lerp(green(c50), green(c100), t);
        b = lerp(blue(c50), blue(c100), t);
        break;
      default: // EXTEND
        r = lerp(red(c100), red(c200), t);
        g = lerp(green(c100), green(c200), t);
        b = lerp(blue(c100), blue(c200), t);
        break;
    }
    return 0xFF000000 | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
  }

  private static double lerp(double a, double b, double t) {
    return a + (b - a) * t;
  }

  private static int red(int rgb) {
    return (rgb >> 16) & 0xFF;
  }

  private static int green(int rgb) {
    return (rgb >> 8) & 0xFF;
  }

  private static int blue(int rgb) {
    return rgb & 0xFF;
  }

  private static int clamp(double v) {
    return Math.max(0, Math.min(255, (int) Math.round(v)));
  }

  /** SplitMix64-style scrambler so consecutive ids don't give neighbouring colors. */
  private static long mix(long z) {
    z += 0x9E3779B97F4A7C15L;
    z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
    z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
    return z ^ (z >>> 31);
  }

  private static int applyAlpha(@ColorInt int color, int alpha) {
    int a = Math.max(0, Math.min(255, alpha));
    return (color & 0x00FFFFFF) | (a << 24);
  }
}
