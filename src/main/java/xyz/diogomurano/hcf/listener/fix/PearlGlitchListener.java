package xyz.diogomurano.hcf.listener.fix;

import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Openable;

import java.util.Set;

public class PearlGlitchListener implements Listener {

    private final Set<Material> blockedPearlTypes;

    public PearlGlitchListener() {
        this.blockedPearlTypes = Sets.immutableEnumSet(Material.THIN_GLASS, Material.IRON_FENCE, Material.FENCE,
                Material.NETHER_FENCE, Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS,
                Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS,
                Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS,
                Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.hasItem()) && (event.getItem().getType() == Material.ENDER_PEARL)) {
            Block block = event.getClickedBlock();
            Material type = block.getType();
            if ((type.isSolid()) && (!(block.getState() instanceof InventoryHolder))) {
                if (type == Material.FENCE_GATE && block.getState().getData() instanceof Openable) {
                    if (((Openable) block.getState().getData()).isOpen()) {
                        return;
                    }
                }
            }
//            Faction factionAt = HCF.getInstance().getFactionManager().getFactionAt(block.getLocation());
//            if (!(factionAt instanceof ClaimableFaction)) {
//                return;
//            }
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.setItemInHand(event.getItem());
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPearlClip(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Location to = event.getTo();
            Block block = to.getBlock();
            Material type = block.getType();
            if (this.blockedPearlTypes.contains(type)) {
                if (type == Material.FENCE_GATE && block.getState().getData() instanceof Openable) {
                    if (((Openable) block.getState().getData()).isOpen()) {
                        return;
                    }
                }
                Block above = block.getRelative(BlockFace.UP);
                Material aboveType = above.getType();
                if (this.blockedPearlTypes.contains(aboveType) || aboveType.isSolid()) {
                    Player player = event.getPlayer();
                    player.sendMessage(ChatColor.YELLOW + "Pearl glitching detected, your pearl has been refunded");
                    event.setCancelled(true);
                } else {
                    to.add(0, 1, 0);
                    event.setTo(to);
                }
            }
        }
    }
}