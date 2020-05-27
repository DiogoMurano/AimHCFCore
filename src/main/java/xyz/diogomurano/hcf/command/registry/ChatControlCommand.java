package xyz.diogomurano.hcf.command.registry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.diogomurano.hcf.Commands;
import xyz.diogomurano.hcf.configurations.MessageTag;
import xyz.diogomurano.hcf.utils.HashCooldown;

import java.util.UUID;

public class ChatControlCommand extends AbstractCommandRegistry {

    private final HashCooldown<UUID> cooldown;

    private boolean activate;
    private boolean delayMode;
    private int delay;

    public ChatControlCommand() {
        cooldown = new HashCooldown<>();

        activate = true;
        delayMode = false;
        delay = 5;
    }

    @Override
    public void registerCommands() {
        Commands.create().assertPermission("hcf.command.chat").assertUsage("command value").handler((context, sender, label, args) -> {
            final String command = args.get(0);
            final String value = args.get(1);

            if (command.equalsIgnoreCase("activate")) {
                boolean status = Boolean.parseBoolean(value);
                if (activate == status) {
                    context.msg(msg(MessageTag.CHAT_CONTROL_ACTIVATE_ALREADY_STATUS));
                    return;
                }
                this.activate = status;
                context.msg(msg(MessageTag.CHAT_CONTROL_ACTIVATE_CHANGED_STATUS, status ? "ON" : "OFF"));
            } else if (command.equalsIgnoreCase("delaymode")) {
                boolean status = Boolean.parseBoolean(value);
                if (delayMode == status) {
                    context.msg(msg(MessageTag.CHAT_CONTROL_DELAY_MODE_ALREADY_STATUS));
                    return;
                }
                this.delayMode = status;
                context.msg(msg(MessageTag.CHAT_CONTROL_DELAY_MODE_CHANGED_STATUS, status ? "ON" : "OFF"));
            } else if (command.equalsIgnoreCase("delay")) {
                try {
                    int delay = Integer.parseInt(value);
                    this.delay = delay;
                    context.msg(msg(MessageTag.CHAT_CONTROL_DELAY_VALUE_CHANGED, delay));
                } catch (NumberFormatException e) {
                    context.msg(msg(MessageTag.VALUE_MUST_BE_A_NUMBER));
                }
            }
        }).register("chat");
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UUID uniqueId = player.getUniqueId();

        if (!activate && !player.hasPermission("hcf.bypass.chat.off")) {
            player.sendMessage(msg(MessageTag.CHAT_DISABLED));
            event.setCancelled(true);
            return;
        }
        if (cooldown.isWaiting(uniqueId)) {
            player.sendMessage(msg(MessageTag.CHAT_WAIT_COOLDOWN, cooldown.getReamingSeconds(uniqueId)));
            event.setCancelled(true);
            return;
        }

        if (delayMode && !player.hasPermission("hcf.bypass.chat.delaymode")) {
            cooldown.insert(uniqueId, delay * 1000);
        }
    }

}
