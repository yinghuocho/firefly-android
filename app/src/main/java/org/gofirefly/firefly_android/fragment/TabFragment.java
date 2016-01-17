package org.gofirefly.firefly_android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gofirefly.firefly_android.R;
import org.gofirefly.firefly_android.adapter.TabFragmentPagerAdapter;
import org.gofirefly.firefly_android.tab.BaseFragment;
import org.gofirefly.firefly_android.tab.SlidingTabLayout;

import java.util.LinkedList;

public class TabFragment extends Fragment {

	private SlidingTabLayout tabs;
	private ViewPager pager;
	private FragmentPagerAdapter adapter;
	
	public static Fragment newInstance(){
		TabFragment f = new TabFragment();
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frg_tab, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		//adapter
		final LinkedList<BaseFragment> fragments = getFragments();
		adapter = new TabFragmentPagerAdapter(getFragmentManager(), fragments);
		//pager
		pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(adapter);
		//tabs
		tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			
			@Override
			public int getIndicatorColor(int position) {
				return fragments.get(position).getIndicatorColor();
			}
			
			@Override
			public int getDividerColor(int position) {
				return fragments.get(position).getDividerColor();
			}
		});
		tabs.setBackgroundResource(R.color.colorPrimary);
		tabs.setCustomTabView(R.layout.tab_title, R.id.txtTabTitle, 0);
		tabs.setViewPager(pager);
		
	}
	
	private LinkedList<BaseFragment> getFragments(){
		int indicatorColor = Color.BLUE;
		int dividerColor = Color.TRANSPARENT;
		
		LinkedList<BaseFragment> fragments = new LinkedList<BaseFragment>();
		fragments.add(HomeFragment.newInstance("Home", indicatorColor, dividerColor, android.R.drawable.ic_dialog_info));
		fragments.add(ApplicationFragment.newInstance("Application", Color.CYAN, dividerColor, android.R.drawable.ic_dialog_dialer));
		return fragments;
	}
	
}
