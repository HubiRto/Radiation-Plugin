package pl.pomoku.radiationplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static pl.pomoku.radiationplugin.listeners.PlayerMove.inRegion;

public class PotionFade implements Listener {
    @EventHandler
    public void onFade(EntityPotionEffectEvent event){
        if(!(event.getEntity() instanceof Player p)) return;
        if(!(event.getAction() == EntityPotionEffectEvent.Action.REMOVED)) return;
        if(event.getOldEffect() == null) return;
        if(event.getOldEffect().getType() == PotionEffectType.WITHER
                || event.getOldEffect().getType() == PotionEffectType.HUNGER) return;

        if(!inRegion(p.getLocation())) return;
        p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 2, true));
    }
}
