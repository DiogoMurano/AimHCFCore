package xyz.diogomurano.hcf.command.argument;

import xyz.diogomurano.hcf.command.CommandInterruptException;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Parses an argument from a String
 *
 * @param <T> the value type
 */
public interface ArgumentParser<T> {

    /**
     * Parses the value from a string
     *
     * @param s the string
     * @return the value, if parsing was successful
     */
    Optional<T> parse(@Nonnull String s);

    /**
     * Parses the value from a string, throwing an interrupt exception if
     * parsing failed.
     *
     * @param s the string
     * @return the value
     */
    @Nonnull
    default T parseOrFail(@Nonnull String s) throws CommandInterruptException {
        Optional<T> ret = parse(s);
        if (!ret.isPresent()) {
            throw new CommandInterruptException("&cUnable to parse argument: " + s);
        }
        return ret.get();
    }

    /**
     * Tries to parse the value from the argument
     *
     * @param argument the argument
     * @return the value, if parsing was successful
     */
    @Nonnull
    default Optional<T> parse(@Nonnull Argument argument) {
        return argument.value().flatMap(this::parse);
    }

    /**
     * Parses the value from an argument, throwing an interrupt exception if
     * parsing failed.
     *
     * @param argument the argument
     * @return the value
     */
    @Nonnull
    default T parseOrFail(@Nonnull Argument argument) throws CommandInterruptException {
        Optional<T> ret = parse(argument);
        if (!ret.isPresent()) {
            throw new CommandInterruptException("&cUnable to parse argument at index " + argument.index() + ".");
        }
        return ret.get();
    }

    /**
     * Creates a new parser which first tries to obtain a value from
     * this parser, then from another if the former was not successful.
     *
     * @param other the other parser
     * @return the combined parser
     */
    @Nonnull
    default ArgumentParser<T> thenTry(@Nonnull ArgumentParser<T> other) {
        ArgumentParser<T> first = this;
        return t -> {
            Optional<T> ret = first.parse(t);
            return ret.isPresent() ? ret : other.parse(t);
        };
    }
}
