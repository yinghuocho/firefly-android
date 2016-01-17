package org.gofirefly.firefly_android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.gofirefly.firefly_android.R;
import org.gofirefly.firefly_android.log.DLog;
import org.gofirefly.firefly_android.tab.BaseFragment;


public class HomeFragment extends BaseFragment {
	
	private static final String DATA_NAME = "name";
	
	private String title = "";
	
	public static HomeFragment newInstance(String title, int indicatorColor, int dividerColor, int iconResId) {
		DLog.d("HomeFragment - newInstance");
		HomeFragment f = new HomeFragment();
		f.setTitle(title);
		f.setIndicatorColor(indicatorColor);
		f.setDividerColor(dividerColor);
		f.setIconResId(iconResId);

		
		//pass data
		Bundle args = new Bundle();
        args.putString(DATA_NAME, title);
        f.setArguments(args);
		
        return f;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DLog.d("HomeFragment - onCreate");
		
		//get data
		title = getArguments().getString(DATA_NAME);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		DLog.d("HomeFragment - onCreateView");
		return inflater.inflate(R.layout.fra_service_switch, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		DLog.d("HomeFragment - onViewCreated");
		TextView txtName = (TextView) view.findViewById(R.id.txtName);
		txtName.setText(title);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DLog.d("HomeFragment - onActivityCreated");
	}

	@Override
	public void onDestroy() {
		DLog.d("HomeFragment - onDestroy");
		super.onDestroy();
	}



	@Override
	public void onDestroyView() {
		DLog.d("HomeFragment - onDestroyView");
		super.onDestroyView();
	}
	
	
	
}
