package package;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Test extends JavaPlugin implements Listener {

	KountdownPlayer logoutPlayerKountdown = new KountdownPlayer(this) {

		@Override
		public void start(Player player) {
			player.sendMessage("You've started your logout");
		}

		@Override
		public void tick(Player player, int countdown) {
			player.sendMessage("Logging out in " + countdown);

		}

		@Override
		public void stop(Player player) {
			player.sendMessage("You've stopped your logout");
		}

		@Override
		public void finish(Player player) {
			player.sendMessage("Done!");
		}

	};

	public void onEnable() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(this, this);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		// Note: the void would be called in the command class, this is just for
		// testing
		logoutCommand(event.getPlayer());
	}

	public void logoutCommand(Player player) {
		logoutPlayerKountdown.startPlayer(player, 10 * 20); // 10 seconds
	}

	@EventHandler
	public void playerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (logoutPlayerKountdown.containsPlayer(player))
			logoutPlayerKountdown.stopPlayer(player);
	}

}