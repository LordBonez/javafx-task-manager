
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Tasks implements TasksInterface, Serializable
{
	//Used for version control of object
	private static final long serialVersionUID = -2296986366407297542L;
	
	//Fields
	private ArrayList<Task> tasks;
	private String filePath;

	//constructor
	public Tasks()
	{
		tasks = new ArrayList<Task>();
	}
	
	/**
	*  set the tasks
	*  @param tasks ArrayList of tasks
	*/
	public void setTasks(ArrayList<Task> tasks)
	{
		this.tasks = tasks;
	}
	
	/**
	* getter method for getting the tasks
	* @return tasks
	*/
	public ArrayList<Task> getTasks()
	{
		return tasks;
	}
	
	/**
	*  set the file path
	* @param filePath
	*/
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	/**
	* get the file path
	* @return filePath
	*/
	public String getFilePath()
	{
		return filePath;
	}
	
	//save method
	public void Save()
	{
		try
		{
	        FileOutputStream fos = new FileOutputStream(filePath);
	        ObjectOutputStream outputStream = new ObjectOutputStream(fos);
	        
	        outputStream.writeObject(tasks);
        	
	        outputStream.close();
	    }
		catch (IOException ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage());
	    }		
	}
	
	//load method
	public void Load()
	{
		//Clear list of Tasks
		tasks.clear();
		
		try
		{
			//Check file exists
			File file = new File(filePath);
			if (!file.exists())
				return;
			
	        FileInputStream fis = new FileInputStream(filePath);
	        ObjectInputStream inputStream = new ObjectInputStream(fis);
	        
        	tasks = (ArrayList<Task>) inputStream.readObject();
        	
	        inputStream.close();
	    }
		catch (IOException | ClassNotFoundException ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage());
	    }		
	}
}
