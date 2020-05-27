package xyz.diogomurano.hcf.command.lambda;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import xyz.diogomurano.hcf.command.CommandInterruptException;
import xyz.diogomurano.hcf.command.context.CommandContext;

@FunctionalInterface
public interface LambdaCommandHandler<T extends CommandSender> {

    /**
     * Executes the handler using the given command context
     *
     * @param context - The command context
     * @param sender - The command sender
     * @param label - The command alias used
     * @param args - The command arguments
     */
    void handle(CommandContext<T> context, T sender, String label, ImmutableList<String> args) throws CommandInterruptException;

}
