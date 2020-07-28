package me.d3li0n.AdminTools.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager {
	public static int playerTimer;
	private final Map<UUID, PlayerChatManager> players;
	
	public ChatManager() {
		this.players = new HashMap<UUID, PlayerChatManager>();
	}
	
	public void add(final PlayerChatManager cooldown) {
		final UUID p = cooldown.getPlayer();
		if (this.players.containsKey(p)) this.players.remove(p);
		this.players.put(p, cooldown);
	}

	public void remove(final UUID player) {
		this.players.remove(player);
	}

	public boolean hasCooldown(final UUID player) {
		final PlayerChatManager p = this.players.get(player);
		if (p == null) return false;
		if (p.isExpired()) {
			this.players.remove(player);
			return false;
		}
		return true;
	}

	public long getTimeRemaining(final UUID player) {
		final PlayerChatManager p = this.players.get(player);
		if (p != null) return p.getTimeRemaining();
		return -1;
	}
}
