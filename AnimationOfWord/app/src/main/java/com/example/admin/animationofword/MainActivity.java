package com.example.admin.animationofword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Context context;
    RelativeLayout container;
    RelativeLayout place;
    RelativeLayout view;
    RelativeLayout parent;
    boolean check = false;
    int space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        container = findViewById(R.id.container);
        place = findViewById(R.id.place);
        view = findViewById(R.id.view);
        parent = findViewById(R.id.parent);
        TextView textView1 = findViewById(R.id.text1);
        TextView textView2 = findViewById(R.id.text2);
        TextView textView3 = findViewById(R.id.text3);
        TextView textView4 = findViewById(R.id.text4);
        TextView textView5 = findViewById(R.id.text5);
        TextView textView6 = findViewById(R.id.text6);

        textView1.setOnTouchListener(new MyChoiseTouch());
        textView2.setOnTouchListener(new MyChoiseTouch());
        textView3.setOnTouchListener(new MyChoiseTouch());
        textView4.setOnTouchListener(new MyChoiseTouch());
        textView5.setOnTouchListener(new MyChoiseTouch());
        textView6.setOnTouchListener(new MyChoiseTouch());
    }

    class MyChoiseTouch implements View.OnTouchListener {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            container.setClipToOutline(false);
            place.setClipToOutline(false);
            v.setClipToOutline(false);
            final RelativeLayout viewGroup = (RelativeLayout) v.getParent().getParent();
            //space khoang cach giua 2 view
            space = parent.getHeight() - place.getHeight() - container.getHeight();
            final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //xu ly su kien khi view nam o duoi chuyen len tren
                if (viewGroup == container) {
                    //neu chieu dai con cho chua cho view dang click thi set vao ben phai view truoc
                    if (place.getWidth() - view.getWidth() >= v.getWidth()) {
                        leftRelative(v, layoutParams);
                    } else {
                        downLineRelative(v, layoutParams);
                    }
                    //neu view xuong dong thi add vao ben phai view xuong dong va ben duoi view dong 1
                    if (view.getHeight() - 40 > v.getY() + v.getHeight()) {
                        downLineLeftRelative(v, layoutParams);
                    }
                } else {
                    //xu ly su kien khi view nam o tren chuyen xuong duoi
                    if (viewGroup == place) {
                        backToFirstPlace(v, layoutParams);
                    }
                }
            }
            return true;
        }
    }

    private void backToFirstPlace(final View v, final RelativeLayout.LayoutParams layoutParams) {
        stopAnimation(v);
        final RelativeLayout locationView = findViewById((int) v.getTag());
        final RelativeLayout parentView = (RelativeLayout) v.getParent();
        int[] location = new int[2];
        locationView.getLocationInWindow(location);
        //back from top to bottom set location 1, else - location 1
        TranslateAnimation animationDown = new TranslateAnimation(0, location[0]-v.getX(), 0,
                location[1] - v.getY());
        animationDown.setRepeatMode(0);
        animationDown.setDuration(100);
        animationDown.setFillAfter(true);
        animationDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("TAGGG", "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                swapViewWhenRemove(parentView, v, layoutParams, locationView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.requestLayout();
        v.startAnimation(animationDown);
    }

    private void swapViewWhenRemove(final RelativeLayout parentView, final View v, final RelativeLayout.LayoutParams layoutParams, final RelativeLayout locationView) {
        boolean handler = new Handler().post(new Runnable() {
            public void run() {
                parentView.removeView(v);
                updateRelativeViewWhenRemove(view);
                layoutParams.removeRule(RelativeLayout.BELOW);
                layoutParams.removeRule(RelativeLayout.RIGHT_OF);
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                locationView.addView(v, layoutParams);
            }
        });
    }

    private void leftRelative(View v, RelativeLayout.LayoutParams layoutParams) {
        if (view.getChildCount() != 0) {
            layoutParams.addRule(RelativeLayout.RIGHT_OF, view.getChildAt(view.getChildCount() - 1).getId());
        }
        int[] location = new int[2];
        RelativeLayout parentView = (RelativeLayout) v.getParent();
        parentView.getLocationInWindow(location);
        int x = view.getWidth() - location[0];
        int y = -(location[1] - space);
        RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
        v.setTag(relativeLayout.getId());
        translateUp(view, v, layoutParams, 20, x, y);
    }

    private void downLineRelative(View v, RelativeLayout.LayoutParams layoutParams) {
        int[] location = new int[2];
        RelativeLayout parentView = (RelativeLayout) v.getParent();
        parentView.getLocationInWindow(location);
        int x = (int) view.getChildAt(0).getX() - location[0];
        int y = -(location[1] - space - view.getHeight());
        RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
        v.setTag(relativeLayout.getId());
        layoutParams.addRule(RelativeLayout.BELOW, view.getChildAt(0).getId());
        translateUp(view, v, layoutParams, 20, x, y);
    }

    private void downLineLeftRelative(View v, RelativeLayout.LayoutParams layoutParams) {
        int[] location = new int[2];
        RelativeLayout parentView = (RelativeLayout) v.getParent();
        parentView.getLocationInWindow(location);
        layoutParams.addRule(RelativeLayout.BELOW, view.getChildAt(0).getId());
        layoutParams.addRule(RelativeLayout.RIGHT_OF, view.getChildAt(view.getChildCount() - 1).getId());
        int x = (int) view.getChildAt(view.getChildCount() - 1).getX()
                + view.getChildAt(view.getChildCount() - 1).getWidth() - location[0];
        int y = -(location[1] - space + -v.getHeight());
        RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
        v.setTag(relativeLayout.getId());
        translateUp(view, v, layoutParams, 20, x, y);
    }

    void translateUp(final RelativeLayout parentView, final View v, final RelativeLayout.LayoutParams layoutParams, int margin, int toX, int toY) {
        TranslateAnimation animationUp = new TranslateAnimation(0, (toX + margin), 0,
                (toY + margin));
        animationUp.setRepeatMode(0);
        animationUp.setDuration(100);
        animationUp.setFillAfter(true);
        animationUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                final RelativeLayout viewGroup = (RelativeLayout) v.getParent();
//                new Handler().post(new Runnable() { public void run() {  viewGroup.removeView(v); } });
//                viewGroup.removeView(v);
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout viewGroup = (RelativeLayout) v.getParent();
                viewGroup.removeView(v);
                if (parentView.getChildCount() != 0) {
                    parentView.addView(v, layoutParams);
                    v.setEnabled(true);
                } else {
                    parentView.addView(v);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.requestLayout();
        v.startAnimation(animationUp);
    }

    private void updateRelativeViewWhenRemove(RelativeLayout view) {
        check = false;
        int sum = 0;
        for (int i = 0; i < view.getChildCount(); i++) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getChildAt(i).getLayoutParams();
            if (i != 0) {
                sum = sum + (view.getChildAt(i - 1).getWidth() + 10);
            }
            //từ đầu tiên
            if (i == 0) {
                layoutParams.removeRule(RelativeLayout.BELOW);
                layoutParams.removeRule(RelativeLayout.RIGHT_OF);
                view.getChildAt(i).setLayoutParams(layoutParams);
                //cac tu con lai right of toi khi het cho dong 1
            } else if (place.getWidth() - sum > view.getChildAt(i).getWidth()) {
                layoutParams.removeRule(RelativeLayout.BELOW);
                layoutParams.removeRule(RelativeLayout.RIGHT_OF);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, view.getChildAt(i - 1).getId());
                if (check) {
                    layoutParams.addRule(RelativeLayout.BELOW, view.getChildAt(0).getId());
                    if (i == view.getChildCount() - 1) {
                        check = false;
                    }
                }
                view.getChildAt(i).setLayoutParams(layoutParams);
                //xuong dong dau tien
            } else if (!check) {
                layoutParams.removeRule(RelativeLayout.BELOW);
                layoutParams.removeRule(RelativeLayout.RIGHT_OF);
                layoutParams.addRule(RelativeLayout.BELOW, view.getChildAt(0).getId());
                sum = 0;
                view.getChildAt(i).setLayoutParams(layoutParams);
                check = true;
            }
        }
    }

    public void stopAnimation(View v) {
        v.clearAnimation();
        if (canCancelAnimation()) {
            v.animate().cancel();
        }
    }

    public static boolean canCancelAnimation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }
}


