package jetucker.cmput301asn1.View;

import jetucker.cmput301asn1.R;
import jetucker.cmput301asn1.ViewModel.NoteService;
import jetucker.cmput301asn1.ViewModel.NoteViewModel;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

public class DetailedNotePreview extends FragmentActivity implements OnPageChangeListener
{
	public static final String START_INDEX = "START_INDEX";
	private ViewPager mNoteViewer = null;
	private int mPreviousIndex = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
		this.getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFC19343));
        this.getActionBar().setTitle(""); // hacky but easier than learning styles atm
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_note_viewer);
        
        mNoteViewer = (ViewPager) this.findViewById(R.id.v_detailedNoteViewer);
        int noteIndex = this.getIntent().getIntExtra(START_INDEX, 0);
        NoteService noteService = NoteService.GetNoteService(this.getBaseContext());
        
        mNoteViewer.setOnPageChangeListener(this);
        
        if(noteService.GetAllNotes().size() <= noteIndex)
        {
        	// then we must add a new note to the list
        	NoteViewModel newNote = noteService.CreateNote("subject", "content");
        	noteIndex = noteService.GetAllNotes().indexOf(newNote);
        }
        
        DetailedNoteAdapter detailedNoteAdapter = new DetailedNoteAdapter( this.getSupportFragmentManager()
        											   , noteService);
        
        mNoteViewer.setAdapter(detailedNoteAdapter);
        mNoteViewer.setCurrentItem(noteIndex, false);
    }
	
	public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case R.id.v_delete:
    		DeleteNoteAndExit();
    		return true;
    	case R.id.v_done:
    		SaveNoteAndExit();
    		return true;
    	}
    	
    	return false;
    }
	
	public void onBackPressed()
	{
		SaveNote();
		super.onBackPressed();
	}
	
	private void DeleteNoteAndExit()
	{
		NoteService noteService = NoteService.GetNoteService(this.getBaseContext());
		NoteViewModel currentNote = noteService.GetAllNotes().get(mNoteViewer.getCurrentItem());
		
		noteService.DeleteNote(currentNote);
		super.onBackPressed();
	}
	
	private void SaveNote()
	{
		NoteService noteService = NoteService.GetNoteService(this.getBaseContext());
		NoteViewModel currentNote = noteService.GetAllNotes().get(mNoteViewer.getCurrentItem());
		
		noteService.SaveNote(currentNote);
	}
	
	private void SaveNoteAndExit()
	{
		SaveNote();
		this.onBackPressed();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notedetailsmenu, menu);
        return true;
    }

	@Override
	public void onPageScrollStateChanged(int arg0) 
	{ }

	@Override
	public void onPageScrolled(int newIndex, float arg1, int arg2) 
	{
		NoteService noteService = NoteService.GetNoteService(getBaseContext());
		NoteViewModel note = noteService.GetAllNotes().get(mPreviousIndex);
		noteService.SaveNote(note);
		mPreviousIndex = newIndex;
	}

	@Override
	public void onPageSelected(int arg0) 
	{ } 
}
