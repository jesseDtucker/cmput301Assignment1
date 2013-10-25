package jetucker.cmput301asn1.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jetucker.cmput301asn1.R;
import jetucker.cmput301asn1.ViewModel.NoteViewModel;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class DetailedNoteFragment extends Fragment implements OnDateSetListener, OnClickListener
{
	private static final String LOG_TAG = "View::DetailedNoteFragment";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private NoteViewModel mNote = null;
	private Button mDateButton = null;
	
	public NoteViewModel GetNote() { return mNote; }
	public void SetNote(NoteViewModel note) { mNote = note; }
	
	@Override
	public View onCreateView(LayoutInflater inflator,
							 ViewGroup container,
							 Bundle savedInstanceState)
	 {
		ViewGroup result = (ViewGroup)(inflator.inflate(R.layout.note_detailed, container, false));
		
		mDateButton = (Button)result.findViewById(R.id.v_noteDate);
		TextView subject = (TextView)result.findViewById(R.id.v_noteDetails_subject);
		TextView content = (TextView)result.findViewById(R.id.v_noteDetails_content);
		
		mDateButton.setOnClickListener(this);
		
		content.addTextChangedListener(new ContentListener(mNote));
		subject.addTextChangedListener(new SubjectListener(mNote));
		
		SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_FORMAT);
		
		mDateButton.setText(dateFormater.format(mNote.GetDate()));
		subject.setText(mNote.GetSubject());
		content.setText(mNote.GetContent());
		
		return result;
	 }
	
	@Override
	public void onClick(View v) 
	{
		final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
		DatePickerDialog dateDialog = new DatePickerDialog(v.getContext(), this, year, month, day);
		dateDialog.show();
	}
	
	@Override
	public void onDateSet(DatePicker datePicker, int year, int month, int day) 
	{
		SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_FORMAT);
		
		try 
		{
			// month provided is off by one...
			String dateStr = year + "-" + (month + 1) + "-" + day;
			Date newDate = dateFormater.parse(dateStr);
			mDateButton.setText(dateStr);
			mNote.SetDate(newDate);
		} 
		catch (ParseException ex) 
		{
			Log.e(LOG_TAG, "Failed to parse Date!");
			Log.e(LOG_TAG, ex.toString());
		}
	}
	
	/// There must be a better way to implement this... would
	/// be much easier if java supported Lambda's... or delegates... or function pointers...
	private class ContentListener implements TextWatcher
	{
		private NoteViewModel mNote = null;
		
		public ContentListener(NoteViewModel note)
		{
			mNote = note;
		}

		@Override
		public void afterTextChanged(Editable s) 
		{
			// TODO Auto-generated method stub
			mNote.SetContent(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) 
		{
			// TODO Auto-generated method stub
			// Unused
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) 
		{
			// TODO Auto-generated method stub
			// Unused
		}
	}
	
	private class SubjectListener implements TextWatcher
	{
		private NoteViewModel mNote = null;
		
		public SubjectListener(NoteViewModel note)
		{
			mNote = note;
		}

		@Override
		public void afterTextChanged(Editable s) 
		{
			// TODO Auto-generated method stub
			mNote.SetSubject(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) 
		{
			// TODO Auto-generated method stub
			// Unused
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) 
		{
			// TODO Auto-generated method stub
			// Unused
		}
	}

	
}
