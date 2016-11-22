package com.wangjw.bottombar.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangjw.bottombar.BottomTabImpl;
import com.wangjw.bottombar.R;

/**
 * Created by wangjw on 16/11/21.
 */

public class BottomTab extends LinearLayout implements BottomTabImpl {

    private static final int DEFAULT_TITLE_TEXT_SIZE = 36;

    private CharSequence mTabTitle;
    private int mTabTitleSize;
    private ColorStateList mTabTextColors;
    private Drawable mTabDrawable;

    private TextView mTvTabHint;
    private RadioImageView mRivTabIcon;
    private RadioTextView mRtvTabTitle;

    private boolean mChecked = false;

    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public BottomTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs == null) {
            throw new NullPointerException();
        }

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomTab);
        mTabTitle = ta.getText(R.styleable.BottomTab_android_text);
        mTabTitleSize = ta.getDimensionPixelSize(R.styleable.BottomTab_android_textSize, DEFAULT_TITLE_TEXT_SIZE);
        mTabTextColors = ta.getColorStateList(R.styleable.BottomTab_android_textColor);
        mTabDrawable = ta.getDrawable(R.styleable.BottomTab_android_drawableTop);
        ta.recycle();

        this.initViews();
    }

    private void initViews() {
        super.setOrientation(VERTICAL);
        inflate(getContext(), getLayoutRes(), this);

        mTvTabHint = (TextView) findViewById(R.id.tab_hint);
        mRivTabIcon = (RadioImageView) findViewById(R.id.tab_icon);
        mRtvTabTitle = (RadioTextView) findViewById(R.id.tab_title);

        mRivTabIcon.setImageDrawable(mTabDrawable);
        mRtvTabTitle.setText(mTabTitle);
        mRtvTabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTitleSize);
        if (mTabTextColors != null) {
            mRtvTabTitle.setTextColor(mTabTextColors);
        }

        mRivTabIcon.setClickable(false);
        mRtvTabTitle.setClickable(false);
        this.setClickable(true);
    }

    public int getLayoutRes() {
        return R.layout.bottom_tab_main;
    }

    @Override
    public boolean performClick() {
        if (!mChecked) {
            setChecked(true);
            return true;
        }
        return super.performClick();
    }

    public TextView getHintTextView() {
        return mTvTabHint;
    }

    @Override
    public int getId() {
        return super.getId();
    }

    public BottomTab setTabTitle(String text) {
        mRtvTabTitle.setText(text);
        return this;
    }

    public BottomTab setTabDrawable(int drawableResId) {
        mRivTabIcon.setImageDrawable(getResources().getDrawable(drawableResId));
        return this;
    }

    public BottomTab setTabDrawable(Drawable drawable) {
        mRivTabIcon.setImageDrawable(drawable);
        return this;
    }

    public BottomTab setHint(String msg) {
        mTvTabHint.setText(msg);
        return this;
    }

    public BottomTab setHint(int msg) {
        mTvTabHint.setText(String.valueOf(msg));
        return this;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @deprecated 请勿使用测方法，此方法本身为内部方法
     */
    public void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            mRivTabIcon.setChecked(mChecked);
            mRtvTabTitle.setChecked(mChecked);
            mTvTabHint.refreshDrawableState();
            mOnCheckedChangeWidgetListener.onCheckedChanged(this, checked);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {

    }
}
