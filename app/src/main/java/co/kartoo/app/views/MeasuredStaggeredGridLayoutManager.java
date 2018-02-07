package co.kartoo.app.views;

/**
 * Created by MartinOenang on 10/12/2015.
 */

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


public class MeasuredStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    public MeasuredStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    private int[] mMeasuredDimension = new int[2];
    int normalHeight = 0;

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {

        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);
        int width = 0;
        int height = 0;
        int heightR = 0;
        int heightL = 0;
        for (int i = 0; i < getItemCount(); i++) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    mMeasuredDimension);
            if (i == 4) {
                Log.e("asdf", "item number 4");
            } else if(i % 2 == 0){
                heightL += mMeasuredDimension[1];
            }else{
                heightR += mMeasuredDimension[1];
            }

            if (i == 0) {
                width = mMeasuredDimension[0];
            }
        }
        switch (widthMode) {
            case View.MeasureSpec.EXACTLY:
                width = widthSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        switch (heightMode) {
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        if(heightL != 0 || heightR != 0){
            height = (heightL > heightR) ? heightL : heightR;
        }
        if (getItemCount() > 4) height+=normalHeight*2;
        setMeasuredDimension(width, height);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {

        View view = recycler.getViewForPosition(position);
        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                    getPaddingLeft() + getPaddingRight(), p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(childWidthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            if (normalHeight == 0) {
                normalHeight = measuredDimension[1]-p.bottomMargin-p.topMargin;
            }
            if (position == 4) {
                measuredDimension[1] = Math.round(normalHeight*2);
            }
            recycler.recycleView(view);
        }
    }
}
