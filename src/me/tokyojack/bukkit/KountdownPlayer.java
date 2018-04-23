package package

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
	private int tickDelay; // May want to change this name

	private int runnableID;
	private JavaPlugin plugin;

	public KountdownPlayer(JavaPlugin plugin) {
		this.players = new HashMap<String, Integer>();
		this.tickDelay = 20;

		this.runnableID = -1;
		this.plugin = plugin;
	}

	public KountdownPlayer(int tickDelay, JavaPlugin plugin) {
		this.players = new HashMap<String, Integer>();
		this.tickDelay = tickDelay;

		this.runnableID = -1;
		this.plugin = plugin;
	}

	public KountdownPlayer(JavaPlugin plugin, int tickDelay) {
		this.players = new HashMap<String, Integer>();
		this.tickDelay = tickDelay;

		this.runnableID = -1;
		this.plugin = plugin;
	}

	protected abstract void start(Player player);

	protected abstract void tick(Player player, int seconds);

	protected abstract void stop(Player player);

	protected abstract void finish(Player player);

	/**
	 * Starts the player runnable
	 *
	 * @param Player
	 *            - The player to be added to the runnable
	 */
	public void startPlayer(Player player, int time) {
		this.players.put(player.getName(), time);
		start(player);

		if (this.runnableID == -1)
			this.startPlayerRunnable();
	}

	/**
	 * Stops the player runnable
	 *
	 * @param Player
	 *            - The player that has a runnable on them
	 */
	public void stopPlayer(Player player) {
		this.players.remove(player.getName());
		stop(player);

		if (this.isEmpty())
			this.stopPlayerRunnable();
	}

	/**
	 * Finishes the player runnable
	 *
	 * @param Player
	 *            - The player that is being finished
	 */
	public void finishPlayer(Player player) {
		this.players.remove(player.getName());
		finish(player);

		if (this.isEmpty())
			this.stopPlayerRunnable();
	}

	/**
	 * Adds time to a player runnable
	 *
	 * @param Player
	 *            - The player that is in the runnable
	 * @param int
	 *            - The amount of time  (in seconds) to be added
	 */
	public void addTimeToPlayer(Player player, int newAmount) {
		int pastAmount = this.players.get(player.getName());
		this.players.put(player.getName(), pastAmount + newAmount);
	}

	/**
	 * Subtracts time frim a player runnable
	 *
	 * @param Player
	 *            - The player that is in the runnable
	 * @param int
	 *            - The amount of time  (in seconds) to be subracted
	 */
	public void subtractTimeFromPlayer(Player player, int newAmount) {
		int pastAmount = this.players.get(player.getName());
		this.players.put(player.getName(), pastAmount - newAmount);
	}

	/**
	 * Sets the delay between call on main runnable
	 *
	 * @param int
	 *            - The new tick amount
	 */
	public void setTickDelay(int tickDelay) {
		this.tickDelay = tickDelay;
	}

	/**
	 * Gets the delay between call on main runnable
	 *
	 * @return int - The current main runnable delay
	 */
	public int getTickDelay() {
		return this.tickDelay;
	}

	/**
	 * Gets a player, that is in the runnables, time left
	 *
	 * @return int - The player's time
	 */
	public int getPlayerTime(Player player) {
		return this.players.get(player.getName());
	}

	/**
	 * Gets all the players that are being looped through
	 *
	 * @return Player - Player
	 */
	public Map<String, Integer> getPlayers() {
		return this.players;
	}

	/**
	 * Checks if the player is in the runnable
	 *
	 * @return boolean - If the player is in the runnable
	 */
	public boolean containsPlayer(Player player) {
		return this.players.containsKey(player.getName());
	}

	
	/**
	 * Checks if the main runnable is running
	 *
	 * @return boolean - If the main runnable is running
	 */
	public boolean isRunnableStarted() {
		return this.runnableID != -1;
	}

	/**
	 * Checks if there are any players in the runnable
	 *
	 * @return boolean - If the main runnable has players in it
	 */
	public boolean isEmpty() {
		return this.players.isEmpty();
	}

	private void startPlayerRunnable() {
		this.runnableID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			public void run() {
				Iterator<Map.Entry<String, Integer>> it = players.entrySet().iterator();
				while (it.hasNext()) { // Gotta do a ireator as I'm removing while looping
					Map.Entry<String, Integer> pair = it.next();

					String playerName = pair.getKey();
					int time = pair.getValue();
					
					Player player = Bukkit.getPlayer(playerName);

					if (player != null) {
						
						// If the player has no time left
						if (time <= 0) {
							finishPlayer(player);
							continue;
						}

						// Runs tick abstract function
						tick(player, time);
						
						// Subtracts the player's time by 1
						players.put(playerName, time - 1);
					}
				}
			}
		}, 0, this.tickDelay);
	}

	private void stopPlayerRunnable() {
		Bukkit.getScheduler().cancelTask(this.runnableID);
		this.runnableID = -1;
	}

}