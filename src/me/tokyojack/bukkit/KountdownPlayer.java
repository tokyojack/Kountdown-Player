package package;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Created by tokyojack
 * 
 * DON't REMOVE
 * 
 * GITHUB: https://github.com/tokyojack
 * DISCORD: tokyojack#7353
 * 
 */

public abstract class KountdownPlayer {

	private Map<String, Integer> players;
	private int interval;

	private int runnableID;
	private JavaPlugin plugin;

	public KountdownPlayer(JavaPlugin plugin) {
		this.players = new HashMap<String, Integer>();
		this.interval = 1;

		this.runnableID = -1;
		this.plugin = plugin;
	}

	public KountdownPlayer(int interval, JavaPlugin plugin) {
		this.players = new HashMap<String, Integer>();
		this.interval = interval;

		this.runnableID = -1;
		this.plugin = plugin;
	}

	public abstract void start(Player player);

	public abstract void tick(Player player, int countdown);

	public abstract void stop(Player player);

	public abstract void finish(Player player);

	public void startPlayer(Player player, int time) {
		this.players.put(player.getName(), time);
		start(player);

		if (this.runnableID == -1)
			startPlayerRunnable();
	}

	public void stopPlayer(Player player) {
		this.players.remove(player.getName());
		stop(player);

		if (this.players.isEmpty())
			this.stopPlayerRunnable();
	}

	private void startPlayerRunnable() {
		this.runnableID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			public void run() {
				Iterator<Map.Entry<String, Integer>> playerIterator = players.entrySet().iterator();
				while (playerIterator.hasNext()) { // Gotta do a ireator as I'm
													// removing. Would of used a foreach

					Map.Entry<String, Integer> pair = playerIterator.next();

					String playerName = pair.getKey();
					int time = pair.getValue();
					Player player = Bukkit.getPlayer(playerName);

					if (player != null) {
						if (time <= 0) {
							players.remove(playerName);
							stopPlayer(player);
							return;
						}

						tick(player, time);
						players.put(playerName, time - 1);
					}

				}
			}
			// 20 ticks = 1 second
		}, 0, this.interval * 20);
	}

	private void stopPlayerRunnable() {
		Bukkit.getScheduler().cancelTask(this.runnableID);
		this.runnableID = -1;
	}

}