# Kountdown-Player
A util class that runs code for players over a set amount of time. Made for Bukkit API.

INSTRUCTIONS:
1. Copy and paste the util class from ```src/me/tokyojack/bukkit``` to your project into a class called ```KountdownPlayer```
2. Change the package at the top to your needed one, and you're good to go!

TO USE:
```
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
```

Now you've just install the program! You're a pretty cool person:
1. ```(•_•)```
2. ```( •_•)>⌐■-■```
3. ```(⌐■_■)```
