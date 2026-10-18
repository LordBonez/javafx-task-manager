
package application;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javafx.application.Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.util.Callback;
import javafx.util.StringConverter;

public class ToDoListClient extends Application
{
	//Fields
	//Table View
	private TableView table;
	
	//List for Table View
	private ObservableList<ArrayList<String>> list;

	//Reference to main Stage
	private Stage mainStage;
	
	//Tasks file name
    private String filePath = "To Do List.tdl";
	
    //Task dialog fields 
	private TextField textName;
	private DatePicker dueDate;
	private ComboBox<String> dueTime;
	private CheckBox status;
	private TextField textNote;
	
	//Table Columns
	private static final int COL_NAME = 0;
	private static final int COL_DUE_DATE = 1;
	private static final int COL_DUE_IN = 2;
	private static final int COL_STATUS = 3;
	private static final int COL_NOTE = 4;
	
	//List of Due Time for Combo Box
	private static final ObservableList<String> timeOptions = 
	    FXCollections.observableArrayList(
	        "00:00",
	        "01:00",
	        "02:00",
	        "03:00",
	        "04:00",
	        "05:00",
	        "06:00",
	        "07:00",
	        "08:00",
	        "09:00",
	        "10:00",
	        "11:00",
	        "12:00",
	        "13:00",
	        "14:00",
	        "15:00",
	        "16:00",
	        "17:00",
	        "18:00",
	        "19:00",
	        "20:00",
	        "21:00",
	        "22:00",
	        "23:00"
	    );
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage)
	{
		//Make reference to main Stage
		mainStage = stage;
		
		//Creating a menu
		Menu menuFile = new Menu("File");
		Menu menuEdit = new Menu("Edit");
		Menu menuHelp = new Menu("Help");
		
		//Creating menu Items
		MenuItem itemExit = new MenuItem("Exit");
	  
		MenuItem itemAdd = new MenuItem("Add\t\t\tInsert");
		MenuItem itemModify = new MenuItem("Modify\t\tEnter");
		MenuItem itemRemove = new MenuItem("Remove\t\tDelete");
		  
		MenuItem itemAbout = new MenuItem("About");
		  
		//Adding all the menu items to the menu
		menuFile.getItems().addAll(itemExit);
		menuEdit.getItems().addAll(itemAdd, itemModify, itemRemove);
		menuHelp.getItems().addAll(itemAbout);
		  
		//Creating a menu bar and adding menu to it.
		MenuBar menuBar = new MenuBar(menuFile, menuEdit, menuHelp);
		
		//Event handlers for menu items
		itemExit.setOnAction(event -> { StoreTasks(); stage.close(); });
		itemAdd.setOnAction(event -> { AddTask(); });
		itemModify.setOnAction(event -> { ModifyTask(); });
		itemRemove.setOnAction(event -> { RemoveTask(); });
		itemAbout.setOnAction(event -> { JOptionPane.showMessageDialog(null, "Task Manager\n\nVersion 1.0.1"); });

		//Store Tasks on application exit
		stage.setOnCloseRequest(event ->
		{
			StoreTasks();
		});
	 
		//Creating a table view
		table = new TableView<>();
		
		//Creating columns
		TableColumn nameCol = new TableColumn("Name");
		TableColumn dueDateCol = new TableColumn("Due Date");
		TableColumn dueInCol = new TableColumn("Due in");
		TableColumn statusCol = new TableColumn("Status");
		TableColumn noteCol = new TableColumn("Note");
		
		//Disable sort in table columns
		nameCol.setSortable(false);
		dueDateCol.setSortable(false);
		dueInCol.setSortable(false);
		statusCol.setSortable(false);
		noteCol.setSortable(false);
		
		//Set width for table columns
		nameCol.setPrefWidth(200);
		dueDateCol.setPrefWidth(200);
		dueInCol.setPrefWidth(200);
		statusCol.setPrefWidth(200);
		noteCol.setPrefWidth(200);

		//Add columns to Table
		table.getColumns().addAll(nameCol, dueDateCol, dueInCol, statusCol, noteCol);

		//Use RowFactory method to access TableRow in Table
		table.setRowFactory(tv ->
		{
            TableRow<ArrayList<String>> row = new TableRow<ArrayList<String>>();
            
    		//Event handler for Double Click in Table row
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && !row.isEmpty())
                	ModifyTask();
            });
            return row;
        });
		
		//Event handler for Keys
		stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>()
		{
		    public void handle(KeyEvent ke)
		    {
		        if (ke.getCode() == KeyCode.ESCAPE)
		        {
		        	StoreTasks();
		        	stage.close();
		        }
		        else if (ke.getCode() == KeyCode.INSERT)
		        	AddTask();
		        else if (ke.getCode() == KeyCode.ENTER)
		        	ModifyTask();
		        else if (ke.getCode() == KeyCode.DELETE)
		        	RemoveTask();
		    }
		});
		
		//Adding data to the table
        list = FXCollections.observableArrayList();

        //Loading Tasks from File to Table
    	LoadTasks();
		
        //Link buildCallbackString Callback method with Table columns
    	//from https://stackoverflow.com/questions/46171183/javafx-tableview-data-from-list
        ObservableList<TableColumn> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++)
        {
            TableColumn column = columns.get(i);

            Callback<TableColumn.CellDataFeatures<ArrayList, String>, ObservableValue<String>> cellValueFactory = buildCallbackString(i);
            column.setCellValueFactory(cellValueFactory);
        }
        
        //Link buildCallbackTextFieldTableCell Callback method with Table columns
     	//from https://stackoverflow.com/questions/46171183/javafx-tableview-data-from-list
        for (int i = 0; i < columns.size(); i++)
        {
            TableColumn column = columns.get(i);
            
			Callback<TableColumn<ArrayList, String>, TableCell<ArrayList, String>> cellFactoryTextFieldTableCell = buildCallbackTextFieldTableCell();
			column.setCellFactory(cellFactoryTextFieldTableCell);
        }
        
        //Create Timer
		Timer timer = new Timer();
		
		//Schedule the timer every 1 second with no initial delay
		timer.scheduleAtFixedRate(new TimerTask()
		{
			//override run method
			@Override
			public void run()
			{
				Reminder();
			}
		}, 0, 1000);
		
		//Setting the size of the table
		VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(5, 5, 5, 5));
		VBox.setVgrow(table, Priority.ALWAYS);
		vbox.getChildren().addAll(menuBar, table);
		  
		//Setting the scene
		Scene scene = new Scene(vbox);
		  
		//Setting the stage
		stage.getIcons().add(new Image(ToDoListClient.class.getResourceAsStream("/application/Icon.bmp")));
		stage.setTitle("Task Manager");
		stage.setScene(scene);
		stage.show();
	}
	
    /**
    * Callback method to Link data with Table Cells
	* from https://stackoverflow.com/questions/46171183/javafx-tableview-data-from-list
    */
    private Callback<TableColumn.CellDataFeatures<ArrayList, String>, ObservableValue<String>> buildCallbackString(int index)
    {
        Callback<TableColumn.CellDataFeatures<ArrayList, String>, ObservableValue<String>> cellValueFactory = new Callback<TableColumn.CellDataFeatures<ArrayList, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList, String> param)
            {
                return new SimpleStringProperty((String) param.getValue().get(index));
            }
        };
        return cellValueFactory;
    }
    
    /**
    * Callback method to Link data with Table Cells
	* from https://stackoverflow.com/questions/46171183/javafx-tableview-data-from-list
    */
    private Callback<TableColumn<ArrayList, String>, TableCell<ArrayList, String>> buildCallbackTextFieldTableCell()
    {
        Callback<TableColumn<ArrayList, String>, TableCell<ArrayList, String>> cellFactory = new Callback<TableColumn<ArrayList, String>, TableCell<ArrayList, String>>()
        {
            @Override
            public TableCell call(TableColumn tc)
            {
				TextFieldTableCell tftc = new TextFieldTableCell(new StringConverter()
                {
                    @Override
                    public String toString(Object t)
                    {
                        return t.toString();
                    }

                    @Override
                    public Object fromString(String string)
                    {
                        return string;
                    }
                });

                return tftc;
            }
        };
        return cellFactory;
    }
	
    /**
    * Method to calculate "due in" time for each Task 
    */
	private void Reminder()
	{
		//Convert List of Strings to Tasks 
		Tasks tasks = ListToTasks();

		//Get list of Tasks
		ArrayList<Task> listTasks = tasks.getTasks();
		
		//Iterate for list of Tasks
		for (int i = 0; i < listTasks.size(); i++)
		{
			//Get Task from list
			Task task = listTasks.get(i);
			
			//only for "not completed" Tasks
			if (!task.getStatus())
			{
				//Get "Due Date" from Task
				LocalDateTime dueDate = task.getDueDate();
				
				//Get current Date and Time
				LocalDateTime dateNow;
				dateNow = LocalDateTime.now();
				
				//Get Row from Table data list
				ArrayList<String> dataRow = list.get(i);
				
				//Calculate Duration between "Due Date" and current Time
				Duration duration = Duration.between(dueDate, dateNow);
				long days = duration.toDays();
				long hours = duration.toHours();
				long minutes = duration.toMinutes();
				
				//Remove negative sign
				days = Math.abs(days);
				hours = Math.abs(hours);
				minutes = Math.abs(minutes);
				
				String valueDateIn;
				
				//Compare "Due Date" and current Time and format "Date In" string to display in Table column
				if (dueDate.isAfter(dateNow))
				{
					if (minutes == 0)
						valueDateIn = String.format("Due now");
					else if (hours == 0)
						valueDateIn = String.format("Due in %d minutes", minutes);
					else if (days == 0)
						valueDateIn = String.format("Due in %d hours %d minutes", hours, minutes % 60);
					else
						valueDateIn = String.format("Due in %d days and %d hours", days, hours % 24);
				}
				else
				{
					if (minutes == 0)
						valueDateIn = String.format("Due now");
					else if (hours == 0)
						valueDateIn = String.format("Overdue %d minutes", minutes);
					else if (days == 0)
						valueDateIn = String.format("Overdue %d hours %d minutes", hours, minutes % 60);
					else
						valueDateIn = String.format("Overdue %d days %d hours", days, hours % 24);
				}
				dataRow.set(COL_DUE_IN, valueDateIn);
			}
		}
		//Update Table with new data List
		table.getItems().setAll(list);
	}
	
    /**
    * Load Tasks from file to Table View
    */
	private void LoadTasks()
	{
		//Get Selection Model from Table View
		TableViewSelectionModel selectionModel = table.getSelectionModel();

		//Get Selected Indices from Selection Model
		ObservableList selectedIndices = selectionModel.getSelectedIndices();
		
		//Create Tasks
    	Tasks tasks = new Tasks();
    	
    	//Set file name
    	tasks.setFilePath(filePath);
    	
    	//Load Tasks from file
		tasks.Load();
		
		//Get list of Tasks
		ArrayList<Task> listTasks = tasks.getTasks();
		
		//Iterate for each Task
        for (int i = 0; i < listTasks.size(); i++)
        {
        	//Get Task from Task List
        	Task task = listTasks.get(i);
        	
        	//Create data Row
            ArrayList<String> dataRow = new ArrayList<>();

            //Get Name from Task
            String valueName = task.getName();
            
            //Get "Due Date" from Task
            LocalDateTime dueDate = task.getDueDate();
            
            //Convert "Due Date" from LocalDateTime to String
            String valueDate = DateTimeFormatter.ISO_LOCAL_DATE.format(dueDate);
            valueDate += " ";
            
            //Create Time Formatter without seconds 
            DateTimeFormatter noSeconds = DateTimeFormatter.ofPattern("HH:mm");            
            valueDate += noSeconds.format(dueDate);
            
            //Convert Task "Status" from boolean to String
            String valueStatus;
            if (task.getStatus() == true)
            	valueStatus = "Completed";
            else
            	valueStatus = "Not Completed";
            
            String valueNote = task.getNote();

            //Add Columns of Strings to Table Row
            dataRow.add(valueName);
            dataRow.add(valueDate);
            dataRow.add("");
            dataRow.add(valueStatus);
            dataRow.add(valueNote);

            //Add Row to Table list
            list.add(dataRow);
        }
        
        //Update Table View
		table.getItems().setAll(list);
		
		//Select first Table Row
		selectionModel.select(0);
	}
	
	/**
	* Convert List to Tasks
	* @return Tasks
	*/
	private Tasks ListToTasks()
	{
		//Create List of Tasks
		ArrayList<Task> listTasks = new ArrayList();
		
		//Iterate for each Task from list
        for (int i = 0; i < list.size(); i++)
        {
        	//Get data Row from List
        	ArrayList dataRow = list.get(i);
        	
        	//Create Task
        	Task task = new Task();
        	
        	//Assign name from List Row to Task
        	task.setName((String)dataRow.get(COL_NAME));
        	
        	String valueDate = (String)dataRow.get(COL_DUE_DATE);
        	
        	//Create Date and Time formatter
	        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	        
	        //Parse and convert from String to LocalDateTime object
	        LocalDateTime date = LocalDateTime.parse(valueDate, df);
        	task.setDueDate(date);

        	String valueStatus = (String)dataRow.get(COL_STATUS);
        	
        	//Convert Task "Status" from String to boolean
        	boolean Status;
            if (valueStatus == "Completed")
            	Status = true;
            else
            	Status = false;
            task.setStatus(Status);
        	
        	task.setNote((String)dataRow.get(COL_NOTE));
        	
        	//Add Task to the list of Tasks 
        	listTasks.add(task);
        }
		
        //Create Tasks
    	Tasks tasks = new Tasks();
    	
    	//Set file name
    	tasks.setFilePath(filePath);
    	
    	//Set Tasks
		tasks.setTasks(listTasks);
		
		return tasks;
	}
	
	/**
	* Store Tasks 
	*/
	private void StoreTasks()
	{
		//Convert List to Tasks
		Tasks tasks = ListToTasks();
		
		//Save Tasks
		tasks.Save();
	}

	/**
	* Add and Modify Tasks
	* @param selectedIndex
	* 	-1 - Add new Task
	* 	Index - Modify Task 
	*/
	private void AddModifyTask(int selectedIndex)
	{	
		// Create Task Dialog Stage and set properties
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainStage);
        dialogStage.setResizable(false);
        dialogStage.initStyle(StageStyle.UTILITY);
        
		VBox vbox = new VBox();
		vbox.setSpacing(5);
		
		Label labelName = new Label("Name");  
		textName = new TextField();
		
		HBox hboxDateTime = new HBox();
		hboxDateTime.setSpacing(5);
		
		VBox vboxDate = new VBox();
		vboxDate.setSpacing(5);
		
		Label labelDueDate = new Label("Due Date");  
		dueDate = new DatePicker();
		
		vboxDate.getChildren().addAll(labelDueDate, dueDate);
		
		VBox vboxTime = new VBox();
		vboxTime.setSpacing(5);
		
		Label labelDueTime = new Label("Due Time");
		dueTime = new ComboBox<String>(timeOptions);
		
		vboxTime.getChildren().addAll(labelDueTime, dueTime);
		
		hboxDateTime.getChildren().addAll(vboxDate, vboxTime);
		
		status = new CheckBox("Mark as completed");
		
		Label labelNote = new Label("Note");
		textNote = new TextField();
		
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		
		Button btnOK = new Button("OK");
		Button btnCancel = new Button("Cancel");
		
		hbox.setPadding(new Insets(5, 5, 5, 5));
		hbox.getChildren().addAll(btnOK, btnCancel);
		
		vbox.setPadding(new Insets(5, 5, 5, 5));
		vbox.getChildren().addAll(labelName, textName, hboxDateTime, status, labelNote, textNote, hbox);
		  
		//Setting the scene
		Scene scene = new Scene(vbox, 400, 220);
		  
		//Event handlers for OK and Cancel buttons
		btnOK.setOnAction(event -> { OnOK(dialogStage, selectedIndex); });
		btnCancel.setOnAction(event -> { dialogStage.close(); });
		
		//Event handler for Dialog keys
		dialogStage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>()
		{
		    public void handle(KeyEvent ke)
		    {
		        if (ke.getCode() == KeyCode.ENTER)
		        	OnOK(dialogStage, selectedIndex);
		        else if (ke.getCode() == KeyCode.ESCAPE)
		        	dialogStage.close();
		    }
		});
		
		//For Modify Task
		if (selectedIndex != -1)
		{
			//Get data Row from list
			ArrayList<String> dataRow = list.get(selectedIndex);
			
			textName.setText((String)dataRow.get(COL_NAME));
			
	        String valueDateTime = (String)dataRow.get(COL_DUE_DATE);
	        //Create Date Formatter
	        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	        //Parse and convert from String to DateTimeFormatter
	        LocalDateTime date = LocalDateTime.parse(valueDateTime, df);
	        dueDate.setValue(date.toLocalDate());
	        
			ObservableList<String> timeOptions = dueTime.getItems();
			
			//Select Time from String in Combo Box
			for (int i = 0; i < timeOptions.size(); i++)
			{
				String value = timeOptions.get(i);
				
				//Compare full Date and Time String and Time String in ComboBox
			    if (valueDateTime.endsWith(value))
			    	dueTime.getSelectionModel().select(i);
			}
			
	        String valueStatus = (String)dataRow.get(COL_STATUS);
	        
        	if (valueStatus == "Completed")
        		status.selectedProperty().setValue(true);
        	else
	        	status.selectedProperty().setValue(false);
	        
			textNote.setText((String)dataRow.get(COL_NOTE));
		}
		//For Add Task
		else
		{
			//Get current Date
			LocalDate date;
			date = LocalDate.now();
	        dueDate.setValue(date);

	        //Select 00::00 time by default
			dueTime.getSelectionModel().select(0);
		}
		
		dialogStage.setTitle("Task Details");
		dialogStage.setScene(scene);
		dialogStage.show();
	}

	/**
	* Add Task method
	*/
	private void AddTask()
	{
		//Pass -1 for Add Task
		AddModifyTask(-1);
	}
	
	/**
	* Modify Task method
	*/
	private void ModifyTask()
	{
		//Get Selection Model from Table View
		TableViewSelectionModel selectionModel = table.getSelectionModel();
		
		//Get Selected Indices from Selection Model
		ObservableList selectedIndices = selectionModel.getSelectedIndices();

		//Check if Row is selected
		if (!selectedIndices.isEmpty())
		{
			//Get First selected Index
			int selectedIndex = (int)selectedIndices.get(0);
			
			//Call Modify Task and pass selected Index
			AddModifyTask(selectedIndex);
			
			//Select Row with selected Index
			selectionModel.select(selectedIndex);
		}
	}

	/**
	* Remove Task method
	*/
	private void RemoveTask()
	{
		//Get Selection Model from Table View
		TableViewSelectionModel selectionModel = table.getSelectionModel();
		
		//Get Selected Indices from Selection Model
		ObservableList selectedIndices = selectionModel.getSelectedIndices();
		
		//Check if Row is selected
		if (!selectedIndices.isEmpty())
		{
	        int size = list.size();
	        
			//Get First selected Index
			int selectedIndex = (int)selectedIndices.get(0);
			
			//Remove Task from list at selected Index
			list.remove(selectedIndex);
			
			//Update Table View
			table.getItems().setAll(list);
			
			//Select Row
			if (selectedIndex == size - 1)
				selectionModel.select(selectedIndex - 1);	//Move selection up, if last Row is removed
			else
				selectionModel.select(selectedIndex);
		}
	}

	/**
	* On OK method
	* @param dialogStage
	* @param selectedIndex
	* 	-1 - Add new Task
	* 	Index - Modify Task 
	*/
	private void OnOK(Stage dialogStage, int selectedIndex)
	{
		//Get Selection Model from Table View
		TableViewSelectionModel selectionModel = table.getSelectionModel();
		
		//Create data Row
        ArrayList<String> dataRow = new ArrayList<>();
        
        String valueName = textName.getText();
        
        //Make sure Name is not empty
        if (valueName.isEmpty())
        {
    		JOptionPane.showMessageDialog(null, "Please enter Name");
        	return;
        }
        
        //Get date from DatePicker control
        LocalDate date = dueDate.getValue();
        
        //Make sure date in not empty
        if (date == null)
        {
    		JOptionPane.showMessageDialog(null, "Please enter Due Date");
        	return;
        }
        
        //Get Time from ComboBox control
        String timeValue = (String)dueTime.getValue();
        
        //Format and convert LocalDate to String
        String valueDate = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
        valueDate += " ";
        valueDate += timeValue;
        
        String valueStatus;
        //Get Status from CheckBox control
        if (status.selectedProperty().getValue() == true)
        	valueStatus = "Completed";
        else
        	valueStatus = "Not Completed";
        
        String valueNote = textNote.getText();

        int size = list.size();

        //Add String columns to the data Row
        dataRow.add(valueName);
        dataRow.add(valueDate);
        dataRow.add("");
        dataRow.add(valueStatus);
        dataRow.add(valueNote);

        if (selectedIndex == -1)
        	list.add(dataRow);	//Add new Task to the List
        else
        	list.set(selectedIndex, dataRow);	//Update selected Task
        
        //Update Table View
		table.getItems().setAll(list);
        
		//Select Row
        if (selectedIndex == -1)
        	selectionModel.select(size);	//Select added Task
        else
    		selectionModel.select(selectedIndex);	//Select Modified Task
        
        dialogStage.close();
	}
}
/* tests all types of input
no Name: "" = please enter name dialog
Name: "something" = good
DueDate: "" = please enter due date
DueDate: "date" = good
Note: "" = good
Note: "something" = good
*/
