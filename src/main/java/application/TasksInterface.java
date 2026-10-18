
package application;

import java.util.ArrayList;

public interface TasksInterface
{
	/**
	*  set the tasks
	*  @param tasks ArrayList of tasks
	*/
	public void setTasks(ArrayList<Task> tasks);
	
	/**
	* getter method for getting the tasks
	* @return tasks
	*/
	public ArrayList<Task> getTasks();
	
	/**
	*  set the file path
	* @param filePath
	*/
	public void setFilePath(String filePath);
	
	/**
	* get the file path
	* @return filePath
	*/
	public String getFilePath();
	
	// save method
	public void Save();
	
	// load method
	public void Load();
}
