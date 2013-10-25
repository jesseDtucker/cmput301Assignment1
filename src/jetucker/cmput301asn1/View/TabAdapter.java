package jetucker.cmput301asn1.View;

import jetucker.cmput301asn1.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabAdapter extends FragmentStatePagerAdapter
{
	
	protected final static int TAB_COUNT = 3;
	protected final static int NOTE_TAB = 1;
	protected final static int CLOUD_TAB = 2;
	protected final static int STAT_TAB = 0;

	public TabAdapter(FragmentManager fm) 
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int index) 
	{
		TabFragment result = new TabFragment();
		
		switch(index)
		{
		case NOTE_TAB:
			result.SetResourceId(R.layout.note_list_view);
			break;
		case CLOUD_TAB:
			result.SetResourceId(R.layout.cloud_view);
			break;
		case STAT_TAB:
			result.SetResourceId(R.layout.statistic_view);
			break;
			default:
				throw new IllegalArgumentException("Invalid tab index specified!");
		}
		
		return result;
	}

	@Override
	public int getCount() 
	{
		return TAB_COUNT;
	}

}
