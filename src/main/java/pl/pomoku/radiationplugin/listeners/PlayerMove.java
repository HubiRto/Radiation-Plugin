package pl.pomoku.radiationplugin.listeners;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

import static com.virtame.menusystem.text.MiniMes.mmd;
import static pl.pomoku.radiationplugin.RadiationPlugin.plugin;

public class PlayerMove implements Listener {
    private final Map<Player, BossBar> bossBars = new HashMap<>();
    private final HashMap<String, Long> messageCooldown = new HashMap<>();
    private static final PotionEffect wither_effect = new PotionEffect(PotionEffectType.WITHER, 100, 2, true);
    private static final PotionEffect hunger_effect = new PotionEffect(PotionEffectType.HUNGER, 100, 2, true);

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Audience audience = plugin.audiences().sender(player);
        Location location = player.getLocation();
        if (!inRegion(location)) {
            BossBar bossBar = bossBars.get(player);
            if (bossBar != null) {
                bossBars.remove(player);
                audience.hideBossBar(bossBar);
                if(player.hasPotionEffect(PotionEffectType.WITHER) || player.hasPotionEffect(PotionEffectType.HUNGER)){
                    player.removePotionEffect(PotionEffectType.WITHER);
                    player.removePotionEffect(PotionEffectType.HUNGER);
                }
            }
        } else {
            player.addPotionEffect(wither_effect);
            player.addPotionEffect(hunger_effect);
            BossBar bossBar = BossBar.bossBar(
                    mmd("<red>Strefa radiacji"),
                    1.0f,
                    BossBar.Color.RED,
                    BossBar.Overlay.PROGRESS
            );
            if(bossBars.containsKey(player)) return;

            String message = "<red>Gracz " + player.getName() + " wszedł do strefy radiacji.";
            if(!messageCooldown.containsKey(message)){
                messageCooldown.put(message, System.currentTimeMillis());
                for(Player playerOnline : plugin.getServer().getOnlinePlayers()){
                    playerOnline.sendMessage(mmd(message));
                }
            }else {
                long timeSinceCreation = System.currentTimeMillis() - messageCooldown.get(message);
                if(timeSinceCreation >= 3 * 1000){
                    messageCooldown.remove(message);
                }
            }

            audience.showBossBar(bossBar);
            bossBars.put(player, bossBar);

        }
    }

    public static boolean inRegion(Location location) {
        int range = plugin.getConfig().getInt("no-radiation-zone");
        double x = plugin.getConfig().getDouble("spawn.x");
        double z = plugin.getConfig().getDouble("spawn.z");

        return location.getX() >= (x + range) || location.getX() <= (x - range)
                || location.getZ() >= (z + range) || location.getZ() <= (z - range);
    }
}
