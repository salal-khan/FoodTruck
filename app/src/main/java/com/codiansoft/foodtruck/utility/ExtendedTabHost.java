package com.codiansoft.foodtruck.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

/**
 * Created by Ahmad on 7/25/2016.
 */
public class ExtendedTabHost extends TabHost {

    public ExtendedTabHost(Context context) {
        super(context);
    }

    public ExtendedTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentTab(int index) {

        View currentView = this.getCurrentView();

        if (this.getCurrentTab() < index) {

            if (currentView != null) {
                currentView.startAnimation(AnimationUtils.loadAnimation(this.getContext(), android.R.anim.slide_out_right));
            }

            super.setCurrentTab(index);

            currentView = this.getCurrentView();

            if (currentView != null) {
                currentView.startAnimation(AnimationUtils.loadAnimation(this.getContext(), android.R.anim.slide_in_left));
            }
        } else {

            if (currentView != null) {
                currentView.startAnimation(AnimationUtils.loadAnimation(this.getContext(), android.R.anim.slide_out_right));
            }

            super.setCurrentTab(index);

            currentView = this.getCurrentView();

            if (currentView != null) {
                currentView.startAnimation(AnimationUtils.loadAnimation(this.getContext(), android.R.anim.slide_in_left));
            }

        }
    }


}
