package pl.pomoku.radiationplugin.commands;

import com.virtame.menusystem.commands_system.CommandInfo;
import com.virtame.menusystem.commands_system.EzCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.virtame.menusystem.text.MiniMes.mmd;
import static pl.pomoku.radiationplugin.RadiationPlugin.plugin;

@CommandInfo(name = "radiation", requiresPlayer = true)
public class SetSpawnCmd extends EzCommand implements TabCompleter {
    @Override
    public void execute(Player p, String[] args) {
        //if(args[0].equals("set")) {
            Location player_location = p.getLocation();
            Block block_above_player = player_location.getBlock().getRelative(BlockFace.DOWN);
            if (block_above_player.getType() == Material.AIR) {
                p.sendMessage(mmd("<red>Nie możesz ustawić spawnu, ponieważ nie ma pod tobą bloku."));
                return;
            }
            Location location_block_above = block_above_player.getLocation();
            plugin.getConfig().set("spawn.x", location_block_above.getX());
            plugin.getConfig().set("spawn.y", location_block_above.getY());
            plugin.getConfig().set("spawn.z", location_block_above.getZ());
            plugin.getConfig().set("spawn.world", location_block_above.getWorld());
            plugin.saveConfig();
            p.sendMessage(mmd("<green>Poprawnie ustawiono spawn dla radiacji."));
        }
    //}

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.singletonList("setspawn");
    }
}
