package leo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskTest {
    @Test
    public void newTask_isNotDoneByDefault() {
        Task task = new Todo("read book");

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatus());
    }

    @Test
    public void markAsDone_thenMarkAsNotDone_flipsStatusBothWays() {
        Task task = new Todo("read book");

        task.markAsDone();
        assertTrue(task.isDone());
        assertEquals("X", task.getStatus());

        task.markAsNotDone();
        assertFalse(task.isDone());
        assertEquals(" ", task.getStatus());
    }

    @Test
    public void toString_todo_matchesExpectedFormat() {
        Task task = new Todo("read book");
        assertEquals("[T][ ] read book", task.toString());

        task.markAsDone();
        assertEquals("[T][X] read book", task.toString());
    }

    @Test
    public void toFileString_todo_matchesExpectedFormat() {
        Task task = new Todo("read book");
        assertEquals("T | 0 | read book", task.toFileString());

        task.markAsDone();
        assertEquals("T | 1 | read book", task.toFileString());
    }

    @Test
    public void deadline_toString_includesFormattedByDate() {
        Task task = new Deadline("return book", "2019-12-01 1800");
        assertEquals("[D][ ] return book (by: Dec 01 2019, 6:00PM)", task.toString());
    }

    @Test
    public void deadline_toFileString_roundTripsInputFormat() {
        Task task = new Deadline("return book", "2019-12-01 1800");
        assertEquals("D | 0 | return book | 2019-12-01 1800", task.toFileString());
    }

    @Test
    public void event_toString_includesFormattedFromAndTo() {
        Task task = new Event("project meeting", "2019-12-02 1400", "2019-12-02 1600");
        assertEquals(
                "[E][ ] project meeting (from: Dec 02 2019, 2:00PM to: Dec 02 2019, 4:00PM)",
                task.toString()
        );
    }
}
