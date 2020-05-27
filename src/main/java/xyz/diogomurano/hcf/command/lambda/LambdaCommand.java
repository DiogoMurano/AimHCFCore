package xyz.diogomurano.hcf.command.lambda;

import com.google.common.collect.ImmutableList;
import xyz.diogomurano.hcf.command.AbstractCommand;
import xyz.diogomurano.hcf.command.CommandInterruptException;
import xyz.diogomurano.hcf.command.context.CommandContext;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

class LambdaCommand extends AbstractCommand {

    private final ImmutableList<Predicate<CommandContext<?>>> predicates;
    private final LambdaCommandHandler handler;

    LambdaCommand(ImmutableList<Predicate<CommandContext<?>>> predicates, LambdaCommandHandler handler) {
        this.predicates = predicates;
        this.handler = handler;
    }

    @Override
    public void call(@Nonnull CommandContext<?> context) throws CommandInterruptException {
        for (Predicate<CommandContext<?>> predicate : this.predicates) {
            if (!predicate.test(context)) {
                return;
            }
        }

        //noinspection unchecked
        this.handler.handle(context, context.sender(), context.label(), context.args());
    }
}
