package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/** Provides command input from a Reader. */
public class ReaderSource implements Source {

    /** A new source that reads from INPUT and prints prompts
     *  if SHOULDPROMPT. */
    ReaderSource(Reader input, boolean shouldPrompt) {
        _inputs = new BufferedReader(input);
        _shouldPrompt = shouldPrompt;
    }

    @Override
    public String getline(String prompt) {
        if (_inputs == null) {
            return null;
        }
        try {
            if (_shouldPrompt) {
                System.out.print(prompt);
                System.out.flush();
            }
            String result = _inputs.readLine();
            if (result == null) {
                _inputs.close();
            }
            System.out.println(result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    /** Input Stream. */
    private BufferedReader _inputs;

    /** True if we request a prompt for each getLine. */
    private boolean _shouldPrompt;
}
