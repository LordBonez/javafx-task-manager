
package application;

import java.time.LocalDateTime;

public interface TaskInterface
{
	
	/**
	* Setter method for the name
	* @param n string of name
	*/
	public void setName(String n);
	
	/**
	* Getter method for the name 
	* @return name
	*/
	public String getName();
	
	/**
	* Setter method for set the due date
	* @param d local date time 
	*/
	public void setDueDate(LocalDateTime d);
	
	/**
	* Getter method for the local date time
	* @return duedate
	*/
	public LocalDateTime getDueDate();
	
	/**
	* setter method for status
	* @param s boolean for status
	*/
	public void setStatus(boolean s);
	
	/**
	* getter method for the status
	* @return status
	*/
	public boolean getStatus();
	
	/**
	* setter method for note
	* @param n String of notes
	*/
	public void setNote(String n);
	
	/**
	* getter method for the note
	* @return note
	*/
	public String getNote();
}
