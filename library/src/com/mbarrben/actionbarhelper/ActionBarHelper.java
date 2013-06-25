package com.mbarrben.actionbarhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.cyrilmottier.android.translucentactionbar.NotifyingScrollView;
import com.cyrilmottier.android.translucentactionbar.NotifyingScrollView.OnScrollChangedListener;
import com.manuelpeinado.glassactionbar.FadingActionBarHelper;
import com.manuelpeinado.glassactionbar.GlassActionBarHelper;

/**
 * @author Miguel Barrios
 */
public class ActionBarHelper {
    private FadingActionBarHelper fadingHelper;
    private GlassActionBarHelper glassHelper;

    private boolean blur = false;
    private boolean fade = false;

    private int contentLayout;
    private int abBackgroundRes;
    private int headerId;
    private int blurDownSampling = 0;

    public static class Builder {
        private ActionBarHelper instance;

        public Builder(int layout) {
            instance = new ActionBarHelper(layout);
        }

        public Builder blur() {
            instance.blur = true;
            return this;
        }

        public Builder blur(int downSampling) {
            instance.blurDownSampling = downSampling;
            return blur();
        }

        public Builder fade(int abBackgroundRes, int headerId) {
            instance.headerId = headerId;
            instance.abBackgroundRes = abBackgroundRes;
            instance.fade = true;
            return this;
        }

        public View build(Context context) {
            return instance.createView(context);
        }
    }

    private ActionBarHelper(int layout) {
        this.contentLayout = layout;
    }

    private View createView(Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.abh__frame, null);
        final View content = inflater.inflate(contentLayout, frame, false);
        frame.addView(content, 0);

        initFadeHelper(frame, abBackgroundRes, headerId);
        initGlassHelper(frame, content);

        if (content instanceof NotifyingScrollView) {
            NotifyingScrollView scrollView = (NotifyingScrollView) content;
            scrollView.setOnScrollChangedListener(scrollListener);
        }

        return frame;
    }

    private void initFadeHelper(FrameLayout frame, int abBackgroundRes, int headerId) {
        if (fade) {
            fadingHelper = new FadingActionBarHelper(frame, abBackgroundRes, headerId);
        }
    }

    private void initGlassHelper(FrameLayout frame, View contentView) {
        if (blur) {
            glassHelper = new GlassActionBarHelper(frame, contentView);
            glassHelper.setDownSampling(blurDownSampling);
        }
    }

    private OnScrollChangedListener scrollListener = new OnScrollChangedListener() {
        @Override
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            if (blur) {
                glassHelper.onScrollChanged(who, l, t, oldl, oldt);
            }
            if (fade) {
                fadingHelper.onScrollChanged(who, l, t, oldl, oldt);
            }
        }
    };

}