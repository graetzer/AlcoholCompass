package com.alcoholcompass;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
	private TabsAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
	    bar.setDisplayShowHomeEnabled(false);
	    
		mPagerAdapter = new TabsAdapter(this, (ViewPager) findViewById(R.id.pager));

		//Add tabs to the Actionbar
		mPagerAdapter.addTab(
				bar.newTab().setText(R.string.fragment_navigation_title),
				NavigatorFragment.class, null);
		
		mPagerAdapter.addTab(bar.newTab().setText(R.string.fragment_showtell_title),
				ShowTellFragment.class, null);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar()
                .getSelectedNavigationIndex());
    }

	private static class TabsAdapter extends FragmentPagerAdapter implements
			ViewPager.OnPageChangeListener, ActionBar.TabListener {
		
		private final Context mContext;
		private final ActionBar mBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(FragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mBar = activity.getActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<? extends Fragment> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

		@Override
		public void onPageSelected(int position) {
			mBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) { }

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) { }

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) { }
	}
}
