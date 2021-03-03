package com.example.funnychat.widgets;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ToggleButton;

import java.lang.reflect.Method;

public class AnimatedToggleButton extends ToggleButton {

    private Bitmap animMainBitmap = null;
    private Drawable animDefaultDrawable = null;
    private Drawable animCheckedDrawable = null;

    private Canvas animMainCanvas = null;

    private int animDuration = 300;
    private boolean animLoaded = false;

    public AnimatedToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAnimation();
    }

    public AnimatedToggleButton(Context context) {
        super(context);
        loadAnimation();
    }

    final private void loadAnimation() {
        this.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                animLoaded = false;

                final int[] ST_DEFAULT = new int[]{-android.R.attr.state_checked};
                final int[] ST_CHECKED = new int[]{android.R.attr.state_checked};

                try {
                    StateListDrawable stateListDrawable = (StateListDrawable) getBackground();

                    Method getStateDrawableIndex =
                            StateListDrawable.class.getMethod(
                                    "findStateDrawableIndex", int[].class);
                    Method getStateDrawable =
                            StateListDrawable.class.getMethod(
                                    "getStateDrawable", int.class);

                    int index = (int) getStateDrawableIndex.invoke (stateListDrawable, ST_DEFAULT);
                    animDefaultDrawable = (Drawable) getStateDrawable.invoke(
                            stateListDrawable, index);

                    index = (int) getStateDrawableIndex.invoke (stateListDrawable, ST_CHECKED);
                    animCheckedDrawable = (Drawable) getStateDrawable.invoke(
                            stateListDrawable, index);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (animDefaultDrawable != null && animCheckedDrawable != null) {
                        animMainBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                                Bitmap.Config.ARGB_8888);

                        final Rect animMainBitmapDstRect = new Rect();
                        animMainBitmapDstRect.set(0, 0, getWidth(), getHeight());
                        animMainCanvas = new Canvas (animMainBitmap);

                        animDefaultDrawable.setBounds(animMainBitmapDstRect);
                        animCheckedDrawable.setBounds(animMainBitmapDstRect);

                        if (isChecked())
                            animCheckedDrawable.draw(animMainCanvas);
                        else
                            animDefaultDrawable.draw(animMainCanvas);

                        setBackground(new BitmapDrawable(getContext().getResources(),
                                animMainBitmap));

                        animLoaded = true;
                    } else {
                        return;
                    }
                }
            }
        });
    }

    final private void animateAndSetChecked (boolean checked) {
        final boolean CHECKED = checked;

        if (!animLoaded && isChecked() != CHECKED) {
            super.setChecked(CHECKED);
            return;
        }

        final ValueAnimator animator = new ValueAnimator();
        animator.setDuration(animDuration);

        if (isChecked())
            animator.setIntValues(255, 0);
        else
            animator.setIntValues(0, 255);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animMainCanvas.drawColor (Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                animDefaultDrawable.draw(animMainCanvas);
                animCheckedDrawable.setAlpha((int) animation.getAnimatedValue());
                animCheckedDrawable.draw(animMainCanvas);
                setBackground(new BitmapDrawable(getContext().getResources(),
                        animMainBitmap));

                invalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatedToggleButton.super.setChecked(CHECKED);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.start();
    }

    public int getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    @Override
    public void toggle() {
        animateAndSetChecked (!isChecked());
    }

}