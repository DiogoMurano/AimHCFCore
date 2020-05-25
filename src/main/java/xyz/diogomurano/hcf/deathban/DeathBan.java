package xyz.diogomurano.hcf.deathban;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class DeathBan {

    private final UUID uniqueId;
    private final String reason;
    private final long creationMillis = 0;
    private final long expiryMillis = 0;
    private final Location location;

    private ItemStack[] inventoryItems;
    private ItemStack[] contentItems;

}
