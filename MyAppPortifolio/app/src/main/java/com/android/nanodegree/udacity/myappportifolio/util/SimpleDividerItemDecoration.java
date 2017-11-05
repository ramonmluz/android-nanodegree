package com.android.nanodegree.udacity.myappportifolio.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

import com.android.nanodegree.udacity.myappportifolio.R;

/**
 * Created by ramon on 05/11/17.
 */

public class SimpleDividerItemDecoration extends ItemDecoration {
    private Drawable mDvider;

    public SimpleDividerItemDecoration(Context context) {
        mDvider = context.getResources().getDrawable(R.drawable.line_devider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();

        for(int i=0; i< childCount;i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDvider.getIntrinsicHeight();

            mDvider.setBounds(left, top, right, bottom);
            mDvider.draw(c);

        }
    }

}
