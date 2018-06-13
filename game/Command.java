package game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {

    static enum Type {

        AUTO(""),
        MANUAL(""),
        START,
        SETBOARD(""),
        QUIT,
        CLEAR,
        PRINT,
        HELP,
        ERROR(".*"),
        EOF;
        ;


        Type(String pattern) {
            _pattern = Pattern.compile(pattern + "$");
        }

        Type() {
            _pattern = Pattern.compile(this.toString().toLowerCase());
        }

        private final Pattern _pattern;
    }

    /** A new Command of type TYPE with OPERANDS as its operands. */
    Command(Type type, String... operands) {
        _type = type;
        _operands = operands;
    }

    static Command parseCommand(String command) {
        if (command == null) {
            return new Command(Type.EOF);
        }
        command = command.trim();
        for (Type type : Type.values()) {
            Matcher matcher = type._pattern.matcher(command);
            if (matcher.matches()) {
                String[] operands = new String[matcher.groupCount()];
                for (int i = 1; i <= operands.length; i++) {
                    operands[i - 1] = matcher.group(i);
                }
                return new Command(type, operands);
            }
        }
        throw new Error("Impossible");
    }

    /** Return the type of this Command. */
    Type commandType() {
        return _type;
    }

    /** Returns this Command's operands. */
    String[] operands() {
        return _operands;
    }

    /** The command name. */
    private final Type _type;

    /** Command arguments. */
    private final String[] _operands;
}
