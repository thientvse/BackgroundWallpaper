package com.softforall.backgroundhd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.softforall.backgroundhd.R;

public class HistoryFragment extends Fragment {
	
	public HistoryFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
         
        return rootView;
    }
}
