package jetucker.cmput301asn1.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database for storing notes
 * 
 * @author Jesse Tucker
 *
 */
public class NoteDatabase extends SQLiteOpenHelper
{
	private static final String LOG_TAG = "NoteDatabase::CursorToNote";
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private static final String TABLE_NOTES = "Notes";
	private static final String COLUMN_ID = "uuid";
	private static final String COLUMN_SUBJECT = "subject";
	private static final String COLUMN_CONTENT = "content";
	private static final String COLUMN_DATE_MODIFIED = "dateModified";
	private static final String COLUMN_DATE_CREATED = "dateCreated";
	
	private static final int INDEX_ID = 0;
	private static final int INDEX_SUBJECT = 1;
	private static final int INDEX_CONTENT = 2;
	private static final int INDEX_DATE_MODIFIED = 3;
	private static final int INDEX_DATE_CREATED = 4;
	
	private static final String[] ALL_COLUMNS = {
													COLUMN_ID,
													COLUMN_SUBJECT,
													COLUMN_CONTENT,
													COLUMN_DATE_MODIFIED,
													COLUMN_DATE_CREATED
												};
	
	private static final long SQL_ERROR = -1;
	
	private static final String DATABASE_NAME = "notes.db";
	private static final int DATABASE_VERSION = 4;
	
	private SQLiteDatabase mDatabase = null;
	
	public NoteDatabase(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL("create table "
			      + TABLE_NOTES + "(" 
			      + COLUMN_ID + " text not null, " 
			      + COLUMN_SUBJECT + " text not null, " 
			      + COLUMN_CONTENT + " text not null, "
			      + COLUMN_DATE_MODIFIED + " text not null, "
			      + COLUMN_DATE_CREATED + " text not null"
				  + ");");
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws IllegalStateException
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
		onCreate(db);
	}
	
	public List<Note> GetAllNotes()
	{
		List<Note> notes = new ArrayList<Note>();
		
		if(mDatabase == null)
		{
			this.Open();
		}
		
		Cursor cursor = mDatabase.query(TABLE_NOTES, ALL_COLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast())
		{
			Note newNote = CursorToNote(cursor);
			notes.add(newNote);
			cursor.moveToNext();
		}
		
		cursor.close();
		this.Close();
		return notes;
		
	}
	
	public void Open() throws SQLException
	{
		mDatabase = this.getWritableDatabase();
	}
	
	public void Close()
	{
		this.close();
		mDatabase = null;
	}
	
	public void InsertNote(Note note)
	{
		if(mDatabase == null)
		{
			this.Open();
		}
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_ID, note.GetId().toString());
		values.put(COLUMN_SUBJECT, note.GetSubject());
		values.put(COLUMN_CONTENT, note.GetContent());
		values.put(COLUMN_DATE_MODIFIED, dateFormatter.format(note.GetDateLastModified()));
		values.put(COLUMN_DATE_CREATED, dateFormatter.format(note.GetDateCreated()));
		
		if(mDatabase.insert(TABLE_NOTES, null, values) == SQL_ERROR)
		{
			Log.e(LOG_TAG, "Failed to insert Value!");
		}
		
		this.Close();
	}
	
	public void UpdateNote(Note note)
	{
		if(mDatabase == null)
		{
			this.Open();
		}
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		ContentValues values = new ContentValues();
		
		values.put(COLUMN_ID, note.GetId().toString());
		values.put(COLUMN_SUBJECT, note.GetSubject());
		values.put(COLUMN_CONTENT, note.GetContent());
		values.put(COLUMN_DATE_MODIFIED, dateFormatter.format(note.GetDateLastModified()));
		values.put(COLUMN_DATE_CREATED, dateFormatter.format(note.GetDateCreated()));
		
		try
		{
			mDatabase.update(TABLE_NOTES, 
					 values,
					 COLUMN_ID + " = \"" + note.GetId().toString() + "\"", 
					 null);
		}
		catch(SQLException ex)
		{
			Log.e(LOG_TAG, "Failed to update existing note in table!");
			Log.e(LOG_TAG, ex.toString());
		}
		finally
		{
			this.Close();
		}
	}
	
	public void DeleteNote(Note note) throws IllegalStateException
	{
		
		
		if(mDatabase == null)
		{
			this.Open();
		}
		
		try
		{
			mDatabase.delete(TABLE_NOTES,
			 		 COLUMN_ID + " = " + "\"" + note.GetId().toString() + "\"",
			 		 null);
		}
		catch(SQLException ex)
		{
			Log.e(LOG_TAG, "Failed to delete note from database!");
			Log.e(LOG_TAG, ex.toString());
		}
		finally
		{
			this.Close();
		}
	}
	
	private Note CursorToNote(Cursor cursor)
	{
		String content;
		String subject;
		String idStr;
		String creationDateStr;
		String modifiedDateStr;
		
		content = cursor.getString(INDEX_CONTENT);
		subject = cursor.getString(INDEX_SUBJECT);
		idStr = cursor.getString(INDEX_ID);
		creationDateStr = cursor.getString(INDEX_DATE_CREATED);
		modifiedDateStr = cursor.getString(INDEX_DATE_MODIFIED);
		
		UUID id = UUID.fromString(idStr);
		Date creationDate = null;
		Date modifiedDate = null;
		
		try
		{
			// We are only supporting Canadian locality
			SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.CANADA);
			creationDate = format.parse(creationDateStr);
			modifiedDate = format.parse(modifiedDateStr);
		}
		catch(ParseException e)
		{
			Log.e(LOG_TAG, "- Failed to parse DateTime! Defaulting to current time!");
			
			creationDate = new Date();
			modifiedDate = new Date();
		}
		
		return new Note(subject, content, creationDate, modifiedDate, id);
	}
}
