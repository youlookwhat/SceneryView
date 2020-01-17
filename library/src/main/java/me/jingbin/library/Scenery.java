package me.jingbin.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 风景View
 * 2020-01-01 11:51
 *
 * @author jingbin
 */
public class Scenery extends ViewGroup {

    public Scenery(Context context) {
        super(context);
    }

    public Scenery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Scenery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
