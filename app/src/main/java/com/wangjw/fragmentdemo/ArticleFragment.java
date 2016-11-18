package com.wangjw.fragmentdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wangjw on 16/11/18.
 */

public class ArticleFragment extends Fragment {

    public static final String EXTRA_KEY_POS = "pos";

    private TextView mTvValue;

    private int mPos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPos = bundle.getInt(EXTRA_KEY_POS);
        }
        if (savedInstanceState != null) {
            mPos = savedInstanceState.getInt(EXTRA_KEY_POS);
        }

        mTvValue = (TextView) view.findViewById(R.id.TextView_Value1);

        mTvValue.setText(String.format("Select %d", mPos + 1));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_KEY_POS, mPos);
    }

    public void updateArticleView(int position) {
        mTvValue.setText(String.format("Select %d", position + 1));
    }
}
