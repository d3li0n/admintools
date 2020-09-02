package me.d3li0n.AdminTools.helpers;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager {
	public static int playerTimer;
	private final Map<UUID, PlayerChatManager> players;
	private final Map<UUID, PlayerChatManager> mutedplayers;

	public ChatManager() {
		this.players = new HashMap<UUID, PlayerChatManager>();
		this.mutedplayers = new HashMap<UUID, PlayerChatManager>();
	}

	/* Chat cool down */
	public void add(final PlayerChatManager player) {
		final UUID p = player.getPlayer();
		if (this.players.containsKey(p)) this.remove(p); // this.players.remove(p);
		this.players.put(p, player);
	}

	/* For muted players */
	public void add(final PlayerChatManager player, final boolean isMuted) {
		final UUID p = player.getPlayer();
		if (this.mutedplayers.containsKey(p)) this.remove(p, isMuted); // this.players.remove(p);
		this.mutedplayers.put(p, player);
	}

	/* Chat cool down */
	public void remove(final UUID player) {
		this.players.remove(player);
	}

	/* For muted players */
	public void remove(final UUID player, boolean isMuted) {
		this.mutedplayers.remove(player);
	}

	/* Chat cool down */
	public boolean isDisabled(final UUID player) {
		final PlayerChatManager p = this.players.get(player);
		if (p == null) return false;
		if (p.isExpired()) {
			this.remove(player);
			return false;
		}
		return true;
	}

	/* For muted players */
	public boolean isDisabled(final UUID player, final boolean isMuted) {
		final PlayerChatManager p = this.mutedplayers.get(player);
		if (p == null) return false;
		if (p.isExpired()) {
			this.remove(player, isMuted);
			return false;
		}
		return true;
	}

	/* Chat cool down */
	public long getTimeRemaining(final UUID player) {
		final PlayerChatManager p = this.players.get(player);
		if (p != null) return p.getTimeRemaining();
		return -1;
	}

	/* For muted players */
	public long getTimeRemaining(final UUID player, final boolean isMuted) {
		final PlayerChatManager p = this.mutedplayers.get(player);
		if (p != null) return p.getTimeRemaining();
		return -1;
	}
}
