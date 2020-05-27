package xyz.diogomurano.hcf.command;

import xyz.diogomurano.hcf.command.context.CommandContext;

import javax.annotation.Nonnull;

/**
 * Represents a command
 */
public interface Command {

    /**
     * Registers this command with the server, via the given plugin instance
     *
     * @param aliases the aliases for the command
     */
    void register(@Nonnull String... aliases);

    void close();

    /**
     * Calls the command handler
     *
     * @param context the contexts for the command
     */
    void call(@Nonnull CommandContext<?> context) throws CommandInterruptException;

}
