package leo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void getCommandWord_singleWord_returnsWholeInput() {
        assertEquals("bye", Parser.getCommandWord("bye"));
    }

    @Test
    public void getCommandWord_wordWithArguments_returnsFirstWordOnly() {
        assertEquals("todo", Parser.getCommandWord("todo read book"));
    }

    @Test
    public void getCommandWord_extraLeadingOrInnerSpaces_isRobust() {
        assertEquals("mark", Parser.getCommandWord("  mark   2"));
    }

    @Test
    public void getCommandWord_emptyInput_returnsEmptyString() {
        assertEquals("", Parser.getCommandWord(""));
    }

    @Test
    public void getArguments_singleWord_returnsEmptyString() {
        assertEquals("", Parser.getArguments("list"));
    }

    @Test
    public void getArguments_wordWithArguments_returnsTrimmedRemainder() {
        assertEquals("read book", Parser.getArguments("todo read book"));
    }

    @Test
    public void getArguments_extraInnerSpaces_stillTrimmed() {
        assertEquals("2", Parser.getArguments("mark  2"));
    }

    @Test
    public void getArguments_deadlineStyleInput_keepsSlashMarkers() {
        assertEquals(
                "return book /by 2019-12-01 1800",
                Parser.getArguments("deadline return book /by 2019-12-01 1800")
        );
    }
}
