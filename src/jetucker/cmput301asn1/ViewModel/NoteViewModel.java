package jetucker.cmput301asn1.ViewModel;

import java.util.Date;

import jetucker.cmput301asn1.Model.Note;

/**
 * View model for the Note model class. Just a simple wrapper as this program is trivial.
 * 
 * @author Jesse
 */
public class NoteViewModel 
{
	private Note mNote = null;
	
	protected NoteViewModel(Note note)
	{
		mNote = note;
	}
	
	public void SetContent(String value)
	{
		mNote.SetContent(value);
	}
	public String GetContent()
	{
		return mNote.GetContent();
	}
	
	public void SetSubject(String value)
	{
		mNote.SetSubject(value);
	}
	public String GetSubject()
	{
		return mNote.GetSubject();
	}
	
	public void SetDate(Date value)
	{
		mNote.SetDate(value);
	}
	public Date GetDate()
	{
		if(mNote.GetDateCreated().equals(mNote.GetDateLastModified()))
		{
			return mNote.GetDateCreated();
		}
		else
		{
			return mNote.GetDateLastModified();
		}
	}
	
	protected Note GetNoteModel()
	{
		return mNote;
	}
}
