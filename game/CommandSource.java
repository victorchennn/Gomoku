package game;

import java.util.Stack;

/** Provides command input from a stack of CommandSource objects. */
public class CommandSource implements Source {

    /** Read and return a line of input from the input stream,
     *  removing comments and leading and trailing whitespace,
     *  and skipping blank lines.  Returns null when input exhausted.
     *  PROMPT suggests a prompt string that might be used, if
     *  appropriate to the input method. */
    @Override
    public String getline(String prompt) {
        while (!_inputs.isEmpty()) {
            String line;
            if ((line = _inputs.peek().getline(prompt)) != null) {
                if (line.indexOf('#') != -1) {
                    line = line.substring(0, line.indexOf('#'));
                }
                line = line.trim();
                if (line.length() > 0) {
                    return line;
                }
            } else {
                _inputs.pop();
            }
        }
        return null;
    }

    /** Make SOURCE the latest input source from which subsequent input
     *  will be read. */
    void addSource(Source source) {
        _inputs.add(source);
    }

    /** Stack of input sources, most recent on top. */
    private Stack<Source> _inputs = new Stack<>();
}
