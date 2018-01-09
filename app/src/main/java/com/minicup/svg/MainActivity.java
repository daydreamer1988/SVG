package com.minicup.svg;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import static com.minicup.svg.R.id.imageView;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Drawable background;
    private AnimatedVectorDrawableCompat unfocus;
    private AnimatedVectorDrawableCompat focus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        mImageView = (ImageView) findViewById(imageView);
        background = mImageView.getDrawable();
        if (background instanceof AnimatedVectorDrawableCompat) {
            final AnimatedVectorDrawableCompat compat = (AnimatedVectorDrawableCompat) background;
            compat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationStart(Drawable drawable) {
                    super.onAnimationStart(drawable);
                    Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    Toast.makeText(MainActivity.this, "end", Toast.LENGTH_SHORT).show();
                    compat.unregisterAnimationCallback(this);
                }
            });

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    Drawable drawable = imageView.getDrawable();
                    if (drawable instanceof Animatable) {
                        if (((Animatable) drawable).isRunning()) {
                            ((Animatable) drawable).stop();
                        } else {
                            ((Animatable) drawable).start();
                        }
                    }
                }
            });
        }

        //
        unfocus = AnimatedVectorDrawableCompat.create(this, R.drawable.searchbar_unfocus);
        focus = AnimatedVectorDrawableCompat.create(this, R.drawable.searchbar_focus);
    }


    public void start(View view){
        Drawable drawable = ((ImageView) view).getDrawable();
        if(drawable instanceof Animatable){
            ((Animatable) drawable).start();
        }
    }

    private static final int UNFOCUS = 300;
    private static final int FOCUS = 948;
    int state = UNFOCUS;

    public void startAnim(View view) {
        if (view instanceof ImageView) {
            if (state == UNFOCUS) {
                state = FOCUS;
                focus((AppCompatImageView) view);
            } else if (state == FOCUS) {
                state = UNFOCUS;
                unfocus((AppCompatImageView) view);
            }
        }
    }

    private void unfocus(AppCompatImageView view) {
        view.setImageDrawable(unfocus);
        unfocus.start();
    }

    private void focus(AppCompatImageView view) {
        view.setImageDrawable(focus);
        focus.start();
        /*ImageView imageView = view;
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }*/
    }

    private boolean flag;

    public void anim2(View view) {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            Toast.makeText(this, "5.0以下不支持path morph", Toast.LENGTH_SHORT).show();
            return;
        }
        //注意，带有path morph的动画只支持5.0之后的系统，即便在XML中引用了带有 path morph的动画，5.0之前的系统会抛异常，
        //并且，只能使用AnimatedVectorDrawable，而AnimatedVectorDrawableCompat是为了兼容性为5.0之前的系统使用的，不支持path morph。
//        AnimatedVectorDrawableCompat full = AnimatedVectorDrawableCompat.create(this, R.drawable.heart_full_anim);
//        AnimatedVectorDrawableCompat empty = AnimatedVectorDrawableCompat.create(this, R.drawable.heart_empty_anim);

        ImageView imageView = (ImageView) view;
        AnimatedVectorDrawable tickToCross = (AnimatedVectorDrawable) getDrawable(R.drawable.heart_full_anim);
        AnimatedVectorDrawable crossToTick = (AnimatedVectorDrawable) getDrawable(R.drawable.heart_empty_anim);
        AnimatedVectorDrawable animDrawable = flag ? crossToTick: tickToCross;
        imageView.setImageDrawable(animDrawable);
        if (animDrawable != null) {
            animDrawable.start();
        }
        flag = !flag;
    }
}
