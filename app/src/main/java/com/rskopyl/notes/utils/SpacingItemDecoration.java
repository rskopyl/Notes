package com.rskopyl.notes.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacingHalfSizePx;

    public SpacingItemDecoration(int spacingSizePx) {
        this.spacingHalfSizePx = spacingSizePx / 2;
    }

    private boolean getReverseLayout(LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getReverseLayout();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getReverseLayout();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private int getOrientation(LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private int getSpanCount(LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof LinearLayoutManager) {
            return 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        Adapter<?> adapter = parent.getAdapter();
        LayoutManager layoutManager = parent.getLayoutManager();
        if (adapter == null || layoutManager == null) return;

        int position = parent.getChildAdapterPosition(view);
        boolean reverseLayout = getReverseLayout(layoutManager);
        int orientation = getOrientation(layoutManager);
        int spanCount = getSpanCount(layoutManager);
        if ((position >= spanCount && !reverseLayout) ||
                (position < adapter.getItemCount() - spanCount && reverseLayout)
        ) {
            outRect.top = spacingHalfSizePx;
        }
        if (position % spanCount != 0) {
            outRect.left = spacingHalfSizePx;
        }
        if ((position + 1) % spanCount != 0) {
            outRect.right = spacingHalfSizePx;
        }
        if ((position < adapter.getItemCount() - spanCount && !reverseLayout) ||
                (position >= spanCount && reverseLayout)
        ) {
            outRect.bottom = spacingHalfSizePx;
        }
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(outRect.top, outRect.right, outRect.bottom, outRect.left);
        }
    }
}