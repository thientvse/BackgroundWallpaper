package com.softforall.backgroundhd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.softforall.backgroundhd.R;

public class SendCommentFragment extends Fragment {
	
	public SendCommentFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_send_comment, container, false);
         
        return rootView;
    }
}
