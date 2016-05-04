package com.softforall.backgroundhd.dcfrgment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.softforall.backgroundhd.R;
import com.softforall.backgroundhd.UI.SelectableRoundedImageView;

public class FragmentLibraryFragment extends Fragment  {

    private ListView lstLibrary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lstLibrary = (ListView) view.findViewById(R.id.lst_library);
        
     // You can set image with resource id.
        SelectableRoundedImageView iv1 = (SelectableRoundedImageView) view.findViewById(R.id.image0);
        iv1.setScaleType(ScaleType.CENTER_CROP);
        iv1.setOval(true);
        iv1.setImageResource(R.drawable.header_image);
        
        
    }

}