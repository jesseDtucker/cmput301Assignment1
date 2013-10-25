package jetucker.cmput301asn1.View;

import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import jetucker.cmput301asn1.R;
import jetucker.cmput301asn1.ViewModel.NoteViewModel;

public class NoteListAdapter extends ArrayAdapter<NoteViewModel>
{
	private static final int CONTENT_MAX_LENGTH = 65;
	
	public NoteListAdapter(Context context, 
						   int resource,
						   List<NoteViewModel> objects) 
	{
		super(context, resource, objects);
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View noteItem = null;
		
		if(convertView == null)
		{
			LayoutInflater aInflater = LayoutInflater.from(getContext());

			noteItem = aInflater.inflate(R.layout.note_preview,parent,false);
		}
		else
		{
			noteItem = convertView;
		}
		
		NoteViewModel note = this.getItem(position);
		
		TextView subject=(TextView)noteItem.findViewById(R.id.v_subject);
		TextView content=(TextView)noteItem.findViewById(R.id.v_content);
		
		subject.setText(note.GetSubject());
		String contentStr = note.GetContent();
		if(contentStr.length() > CONTENT_MAX_LENGTH)
		{
			contentStr = contentStr.substring(0, CONTENT_MAX_LENGTH) + "...";
		}
		content.setText(contentStr);
		
		return noteItem;
	}
	
	@Override
	public void notifyDataSetChanged()
	{
		setNotifyOnChange(false);
	    sort(new NoteDateComparer());
	    setNotifyOnChange(true);
	    super.notifyDataSetChanged();
	}
	
	private class NoteDateComparer implements Comparator<NoteViewModel>
	{
		@Override
		public int compare(NoteViewModel note1, NoteViewModel note2) 
		{
			return -note1.GetDate().compareTo(note2.GetDate());
		}
	}
	
}
