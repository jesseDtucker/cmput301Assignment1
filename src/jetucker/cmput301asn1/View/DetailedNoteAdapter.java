package jetucker.cmput301asn1.View;

import jetucker.cmput301asn1.ViewModel.NoteService;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DetailedNoteAdapter extends FragmentStatePagerAdapter
{
	NoteService mNoteService = null;
	
	public DetailedNoteAdapter(FragmentManager fm, NoteService noteService) 
	{
		super(fm);
		mNoteService = noteService;
	}

	@Override
	public Fragment getItem(int index) 
	{
		DetailedNoteFragment result = new DetailedNoteFragment();
		result.SetNote(mNoteService.GetAllNotes().get(index));
		return result;
	}

	@Override
	public int getCount()
	{
		return mNoteService.GetAllNotes().size();
	}

}
