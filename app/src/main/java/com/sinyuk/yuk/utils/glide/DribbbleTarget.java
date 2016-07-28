package com.sinyuk.yuk.utils.glide;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;


/**
 * A Glide {@see ViewTarget} for {@link ImageView}s. It applies a badge for animated
 * images, can prevent GIFs from auto-playing & applies a palette generated ripple.
 */
public class DribbbleTarget extends GlideDrawableImageViewTarget implements
        Palette.PaletteAsyncListener {

    private final boolean autoplayGifs;

    public DribbbleTarget(ImageView view, boolean autoplayGifs) {
        super(view);
        this.autoplayGifs = autoplayGifs;
    }

    @Override
    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable>
            animation) {
        super.onResourceReady(resource, animation);
        if (!autoplayGifs) {
            resource.stop();
        }

        ImageView badgedImageView = (ImageView) getView();
        if (resource instanceof GlideBitmapDrawable) {
            Palette.from(((GlideBitmapDrawable) resource).getBitmap())
                    .clearFilters()
                    .generate(this);
//            badgedImageView.showBadge(false);
        } else if (resource instanceof GifDrawable) {
            Bitmap image = ((GifDrawable) resource).getFirstFrame();
            Palette.from(image).clearFilters().generate(this);
//            badgedImageView.showBadge(true);

            // look at the corner to determine the gif badge color
            int cornerSize = (int) (56 * getView().getContext().getResources().getDisplayMetrics
                    ().scaledDensity);
            Bitmap corner = Bitmap.createBitmap(image,
                    image.getWidth() - cornerSize,
                    image.getHeight() - cornerSize,
                    cornerSize, cornerSize);
//            boolean isDark = ColorUtils.isDark(corner);
//            corner.recycle();
//            badgedImageView.setBadgeColor(ContextCompat.getColor(getView().getContext(),
//                    isDark ? R.color.gif_badge_dark_image : R.color.gif_badge_light_image));
        }
    }

    @Override
    public void onStart() {
        if (autoplayGifs) {
            super.onStart();
        }
    }

    @Override
    public void onStop() {
        if (autoplayGifs) {
            super.onStop();
        }
    }

    @Override
    public void onGenerated(Palette palette) {
//        ((ImageView) getView()).setForeground(
//                ViewUtils.createRipple(palette, 0.25f, 0.5f,
//                        ContextCompat.getColor(getView().getContext(), R.color.mid_grey), true));
    }

}