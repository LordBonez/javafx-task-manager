
package application;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements TaskInterface, Serializable
{
	//Used for version control of object
	private static final long serialVersionUID = 4806886736215770273L;
	
	//Fields
	private String name;
	private LocalDateTime duedate;
	private boolean status;
	private String note;
	
	/**
	* Setter method for the name
	* @param n string of name
	*/
	public void setName(String n)
	{
		name = n;
	}
	
	/**
	* Getter method for the name 
	* @return name
	*/
	public String getName()
	{
		return name;
	}
	
	/**
	* Setter method for set the due date
	* @param d local date time 
	*/
	public void setDueDate(LocalDateTime d)
	{
		duedate = d;
	}

	/**
	* Getter method for the local date time
	* @return duedate
	*/
	public LocalDateTime getDueDate()
	{
		return duedate;
	}

	/**
	* setter method for status
	* @param s boolean for status
	*/
	public void setStatus(boolean s)
	{
		status = s;
	}
	
	/**
	* getter method for the status
	* @return status
	*/
	public boolean getStatus()
	{
		return status;
	}
	
	/**
	* setter method for note
	* @param n String of notes
	*/
	public void setNote(String n)
	{
		note = n;
	}
	
	/**
	* getter method for the note
	* @return note
	*/
	public String getNote()
	{
		return note;
	}
}
