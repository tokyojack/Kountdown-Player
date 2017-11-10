package package;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Test extends JavaPlugin implements Listener {

	KountdownPlayer kountdownPlayer = new KountdownPlayer(this) {

		@Override
		public void tick(Player player, int countdown) {
			player.sendMessage("Time left: " + countdown);

		}

		@Override
		public void finished(Player player) {
			player.sendMessage("Done!");
		}

	};

	public void onEnable() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(this, this);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		// if some condition...
		this.kountdownPlayer.addPlayer(event.getPlayer(), 10);
	}

}

