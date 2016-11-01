package com.wangjw.fragmentdemo;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FIRST = "tag_first";
    private static final String TAG_SECOND = "tag_second";

    private RadioGroup mRadioGroup;

    private DemoFragmentFrist mFragmentFirst;
    private DemoFragmentSecond mFragmentSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);

        FragmentManager fm = getSupportFragmentManager();
        mFragmentFirst = (DemoFragmentFrist) fm.findFragmentByTag(TAG_FIRST);
        mFragmentSecond = (DemoFragmentSecond) fm.findFragmentByTag(TAG_SECOND);

        changeFragment(1);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.RadioButton1) {
                    changeFragment(1);
                } else {
                    changeFragment(2);
                }
            }
        });
    }

    private void changeFragment(int type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        hideFragment(ft, type);
        showFragment(ft, type);
        ft.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction ft, int type) {
        if (type != 1 && mFragmentFirst != null) {
            ft.hide(mFragmentFirst);
        }
        if (type != 2 && mFragmentSecond != null) {
            ft.hide(mFragmentSecond);
        }
    }

    private void showFragment(FragmentTransaction ft, int type) {
        if (type == 1) {
            if (mFragmentFirst == null) {
                mFragmentFirst = new DemoFragmentFrist();
                ft.add(R.id.Container, mFragmentFirst, TAG_FIRST);
            } else {
                ft.show(mFragmentFirst);
            }
        } else if (type == 2) {
            if (mFragmentSecond == null) {
                mFragmentSecond = new DemoFragmentSecond();
                ft.add(R.id.Container, mFragmentSecond, TAG_SECOND);
            } else {
                ft.show(mFragmentSecond);
            }
        }
    }
}
