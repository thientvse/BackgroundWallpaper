package com.softforall.backgroundhd.dcfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softforall.backgroundhd.R;
import com.softforall.backgroundhd.Utils.Utils;

public class GlosbeFragment extends Fragment {
	
	
	private TextView txtTitle;
	private Context mContext;
	
	public static GlosbeFragment newInstance() {
		GlosbeFragment f = new GlosbeFragment();
		return f;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_globse, container, false);
        
        mContext = getActivity();
        
        txtTitle = (TextView) rootView.findViewById(R.id.txtLabel);
        txtTitle.setTypeface(Utils.getTypefaceRegular(mContext));
         
        return rootView;
    }
}
