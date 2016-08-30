package com.sinyuk.yuk.utils;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.ViewPropertyAnimator;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Sinyuk on 16/8/15.
 */
public class BlackMagics {
    /*
     * 进度条上移
     * @param v
     * @param withLayer
     */
    public static ViewPropertyAnimator fadeOut(SmoothProgressBar progressBar) {
        Preconditions.checkNotNull(progressBar,"progressBar is Null");
        return progressBar.animate()
                .alpha(0)
                .setDuration(400)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withLayer();
    }

    /*
    * 进度条下移
    * @param v
    * @param withLayer
    */
    public static ViewPropertyAnimator fadeIn(SmoothProgressBar progressBar) {
        Preconditions.checkNotNull(progressBar,"progressBar is Null");
        return progressBar.animate()
                .alpha(1f)
                .setDuration(400)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withLayer();
    }
}
