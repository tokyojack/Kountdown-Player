# Kountdown-Player
A util class that runs code for players over a set amount of time. Made for Bukkit API.

INSTRUCTIONS:
1. Copy and paste the util class from ```src/me/tokyojack/bukkit``` to your project into a class called ```KountdownPlayer```
2. Change the package at the top to your needed one, and you're good to go!

TO USE:
```
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
			player.kickPlayer("You have logged out!");
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
```

Now you've just install the program! You're a pretty cool person:
1. ```(•_•)```
2. ```( •_•)>⌐■-■```
3. ```(⌐■_■)```
