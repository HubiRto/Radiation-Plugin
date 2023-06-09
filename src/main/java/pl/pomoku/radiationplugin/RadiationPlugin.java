package pl.pomoku.radiationplugin;

import com.virtame.menusystem.commands_system.EzCommand;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class RadiationPlugin extends JavaPlugin {
    public static RadiationPlugin plugin;
    private BukkitAudiences audiences;
    @Override
    public void onEnable() {
        plugin = this;
        this.audiences = BukkitAudiences.create(this);
        saveDefaultConfig();
        loadListenersAndCommands();
    }

    @Override
    public void onDisable() {
        if(this.audiences != null){
            this.audiences.close();
            this.audiences = null;
        }
    }

    public @NotNull BukkitAudiences audiences(){
        Objects.requireNonNull(this.audiences, "Próba dostania dostępu do Audiences kiedy plugin jest wyłączony.");
        return this.audiences;
    }

    private void loadListenersAndCommands() {
        String packageName = getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName + ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        for (Class<? extends EzCommand> clazz : new Reflections(packageName + ".commands").getSubTypesOf(EzCommand.class)) {
            try {
                EzCommand pluginCommand = clazz.getDeclaredConstructor().newInstance();
                Objects.requireNonNull(getCommand(pluginCommand.getCommandInfo().name())).setExecutor(pluginCommand);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
