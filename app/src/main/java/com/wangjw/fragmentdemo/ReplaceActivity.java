package com.wangjw.fragmentdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by wangjw on 16/11/17.
 */

public class ReplaceActivity extends AppCompatActivity {

    private static final String TAG_FIRST = "tag_first";
    private static final String TAG_SECOND = "tag_second";

    private Button mBtnReplace;

    private DemoFragmentFrist mFragmentFirst;
    private DemoFragmentSecond mFragmentSecond;

    private boolean mIsReplaceFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);

        initViews();
    }

    private void initViews() {
        mFragmentFirst = new DemoFragmentFrist();
        mFragmentSecond = new DemoFragmentSecond();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.Container, mFragmentFirst, TAG_FIRST).commit();

        mBtnReplace = (Button) findViewById(R.id.Button_Replace);
        mBtnReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });
    }

    private void replaceFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mIsReplaceFirst) {
            transaction.replace(R.id.Container, mFragmentSecond, TAG_SECOND);
        } else {
            transaction.replace(R.id.Container, mFragmentFirst, TAG_FIRST);
        }

        transaction.addToBackStack(null); //系统会停止（而非销毁）移除的片段,不执行onDestroy()、onDetach()方法,恢复时不执行onAttach()、onCreate()方法
        transaction.commit();

        mIsReplaceFirst = !mIsReplaceFirst;
    }
}
