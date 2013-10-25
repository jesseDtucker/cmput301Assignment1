package jetucker.cmput301asn1.View;

import java.util.ArrayList;
import java.util.List;

import jetucker.cmput301asn1.R;
import jetucker.cmput301asn1.ViewModel.INoteListListener;
import jetucker.cmput301asn1.ViewModel.NoteService;
import jetucker.cmput301asn1.ViewModel.NoteViewModel;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TabFragment extends Fragment implements INoteListListener, OnItemClickListener
{
	private int mResource = 0;
	private NoteListAdapter mNoteAdapter = null;
	private NoteService mNoteService = null;
	
	private TextView mLogCount = null;
	private TextView mWordCount = null;
	private TextView mCharacterCount = null;
	private ListView mWordList = null;
	private List<TextView> mCloudText = null;
	
	public int GetResourceId() { return mResource; }
	public void SetResourceId(int id) { mResource = id; }
	
	@Override
	public View onCreateView(LayoutInflater inflator,
							 ViewGroup container,
							 Bundle savedInstanceState)
	 {
		ViewGroup result = (ViewGroup)(inflator.inflate(mResource, container, false));
		
		if(mNoteService == null)
		{
			FetchNoteService();
		}
		
		switch(mResource)
		{
		case R.layout.note_list_view:
			SetupNoteList(result);
			break;
		case R.layout.cloud_view:
			SetupCloud(result);
			break;
		case R.layout.statistic_view:
			SetupStatPage(result);
			break;
		default:
			throw new IllegalStateException("Initializing tab fragment with invalid resource ID!");
		}
		
		return result;
	 }
	
	private void SetupNoteList(ViewGroup view)
	{
		Context currentContext = this.getActivity().getBaseContext();
		ListView noteList = (ListView)view.findViewById(R.id.v_noteList);
		mNoteAdapter = new NoteListAdapter(currentContext
														 , R.layout.note_preview
														 , mNoteService.GetAllNotes());
		noteList.setAdapter(mNoteAdapter);
		
		noteList.setOnItemClickListener(this);
		
		// must force this to run the first time to ensure sorting.
		mNoteAdapter.notifyDataSetChanged();
	}
	
	private void SetupCloud(ViewGroup view)
	{
		mCloudText = new ArrayList<TextView>();
		FrameLayout cloudFrame = (FrameLayout)view.findViewById(R.id.v_cloudFrame);
		
		for(int i = 0 ; i < 100 ; ++i)
		{
			TextView text = new TextView(view.getContext());
			mCloudText.add(text);
			cloudFrame.addView(text);
		}
		
		UpdateCloud();
	}
	
	private void SetupStatPage(ViewGroup view)
	{
		mLogCount = (TextView)view.findViewById(R.id.v_logCount);
		mWordCount = (TextView)view.findViewById(R.id.v_wordCount);
		mCharacterCount = (TextView)view.findViewById(R.id.v_characterCount);
		mWordList = (ListView)view.findViewById(R.id.v_frequentWordList);
		
		UpdateStats();
	}
	
	private void UpdateStats()
	{
		mLogCount.setText(((Integer)(mNoteService.GetTotalLogEntries())).toString());
		mWordCount.setText(((Integer)(mNoteService.GetTotalWords())).toString());
		mCharacterCount.setText(((Integer)(mNoteService.GetTotalCharacters())).toString());
		mWordList.setAdapter(new ArrayAdapter<String>(	mWordList.getContext(),
														android.R.layout.simple_list_item_1, 
														mNoteService.GetTop100Words()));
	}
	
	private void UpdateCloud()
	{
		List<String> words = mNoteService.GetTop100Words();
		
		float curX = 0;
		float curY = -500;
		
		//clear the existing text views from the view
		for(TextView txtView : mCloudText)
		{
			txtView.setVisibility(View.INVISIBLE);
		}
		
		for(int i = 0 ; i < 100 ; ++i)
		{
			if(i >= words.size())
			{
				break;
			}
			String word = words.get(i);
			TextView txtView = mCloudText.get(i);
			
			txtView.setText(word);
			
			float scale = 1.0f + (100.0f - i) / 25.0f;
			txtView.setScaleX(scale);
			txtView.setScaleY(scale);
			txtView.setVisibility(View.VISIBLE);
			
			float nextX = (float) (curX + word.length() * 20.0f);
			if(nextX > 1000.0f)
			{
				curX = 0;
				curY += 20 * scale;
			}
			
			txtView.setX(curX);
			txtView.setY(curY);
			
			txtView.setGravity(Gravity.CENTER);
			
			curX = nextX;
		}
	}
	
	private void FetchNoteService()
	{
		// can't place this in constructor or on start... just calling as needed
		mNoteService = NoteService.GetNoteService(this.getActivity().getBaseContext());
		mNoteService.Subscribe(this);
	}
	
	@Override
	public void NoteListChanged() 
	{
		// it is possible this may be called before the components that need refreshing have been initialized
		if(mNoteAdapter != null)
		{
			mNoteAdapter.notifyDataSetChanged();
		}
		
		if( mLogCount != null &&
			mWordCount != null &&
			mCharacterCount != null &&
			mWordList != null)
		{
			UpdateStats();
		}
		
		if(mCloudText != null)
		{
			UpdateCloud();
		}
	}
	@Override
	public void onItemClick( AdapterView<?> parent
			   , View view
			   , int position
			   , long id)
	{
		NoteViewModel note = mNoteAdapter.getItem(position);
		NoteService noteService = NoteService.GetNoteService(this.getActivity().getBaseContext());

		// Note: should really just pass the note id around instead, but UUID's are likely going
		// to require serialization and deserialization to send in a bundle...
		int noteIndex = noteService.GetAllNotes().indexOf(note);;
		
		Intent intent = new Intent(view.getContext(), DetailedNotePreview.class);
		intent.putExtra(DetailedNotePreview.START_INDEX, noteIndex);
		
		view.getContext().startActivity(intent);
	}
}
