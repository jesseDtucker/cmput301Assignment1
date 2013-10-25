package jetucker.cmput301asn1.Model;

import java.util.Date;
import java.util.UUID;

/**
 * Note structure
 * 
 * @author Jesse Tucker
 *
 */
public class Note 
{
	private String mSubject = null;
	private String mContent = null;
	private Date mDateCreated = null;
	private Date mDateModified = null;
	private UUID mId = null;
	
	/**
	 * Creates a new note object with the current date
	 */
	public Note(String subject, String content)
	{
		// default constructor gives current date time
		mDateCreated = new Date();
		mDateModified = new Date();
		
		SetSubject(subject);
		SetContent(content);
		
		mId = UUID.randomUUID();
	}
	
	/**
	 * Used internally during initialization and allows explicit setting of Date's
	 */
	protected Note(String subject, String content, Date dateCreated, Date dateModified, UUID id)
	{
		mDateCreated = dateCreated;
		mDateModified = dateModified;
		
		SetSubject(subject);
		SetContent(content);
		
		mId = id;
	}
	
	public String GetSubject()
	{
		return mSubject;
	}
	public void SetSubject(String value)
	{
		mSubject = value != null ? value : ""; // ensure non null value
	}
	
	public void SetDate(Date date)
	{
		this.mDateModified = date;
	}
	
	public String GetContent()
	{
		return mContent;
	}
	public void SetContent(String value)
	{
		mContent = value != null ? value : ""; // ensure non null value
	}
	
	public Date GetDateLastModified()
	{
		return mDateModified;
	}
	
	public Date GetDateCreated()
	{
		return mDateCreated;
	}
	
	public UUID GetId()
	{
		return mId;
	}
}
