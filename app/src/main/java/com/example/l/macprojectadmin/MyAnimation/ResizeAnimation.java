package com.example.l.macprojectadmin.MyAnimation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by L on 12/12/17.
 */

public class ResizeAnimation extends Animation{

    private int tinggitarget,lebartarget;
    private int lebarawal,tinggiawal;
    private View view;

    public ResizeAnimation(View view,int ttarget,int ltarget){
        this.lebarawal      = 0;
        this.tinggiawal     = 0;
        this.lebartarget    = ltarget;
        this.tinggitarget   = ttarget;
        this.view = view;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int hasillebar = (int)(lebarawal + lebartarget * interpolatedTime);
        int hasiltinggi = (int)(tinggiawal + tinggitarget * interpolatedTime);
        this.view.setPivotX(0.5f);
        this.view.setPivotY(0.5f);
        view.getLayoutParams().height = hasiltinggi;
        view.getLayoutParams().width = hasillebar;
        view.requestLayout();

    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
