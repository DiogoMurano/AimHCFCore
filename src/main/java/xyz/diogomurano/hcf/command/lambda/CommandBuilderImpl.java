package xyz.diogomurano.hcf.command.lambda;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import xyz.diogomurano.hcf.command.Command;
import xyz.diogomurano.hcf.command.context.CommandContext;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

class CommandBuilderImpl<T extends CommandSender> implements CommandBuilder<T> {
    private final ImmutableList.Builder<Predicate<CommandContext<?>>> predicates;

    private CommandBuilderImpl(ImmutableList.Builder<Predicate<CommandContext<?>>> predicates) {
        this.predicates = predicates;
    }

    CommandBuilderImpl() {
        this(ImmutableList.builder());
    }

    @Override
    public CommandBuilder<T> assertPermission(String permission, String failureMessage) {
        Objects.requireNonNull(permission, "permission");
        Objects.requireNonNull(failureMessage, "failureMessage");
        this.predicates.add(context -> {
            if (context.sender().hasPermission(permission)) {
                return true;
            }

            context.msg(failureMessage);
            return false;
        });
        return this;
    }

    @Override
    public CommandBuilder<T> assertOp(String failureMessage) {
        Objects.requireNonNull(failureMessage, "failureMessage");
        this.predicates.add(context -> {
            if (context.sender().isOp()) {
                return true;
            }

            context.msg(failureMessage);
            return false;
        });
        return this;
    }

    @Override
    public CommandBuilder<Player> assertPlayer(String failureMessage) {
        Objects.requireNonNull(failureMessage, "failureMessage");
        this.predicates.add(context -> {
            if (context.sender() instanceof Player) {
                return true;
            }

            context.msg(failureMessage);
            return false;
        });
        // cast the generic type
        return new CommandBuilderImpl<>(this.predicates);
    }

    @Override
    public CommandBuilder<ConsoleCommandSender> assertConsole(String failureMessage) {
        Objects.requireNonNull(failureMessage, "failureMessage");
        this.predicates.add(context -> {
            if (context.sender() instanceof ConsoleCommandSender) {
                return true;
            }

            context.msg(failureMessage);
            return false;
        });
        // cast the generic type
        return new CommandBuilderImpl<>(this.predicates);
    }

    @Override
    public CommandBuilder<T> assertUsage(String usage, String failureMessage) {
        Objects.requireNonNull(usage, "usage");
        Objects.requireNonNull(failureMessage, "failureMessage");

        List<String> usageParts = Splitter.on(" ").splitToList(usage);

        int requiredArgs = 0;
        for (String usagePart : usageParts) {
            if (!usagePart.startsWith("[") && !usagePart.endsWith("]")) {
                // assume it's a required argument
                requiredArgs++;
            }
        }

        int finalRequiredArgs = requiredArgs;
        this.predicates.add(context -> {
            if (context.args().size() >= finalRequiredArgs) {
                return true;
            }

            context.msg(failureMessage.replace("{usage}", "/" + context.label() + " " + usage));
            return false;
        });

        return this;
    }

    @Override
    public CommandBuilder<T> assertArgument(int index, Predicate<String> test, String failureMessage) {
        Objects.requireNonNull(test, "test");
        Objects.requireNonNull(failureMessage, "failureMessage");
        this.predicates.add(context -> {
            String arg = context.rawArg(index);
            if (test.test(arg)) {
                return true;
            }

            context.msg(failureMessage.replace("{arg}", arg).replace("{index}", Integer.toString(index)));
            return false;
        });
        return this;
    }

    @Override
    public CommandBuilder<T> assertSender(Predicate<T> test, String failureMessage) {
        Objects.requireNonNull(test, "test");
        Objects.requireNonNull(failureMessage, "failureMessage");
        this.predicates.add(context -> {
            //noinspection unchecked
            T sender = (T) context.sender();
            if (test.test(sender)) {
                return true;
            }

            context.msg(failureMessage);
            return false;
        });
        return this;
    }

    @Override
    public Command handler(LambdaCommandHandler handler) {
        Objects.requireNonNull(handler, "handler");
        return new LambdaCommand(this.predicates.build(), handler);
    }
}
