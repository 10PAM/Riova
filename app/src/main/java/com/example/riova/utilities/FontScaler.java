/*
Programmer (Team Novelty) {
    - Fulya Bilgin, CWU Student ID: 50593580
}
File: FontScaler.java
About: Applies the user's "font size" setting to the text on a page. The
       setting is stored as 0 = small, 1 = medium, 2 = large. This walks a
       page's view tree and resizes every TextView (which includes buttons and
       text fields) by a scale factor.

       Each TextView's original size is remembered the first time it is scaled,
       so re-applying a new size never compounds (it always scales from the
       original, not from the already-scaled value).
*/

package com.example.riova.utilities;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;
import java.util.WeakHashMap;

public class FontScaler {

    // Remembers each TextView's original (unscaled) pixel size.
    private static final Map<TextView, Float> baseSizes = new WeakHashMap<>();

    /**
     * Turns the stored font-size setting into a scale factor.
     *
     * @param setting 0 = small, 1 = medium, 2 = large.
     * @return the multiplier to apply to text sizes.
     */
    public static float scaleFor(int setting) {
        switch (setting) {
            case 0:
                return 0.85f;
            case 2:
                return 1.20f;
            default:
                return 1.0f;
        }
    }

    /**
     * Reads the saved font-size setting from preferences and applies it.
     *
     * @param root    the page's root view.
     * @param context any context (used to read the preference).
     */
    public static void applyFromPrefs(View root, Context context) {
        int setting = context.getApplicationContext()
                .getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getInt("font_size", 1);
        apply(root, setting);
    }

    /**
     * Applies a font-size setting to every TextView under the given view.
     *
     * @param root    the view (or view group) to scale.
     * @param setting 0 = small, 1 = medium, 2 = large.
     */
    public static void apply(View root, int setting) {
        scale(root, scaleFor(setting));
    }

    /**
     * Recursively resizes TextViews from their remembered original size.
     *
     * @param view  the current view being visited.
     * @param scale the multiplier to apply.
     */
    private static void scale(View view, float scale) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            Float base = baseSizes.get(textView);
            if (base == null) {
                base = textView.getTextSize();   // original size in pixels
                baseSizes.put(textView, base);
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, base * scale);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                scale(group.getChildAt(i), scale);
            }
        }
    }
}
