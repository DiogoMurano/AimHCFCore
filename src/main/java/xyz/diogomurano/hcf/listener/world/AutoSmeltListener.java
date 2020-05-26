package xyz.diogomurano.hcf.listener.world;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AutoSmeltListener implements Listener {

    private final Map<Material, Material> smeltItems;

    public AutoSmeltListener() {
        smeltItems = new HashMap<>();
        loadItems();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            final Block block = event.getBlock();
            final Material type = block.getType();
            final Material smelt = smeltItems.get(type);
            if (smelt != null) {
                event.setCancelled(true);
                event.setExpToDrop(0);

                block.setType(Material.AIR);
                ItemStack stack = new ItemStack(smelt, 1);
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), stack);
            }
        }
    }

    public void loadItems() {
        smeltItems.put(Material.IRON_ORE, Material.IRON_INGOT);
        smeltItems.put(Material.GOLD_ORE, Material.GOLD_INGOT);
    }
}
