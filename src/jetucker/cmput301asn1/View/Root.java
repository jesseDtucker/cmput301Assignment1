package jetucker.cmput301asn1.View;


import jetucker.cmput301asn1.R;
import jetucker.cmput301asn1.ViewModel.NoteService;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class Root extends FragmentActivity 
{
	ViewPager mTabsList = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        
        mTabsList = (ViewPager)this.findViewById(R.id.v_mainTabs);
        mTabsList.setAdapter(new TabAdapter(this.getSupportFragmentManager()));
        mTabsList.setCurrentItem(TabAdapter.NOTE_TAB);
        
        this.getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFC19343));
        this.getActionBar().setTitle(""); // hacky but easier than learning styles atm
    }
	
    @Override
	protected void onResume()
	{
    	super.onResume();
	}
	
    @Override
	protected void onPause()
    {
    	super.onPause();
    }
    
    public void onBackPressed()
    {
    	if(mTabsList.getCurrentItem() == TabAdapter.NOTE_TAB)
    	{
    		super.onBackPressed();
    	}
    	else
    	{
    		mTabsList.setCurrentItem(TabAdapter.NOTE_TAB);
    	}
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case R.id.v_cloudTab:
    		mTabsList.setCurrentItem(TabAdapter.CLOUD_TAB, true);
    		return true;
    	case R.id.v_noteTab:
    		mTabsList.setCurrentItem(TabAdapter.NOTE_TAB, true);
    		return true;
    	case R.id.v_statTab:
    		mTabsList.setCurrentItem(TabAdapter.STAT_TAB, true);
    		return true;
    	case R.id.v_addNote:
    		mTabsList.setCurrentItem(TabAdapter.NOTE_TAB, false);
    		
    		Intent intent = new Intent(this, DetailedNotePreview.class);
    		intent.putExtra(DetailedNotePreview.START_INDEX, NoteService.GetNoteService(getBaseContext()).GetAllNotes().size());
    		
    		this.startActivity(intent);
    		
    		return true;
    	}
    	
    	return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.root, menu);
        return true;
    }
    
}
