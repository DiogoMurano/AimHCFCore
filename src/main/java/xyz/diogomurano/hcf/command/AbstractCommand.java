package xyz.diogomurano.hcf.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.diogomurano.hcf.HCF;
import xyz.diogomurano.hcf.command.context.CommandContext;
import xyz.diogomurano.hcf.command.context.ImmutableCommandContext;

import javax.annotation.Nonnull;

/**
 * An abstract implementation clickable {@link Command} and {@link CommandExecutor}
 */
public abstract class AbstractCommand implements Command, CommandExecutor {

    @Override
    public void register(@Nonnull String... aliases) {
        HCF.getInstance().registerCommand(this, aliases);
    }

    @Override
    public void close() {
        CommandMapUtil.unregisterCommand(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        CommandContext<CommandSender> context = new ImmutableCommandContext<>(sender, label, args);

        try {
            call(context);
        } catch (CommandInterruptException e) {
            e.getAction().accept(context.sender());
        }
        return true;
    }
}
