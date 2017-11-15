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
	private int intervalInSeconds;

	private int runnableID;
	private JavaPlugin plugin;

	public KountdownPlayer(JavaPlugin plugin) {
		this.players = new HashMap<String, Integer>();
		this.intervalInSeconds = 1;

		this.runnableID = -1;
		this.plugin = plugin;
	}

	public KountdownPlayer(int seconds, JavaPlugin plugin) {
		this.players = new HashMap<String, Integer>();
		this.intervalInSeconds = seconds;

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
			this.startPlayerRunnable();
	}

	public void stopPlayer(Player player) {
		this.players.remove(player.getName());
		stop(player);

		if (this.isEmpty())
			this.stopPlayerRunnable();
	}

	public void addPlayerTime(Player player, int newAmount) {
		int pastAmount = this.players.get(player.getName());
		this.players.put(player.getName(), pastAmount + newAmount);
	}

	public void subtractPlayerTime(Player player, int newAmount) {
		int pastAmount = this.players.get(player.getName());
		this.players.put(player.getName(), pastAmount - newAmount);
	}

	public void setIntervalTime(int seconds) {
		this.intervalInSeconds = seconds;
	}

	public int getInteralTime() {
		return this.intervalInSeconds;
	}

	public int getPlayerTime(Player player) {
		return this.players.get(player.getName());
	}

	public Map<String, Integer> getPlayers() {
		return this.players;
	}

	public boolean containsPlayer(Player player) {
		return this.players.containsKey(player.getName());
	}

	public Boolean isPlayerStarted(Player player) {
		return this.players.containsKey(player.getName());
	}

	public Boolean isRunnableStarted() {
		return this.runnableID != -1;
	}

	public Boolean isEmpty() {
		return this.players.isEmpty();
	}

	private void startPlayerRunnable() {
		this.runnableID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			public void run() {
				Iterator<Map.Entry<String, Integer>> it = players.entrySet().iterator();
				while (it.hasNext()) { // Gotta do a ireator as I'm removing
										// while looping
					Map.Entry<String, Integer> pair = it.next();

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
		}, 0, this.intervalInSeconds * 20);
	}

	private void stopPlayerRunnable() {
		Bukkit.getScheduler().cancelTask(this.runnableID);
		this.runnableID = -1;
	}

}