package jetucker.cmput301asn1.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;

import jetucker.cmput301asn1.Model.Note;
import jetucker.cmput301asn1.Model.NoteDatabase;

/**
 * Service provides data to the view.
 * 
 * Static methods are not thread safe!
 * 
 * @author Jesse
 */
public class NoteService 
{
	private static NoteService mServiceInstance = null;
	
	private List<NoteViewModel> mNotes = null;
	private Set<INoteListListener> mListeners = new HashSet<INoteListListener>();
	private boolean mIsStatsDirty = true;
	private int mTotalCharacters;
	private int mTotalWords;
	private List<String> mTop100Words;
	
	public static NoteService GetNoteService(Context context)
	{
		if(mServiceInstance == null)
		{
			mServiceInstance = new NoteService(context);
		}
		
		return mServiceInstance;
	}
	
	private NoteDatabase mNoteDB = null;
	
	private NoteService(Context context)
	{
		mNoteDB = new NoteDatabase(context);
	}
	
	public NoteViewModel CreateNote(String subject, String content)
	{
		Note newNote = new Note(subject, content);
		mNoteDB.InsertNote(newNote);
		NoteViewModel noteVM = new NoteViewModel(newNote);
		mNotes.add(noteVM);
		
		this.NotifyListeners();
		mIsStatsDirty = true;
		
		return noteVM;
	}
	
	public void SaveNote(NoteViewModel note)
	{
		mNoteDB.UpdateNote(note.GetNoteModel());
		
		mIsStatsDirty = true;
		this.NotifyListeners();
	}
	
	public void DeleteNote(NoteViewModel note)
	{
		mNoteDB.DeleteNote(note.GetNoteModel());
		if(mNotes != null)
		{
			// should never be null at this point, but it is technically valid by contract
			mNotes.remove(note);
		}
		
		this.NotifyListeners();
		mIsStatsDirty = true;
	}
	
	public List<NoteViewModel> GetAllNotes()
	{
		return GetNotes();
	}
	
	public int GetTotalCharacters()
	{
		if(mIsStatsDirty)
		{
			CalculateStatistics();
		}
		
		return mTotalCharacters;
	}
	
	public int GetTotalWords()
	{
		if(mIsStatsDirty)
		{
			CalculateStatistics();
		}
		
		return mTotalWords;
	}
	
	public List<String> GetTop100Words()
	{
		if(mIsStatsDirty)
		{
			CalculateStatistics();
		}
		
		return mTop100Words;
	}
	
	private void CalculateStatistics()
	{
		mTotalWords = 0;
		mTotalCharacters = 0;
		Map<String, Integer> words = new HashMap<String, Integer>();
		
		List<NoteViewModel> notes = GetNotes();
		for(NoteViewModel note : notes)
		{
			String[] contentWords = note.GetContent().split(" ");
			String[] subjectWords = note.GetSubject().split(" ");
			
			mTotalWords += contentWords.length;
			mTotalWords += subjectWords.length;
			
			mTotalCharacters += note.GetContent().length();
			mTotalCharacters += note.GetSubject().length();
			
			CountWords(contentWords, words);
			CountWords(subjectWords, words);
		}
		
		mTop100Words = CalculateTop100Words(words);
		
		mIsStatsDirty = false;
	}
	
	private void CountWords(String[] words, Map<String, Integer> wordCount)
	{
		for(String word : words)
		{
			word = word.toLowerCase();
			if(wordCount.containsKey(word))
			{
				Integer count = wordCount.get(word);
				count++;
				wordCount.put(word, count);
			}
			else
			{
				wordCount.put(word, 1);
			}
		}
	}
	
	private List<String> CalculateTop100Words(Map<String, Integer> wordCount)
	{
		StringCountComparator countComparator = new StringCountComparator();
		
		Set<Map.Entry<String,Integer>> entrySet = wordCount.entrySet();
		List<Map.Entry<String,Integer>> entries = new ArrayList<Map.Entry<String,Integer>>();
		
		for(Map.Entry<String,Integer> entry : entrySet)
		{
			entries.add(entry);
		}
		
		Collections.sort(entries, countComparator);
		
		int count = 0;
		List<String> results = new ArrayList<String>(100);
		
		for (Map.Entry<String,Integer> entry : entries)
		{
		    if(count >= 100)
		    {
		    	break;
		    }
		    
		    results.add(entry.getKey());
		}
		
		return results;
	}
	
	public int GetTotalLogEntries()
	{
		return GetNotes().size();
	}
	
	public void Subscribe(INoteListListener listener)
	{
		this.mListeners.add(listener);
	}
	
	public void Unsubscribe(INoteListListener listener)
	{
		this.mListeners.remove(listener);
	}
	
	private List<NoteViewModel> GetNotes()
	{
		if(mNotes == null)
		{
			List<Note> notes = mNoteDB.GetAllNotes();
			
			List<NoteViewModel> results = new ArrayList<NoteViewModel>(notes.size());
			
			for(Note note : notes)
			{
				results.add(new NoteViewModel(note));
			}
			
			mNotes = results;
		}
		
		return mNotes;
	}
	
	private void NotifyListeners()
	{
		for(INoteListListener listener : this.mListeners)
		{
			listener.NoteListChanged();
		}
	}
	
	private class StringCountComparator implements Comparator<Map.Entry<String,Integer>>
	{
		@Override
		public int compare(Entry<String, Integer> arg0,
				Entry<String, Integer> arg1) 
		{
			return arg1.getValue() - arg0.getValue();
		}
	}
}
