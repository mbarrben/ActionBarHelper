/*
 * Copyright (C) 2013 Manuel Peinado
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.manuelpeinado.glassactionbar;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.cyrilmottier.android.translucentactionbar.NotifyingScrollView.OnScrollChangedListener;
import com.mbarrben.actionbarhelper.R;

/**
 * @author Manuel Peinado with modifications from Miguel Barrios
 */
public class FadingActionBarHelper implements OnGlobalLayoutListener, OnScrollChangedListener {
    private ImageView fadingOverlay;
    private boolean hasFirstGlobalLayout = false;
    private View header;
    private int headerHeight;
    private Drawable fadingOverlayDrawable;

    public FadingActionBarHelper(FrameLayout frame, int abBackgroundRes, int headerId) {
        fadingOverlay = (ImageView) frame.findViewById(R.id.fadingOverlay);
        fadingOverlayDrawable = frame.getContext().getResources().getDrawable(abBackgroundRes);
        fadingOverlay.setImageDrawable(fadingOverlayDrawable);
        fadingOverlayDrawable.setAlpha(0);

        header = frame.findViewById(headerId);

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY);

        header.measure(widthMeasureSpec, heightMeasureSpec);
        headerHeight = header.getMeasuredHeight();

        frame.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (!hasFirstGlobalLayout) {
            hasFirstGlobalLayout = false;
            updateHeaderHeight();
        }
    }

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        setNewAlpha(t);
    }

    private void updateHeaderHeight() {
        int headerHeight = header.getHeight();
        if (headerHeight != 0) {
            this.headerHeight = headerHeight;
        }
    }

    private void setNewAlpha(int scrollPosition) {
        final int currentHeaderHeight = header.getHeight();
        if (currentHeaderHeight != headerHeight) {
            headerHeight = currentHeaderHeight;
        }

        final int headerHeight = currentHeaderHeight - fadingOverlay.getHeight();
        final float ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;
        final int newAlpha = (int) (ratio * 255);
        fadingOverlayDrawable.setAlpha(newAlpha);
    }

}