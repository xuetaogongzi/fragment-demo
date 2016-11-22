package com.wangjw.bottombar;

import android.widget.Checkable;

/**
 * Created by wangjw on 16/11/21.
 */

public interface BottomTabImpl extends Checkable {

    interface OnCheckedChangeListener {
        void onCheckedChanged(BottomTabImpl buttonView, boolean isChecked);
    }

    int getId();

    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener);

}
