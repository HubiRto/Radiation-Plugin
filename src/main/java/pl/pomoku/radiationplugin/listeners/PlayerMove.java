package pl.pomoku.radiationplugin.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static pl.pomoku.radiationplugin.RadiationPlugin.plugin;

public class PlayerMove implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if(!inRegion(location)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2, true));
    }

    public static boolean inRegion(Location location){
        int range = plugin.getConfig().getInt("no-radiation-zone");
        double x = plugin.getConfig().getDouble("spawn.x");
        double z = plugin.getConfig().getDouble("spawn.z");

        return location.getX() >= (x + range) || location.getX() <= (x - range)
                || location.getZ() >= (z + range) || location.getZ() <= (z - range);
    }
}
