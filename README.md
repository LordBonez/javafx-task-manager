# JavaFX Task Manager

A desktop to-do application originally completed as a December 2022 data-structures project.

## Features

- Five table columns: name, due date, time until due, completion status, and note.
- Add, modify and remove tasks, with date/time selection and deadline reminders.
- Save/load four task fields through Java serialization in a local `.tdl` file.
- A desktop menu and keyboard shortcuts.

## Run

Install JDK 17+ and Maven 3.9+. Set JAVA_HOME to the JDK, then run:

```sh
mvn clean test
mvn javafx:run
```

The application needs a graphical desktop. It creates `To Do List.tdl` in its working directory.
Task data is ignored by Git. Only open your own trusted serialized task files.

## Checks

The JUnit suite covers save/load across a new store, all four persisted fields, an empty list,
a missing file, and repeated loading without duplicates. It does not automate interaction with the GUI.
A useful manual check is to add two sample tasks, edit/delete one, close, reopen, and verify the saved task.

## Project history

October 2026 portfolio preparation adds Maven dependency management, a launcher,
repeatable persistence tests, and classpath-based icon loading.
Source comments retain attribution for the referenced TableView callback example.
See VALIDATION.md for the verified checks.
