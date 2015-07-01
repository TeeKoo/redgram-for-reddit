package com.matie.redgram.ui.common.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.home.HomeComponent;

/**
 * Created by matie on 09/06/15.
 */
public abstract class BaseFragment extends Fragment {

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupComponent(((MainActivity)getActivity()).component());
    }

    protected abstract void setupComponent(AppComponent component);

//    public abstract HomeComponent component();
}