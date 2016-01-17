package org.gofirefly.firefly_android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gofirefly.firefly_android.R;
import org.gofirefly.firefly_android.adapter.ApplicationAdapter;
import org.gofirefly.firefly_android.log.DLog;
import org.gofirefly.firefly_android.tab.BaseFragment;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ApplicationFragment extends BaseFragment {
	
	private static final String DATA_NAME = "name";
	
	private String title = "";
	
	public static ApplicationFragment newInstance(String title, int indicatorColor,
            int dividerColor, int iconResId) {

		DLog.d("ApplicationFragment - newInstance");
		ApplicationFragment f = new ApplicationFragment();
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DLog.d("ApplicationFragment - onActivityCreated");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DLog.d("ApplicationFragment - onCreate");
		
		//get data
		title = getArguments().getString(DATA_NAME);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DLog.d("ApplicationFragment - onCreateView");
		
		//layout
		View view = inflater.inflate(R.layout.fra_application, container, false);

		// View
		StickyListHeadersListView stickyList = (StickyListHeadersListView) view.findViewById(R.id.list_application);
		ApplicationAdapter adapter = new ApplicationAdapter(getActivity());
		stickyList.setAdapter(adapter);

		return view;
	}

	@Override
	public void onDestroy() {
		DLog.d("ApplicationFragment - onDestroy");
		super.onDestroy();
	}



	@Override
	public void onDestroyView() {
		DLog.d("ApplicationFragment - onDestroyView");
		super.onDestroyView();
	}
	
	
	
}
