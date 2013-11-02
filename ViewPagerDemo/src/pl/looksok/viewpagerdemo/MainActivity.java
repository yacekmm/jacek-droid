package pl.looksok.viewpagerdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class MainActivity extends FragmentActivity {

	private MyPagerAdapter pageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pageAdapter = new MyPagerAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager)findViewById(R.id.myViewPager);
		pager.setAdapter(pageAdapter);
		
		pager.setOnPageChangeListener(pageChangeListener);
	}
	
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		
		int currentPosition = 0;
		
		@Override
		public void onPageSelected(int newPosition) {
			
			FragmentLifecycle fragmentToHide = (FragmentLifecycle)pageAdapter.getItem(currentPosition);
			fragmentToHide.onPauseFragment();

			FragmentLifecycle fragmentToShow = (FragmentLifecycle)pageAdapter.getItem(newPosition);
			fragmentToShow.onResumeFragment();
			
			currentPosition = newPosition;
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }
		
		public void onPageScrollStateChanged(int arg0) { }
	};

}
