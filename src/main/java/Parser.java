/**
 * Parses raw user input into a command word and its remaining
 * arguments, so that Leo does not need to inspect the raw input string
 * (e.g. via startsWith/substring) directly.
 */
public class Parser {
    /**
     * Extracts the command word: the first whitespace-separated token
     * of the input.
     *
     * @param input raw user input
     * @return the command word, or an empty string if input is blank
     */
    public static String getCommandWord(String input) {
        String trimmed = input.trim();
        int spaceIndex = trimmed.indexOf(' ');
        return spaceIndex == -1 ? trimmed : trimmed.substring(0, spaceIndex);
    }

    /**
     * Extracts the arguments that follow the command word.
     *
     * @param input raw user input
     * @return the text after the first whitespace-separated token,
     *     trimmed; an empty string if there are no arguments
     */
    public static String getArguments(String input) {
        String trimmed = input.trim();
        int spaceIndex = trimmed.indexOf(' ');
        return spaceIndex == -1 ? "" : trimmed.substring(spaceIndex + 1).trim();
    }
}
