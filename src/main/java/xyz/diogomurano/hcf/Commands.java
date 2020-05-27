package xyz.diogomurano.hcf;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import xyz.diogomurano.hcf.command.argument.ArgumentParserRegistry;
import xyz.diogomurano.hcf.command.argument.SimpleParserRegistry;
import xyz.diogomurano.hcf.command.lambda.CommandBuilder;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class Commands {

    // Global argument parsers
    private static final ArgumentParserRegistry PARSER_REGISTRY;

    @Nonnull
    public static ArgumentParserRegistry parserRegistry() {
        return PARSER_REGISTRY;
    }

    static {
        PARSER_REGISTRY = new SimpleParserRegistry();


        // setup default argument parsers
        PARSER_REGISTRY.register(String.class, Optional::of);
        PARSER_REGISTRY.register(Number.class, s -> {
            Objects.requireNonNull(s);
            try {
                return Optional.ofNullable(NumberFormat.getInstance().parse(s));
            } catch (ParseException e) {
                return Optional.empty();
            }
        });

        PARSER_REGISTRY.register(Integer.class, s -> {
            Objects.requireNonNull(s);
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });

        PARSER_REGISTRY.register(Long.class, s -> {
            Objects.requireNonNull(s);
            try {
                return Optional.of(Long.parseLong(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });

        PARSER_REGISTRY.register(Float.class, s -> {
            Objects.requireNonNull(s);
            try {
                return Optional.of(Float.parseFloat(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });

        PARSER_REGISTRY.register(Double.class, s -> {
            Objects.requireNonNull(s);
            try {
                return Optional.of(Double.parseDouble(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });

        PARSER_REGISTRY.register(Byte.class, s -> {
            Objects.requireNonNull(s);
            try {
                return Optional.of(Byte.parseByte(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        });

        PARSER_REGISTRY.register(Boolean.class, s -> s.equalsIgnoreCase("true") ? Optional.of(true) : s.equalsIgnoreCase("false") ? Optional.of(false) : Optional.empty());
        PARSER_REGISTRY.register(UUID.class, s -> {
            try {
                return Optional.of(UUID.fromString(s));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        });
        PARSER_REGISTRY.register(World.class, s -> Optional.ofNullable(Bukkit.getWorld(s)));
    }

    /**
     * Creates and returns a new command builder
     *
     * @return a command builder
     */
    public static CommandBuilder<CommandSender> create() {
        return CommandBuilder.newBuilder();
    }

    private Commands() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}