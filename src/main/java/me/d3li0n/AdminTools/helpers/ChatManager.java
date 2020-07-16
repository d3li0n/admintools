package me.d3li0n.AdminTools.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager {
	public static int cooldownTimer;
	private final Map<UUID, ChatCooldown> cooldowns;
	
	public ChatManager() {
		this.cooldowns = new HashMap<UUID, ChatCooldown>();
	}
	
	public void add(final ChatCooldown cooldown) {
		final UUID player = cooldown.getPlayer();
		if (this.cooldowns.containsKey(player)) this.cooldowns.remove(player);
		this.cooldowns.put(player, cooldown);
	}

	public void remove(final UUID player) {
		this.cooldowns.remove(player);
	}

	public boolean hasCooldown(final UUID player) {
		final ChatCooldown cooldown = this.cooldowns.get(player);
		if (cooldown == null) return false;
		if (cooldown.isExpired()) {
			this.cooldowns.remove(player);
			return false;
		}
		return true;
	}

	public long getTimeRemaining(final UUID player) {
		final ChatCooldown cooldown = this.cooldowns.get(player);
		
		if (cooldown != null) return cooldown.getTimeRemaining();
		
		return -1;
	}
}
