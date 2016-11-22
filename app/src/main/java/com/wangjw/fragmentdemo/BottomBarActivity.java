package com.wangjw.fragmentdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.wangjw.bottombar.widget.BottomTabGroup;

/**
 * Created by wangjw on 16/11/22.
 */

public class BottomBarActivity extends AppCompatActivity {

    private static final String TAG_FIRST = "tag_first";
    private static final String TAG_SECOND = "tag_second";

    private BottomTabGroup mBottomTabGroup;

    private DemoFragmentFrist mFragmentFirst;
    private DemoFragmentSecond mFragmentSecond;

    private int mCurrTabId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        initViews();
    }

    private void initViews() {
        mBottomTabGroup = (BottomTabGroup) findViewById(R.id.BottomTabGroup);
        mBottomTabGroup.setOnCheckedChangeListener(new BottomTabGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(BottomTabGroup root, int checkedId) {
                changeTab(checkedId);
            }
        });

        checkExistedFragments();
        changeTab(0);
    }

    private void  checkExistedFragments() {
        FragmentManager fm = getSupportFragmentManager();
        mFragmentFirst = (DemoFragmentFrist) fm.findFragmentByTag(TAG_FIRST);
        mFragmentSecond = (DemoFragmentSecond) fm.findFragmentByTag(TAG_SECOND);
    }

    private void changeTab(int tabId) {
        if (mCurrTabId > 0 && mCurrTabId == tabId) {
            return;
        }

        if (tabId <= 0) {
            tabId = R.id.BottomTab_Main_Bus;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideTabFragments(ft, tabId);
        addOrShowTabFragment(ft, tabId);
        ft.commitAllowingStateLoss();

        mBottomTabGroup.check(tabId);
        mCurrTabId = tabId;
    }

    private void hideTabFragments(FragmentTransaction ft, int exclusiveTabId) {
        if (exclusiveTabId != R.id.BottomTab_Main_Bus && mFragmentFirst != null)
            ft.hide(mFragmentFirst);
        if (exclusiveTabId != R.id.BottomTab_Main_Mine && mFragmentSecond != null)
            ft.hide(mFragmentSecond);
    }

    private void addOrShowTabFragment(FragmentTransaction ft, int tabId) {
        if (tabId == R.id.BottomTab_Main_Bus) {
            if (mFragmentFirst == null) {
                mFragmentFirst = new DemoFragmentFrist();
                ft.add(R.id.FrameLayout_Container, mFragmentFirst, TAG_FIRST);
            } else {
                ft.show(mFragmentFirst);
            }
        } else if (tabId == R.id.BottomTab_Main_Mine) {
            if (mFragmentSecond == null) {
                mFragmentSecond = new DemoFragmentSecond();
                ft.add(R.id.FrameLayout_Container, mFragmentSecond, TAG_SECOND);
            } else {
                ft.show(mFragmentSecond);
            }
        }
    }
}
