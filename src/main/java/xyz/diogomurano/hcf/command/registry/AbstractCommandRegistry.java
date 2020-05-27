package xyz.diogomurano.hcf.command.registry;

import org.bukkit.event.Listener;
import xyz.diogomurano.hcf.HCF;
import xyz.diogomurano.hcf.configurations.MessageTag;

public abstract class AbstractCommandRegistry implements Listener {

    public abstract void registerCommands();

    public String msg(MessageTag tag) {
        return "";
    }

    public String msg(MessageTag tag, String... format) {
        return "";
    }

    public String msg(MessageTag tag, Object... format) {
        return "";
    }

}
