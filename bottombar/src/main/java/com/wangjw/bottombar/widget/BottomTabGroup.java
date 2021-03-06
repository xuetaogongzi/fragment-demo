package com.wangjw.bottombar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wangjw.bottombar.BottomTabImpl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangjw on 16/11/21.
 */

@SuppressWarnings("unused")
public class BottomTabGroup extends LinearLayout {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    //holds the checked id,the selection is empty by default
    private int mCheckedId = -1;

    // racks children radio buttons checked state
    private BottomTabImpl.OnCheckedChangeListener mChildCheckedChangeListener;

    //when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    private PassThroughHierarchyChangeListener mPassThroughListener;

    public BottomTabGroup(Context context) {
        this(context, null);
    }

    public BottomTabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        super.setOrientation(HORIZONTAL);
        mChildCheckedChangeListener = new CheckedStateTracker();

        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    public void check(int id) {
        if (id != -1 && id != mCheckedId) {
            clearCheck();
            setCheckedStateForView(id, true);
            setCheckedId(id);
        }
    }

    public int getCheckedChildId() {
        return mCheckedId;
    }

    public void clearCheck() {
        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        //the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof BottomTabImpl) {
            final BottomTabImpl button = (BottomTabImpl) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }
        super.addView(child, index, params);
    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChange(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof BottomTabImpl) {
            ((BottomTabImpl) checkedView).setChecked(checked);
        }
    }

    @Override
    public LinearLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new BottomTabGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof BottomTabGroup.LayoutParams;
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(BottomTabGroup root, int checkedId);
    }

    private class CheckedStateTracker implements BottomTabImpl.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(BottomTabImpl buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     * <p>
     * 此监听器是为了拦截view的添加和移除事件.强制将OnHierarchyChangeListener设置为mPassThroughListener.
     * 外部通过{@link BottomTabGroup#setOnHierarchyChangeListener(OnHierarchyChangeListener)}设置的
     * OnHierarchyChangeListener将成为PassThroughHierarchyChangeListener的成员变量,通过PassThroughHierarchyChangeListener
     * 手动调用
     * </p>
     */
    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == BottomTabGroup.this && child instanceof BottomTabImpl) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = generateViewId();
                    child.setId(id);
                }
                ((BottomTabImpl) child).setOnCheckedChangeWidgetListener(mChildCheckedChangeListener);
            }

            //手动调用外部设置的OnHierarchyChangeListener
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == BottomTabGroup.this && child instanceof BottomTabImpl) {
                ((BottomTabImpl) child).setOnCheckedChangeWidgetListener(null);
            }

            //手动调用外部设置的OnHierarchyChangeListener
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < 17) {
            for (; ;) {
                final int result = sNextGeneratedId.get();

                //aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00ffffff) {
                    newValue = 1; // Roll over to 1, not 0.
                }

                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return generateViewId();
        }
    }
}
