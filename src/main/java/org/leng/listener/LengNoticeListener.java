package org.leng.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.leng.LengNotice;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LengNoticeListener implements Listener {
    private final LengNotice plugin;

    public LengNoticeListener(LengNotice plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.actionBarEnabled) return;

        Player player = event.getPlayer();
        String actionBarMessage = plugin.actionBarMessage.replace("{playername}", player.getName());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBarMessage));
    }
}