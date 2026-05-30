package net.okocraft.oobteleporter;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public final class OOBTeleporterPlugin extends JavaPlugin implements Listener {

    private ScheduledTask checkTask;

    @Override
    public void onEnable() {
        this.checkTask = this.getServer().getAsyncScheduler().runAtFixedRate(
                this,
                ignored -> this.getServer().getOnlinePlayers().forEach(this::checkPlayer),
                3, 3, TimeUnit.SECONDS
        );
    }

    @Override
    public void onDisable() {
        if (this.checkTask != null) {
            this.checkTask.cancel();
        }
    }

    private void checkPlayer(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        if (!world.getWorldBorder().isInside(location) || location.getY() < world.getMinHeight() - 256) {
            player.teleportAsync(world.getSpawnLocation());
        }
    }
}
