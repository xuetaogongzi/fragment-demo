package com.wangjw.fragmentdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjw on 16/11/18.
 */

public class HeadlinesFragment extends ListFragment {

    OnHeadlineSelectedListener mCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallBack = (OnHeadlineSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnHeadlineSelectListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, getItems()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallBack.onArticleSelected(position);
    }

    private List<String> getItems() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("Item " + (i + 1));
        }
        return list;
    }

    public interface OnHeadlineSelectedListener {
        void onArticleSelected(int position);
    }

}
