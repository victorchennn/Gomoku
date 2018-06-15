package game;

/** Provides command input, one line at a time. */
public interface Source {

    /** Read and return a line of input from the input stream,
     *  removing comments and leading and trailing whitespace,
     *  and skipping blank lines. Returns null when input exhausted.
     *  PROMPT suggests a prompt string that might be used, if
     *  appropriate to the input method. */
    String getline(String prompt);
}
