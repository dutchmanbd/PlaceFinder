package com.zxdmjr.placefinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 9/7/2016.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private GestureDetector gestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;

            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View childView = rv.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && onItemClickListener != null && gestureDetector.onTouchEvent(e))
        {
            onItemClickListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }


        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
