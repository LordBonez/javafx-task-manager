package application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TaskPersistenceTest {
    @TempDir Path temp;
    private Task sample() {
        Task task = new Task();
        task.setName("Prepare internship application");
        task.setDueDate(LocalDateTime.of(2026, 10, 20, 12, 30));
        task.setStatus(true);
        task.setNote("Sample data for an automated persistence check.");
        return task;
    }
    @Test void savesAndReloadsAllFourFieldsInANewStore() {
        Tasks original = new Tasks();
        original.setFilePath(temp.resolve("tasks.tdl").toString());
        Task task = sample();
        original.getTasks().add(task);
        original.Save();
        Tasks reopened = new Tasks();
        reopened.setFilePath(original.getFilePath());
        reopened.Load();
        assertEquals(1, reopened.getTasks().size());
        Task restored = reopened.getTasks().get(0);
        assertEquals(task.getName(), restored.getName());
        assertEquals(task.getDueDate(), restored.getDueDate());
        assertEquals(task.getStatus(), restored.getStatus());
        assertEquals(task.getNote(), restored.getNote());
    }
    @Test void persistsAnEmptyTaskList() {
        Tasks store = new Tasks();
        store.setFilePath(temp.resolve("empty.tdl").toString());
        store.Save();
        store.getTasks().add(sample());
        store.Load();
        assertTrue(store.getTasks().isEmpty());
    }
    @Test void missingFileStartsWithAnEmptyList() {
        Tasks store = new Tasks();
        store.setFilePath(temp.resolve("not-created.tdl").toString());
        store.Load();
        assertTrue(store.getTasks().isEmpty());
    }
    @Test void repeatedLoadDoesNotDuplicateTasks() {
        Tasks store = new Tasks();
        store.setFilePath(temp.resolve("reload.tdl").toString());
        store.getTasks().add(sample());
        store.Save();
        store.Load();
        store.Load();
        assertEquals(1, store.getTasks().size());
    }
}
