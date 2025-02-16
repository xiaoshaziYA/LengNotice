package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.leng.commands.LengNoticeCommand;
import org.leng.listener.LengNoticeListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LengNotice extends JavaPlugin {
    public boolean broadcastEnabled;
    public int broadcastInterval;
    public Map<Integer, String> broadcastMessages = new HashMap<>();
    public boolean actionBarEnabled;
    public String actionBarMessage;
    private Random random = new Random();

    @Override
    public void onEnable() {
        reloadConfigData();
        getCommand("ln").setExecutor(new LengNoticeCommand(this));
        startBroadcastTask();
        getServer().getPluginManager().registerEvents(new LengNoticeListener(this), this);
        getLogger().info("§aLengNotice 已加载！");
        getLogger().info("§a当前版本：" + getDescription().getVersion());
        getLogger().info("§a作者：Leng");
    }

    @Override
    public void onDisable() {
        getLogger().info("§cLengNotice 已卸载！");
    }

    public void reloadConfigData() {
        reloadConfig();
        FileConfiguration config = getConfig();
        broadcastEnabled = config.getBoolean("broadcast-enabled");
        broadcastInterval = config.getInt("broadcast-interval");
        broadcastMessages.clear();
        config.getConfigurationSection("broadcast-messages").getKeys(false).forEach(key -> {
            broadcastMessages.put(Integer.parseInt(key), config.getString("broadcast-messages." + key));
        });
        actionBarEnabled = config.getBoolean("actionbar-enabled");
        actionBarMessage = config.getString("actionbar-message");
    }

    private void startBroadcastTask() {
        if (broadcastEnabled) {
            Bukkit.getScheduler().runTaskTimer(this, () -> {
                if (broadcastEnabled && !broadcastMessages.isEmpty()) {
                    int randomKey = random.nextInt(broadcastMessages.size()) + 1;
                    String message = broadcastMessages.get(randomKey);
                    Bukkit.broadcastMessage("§e[广播] §f" + message);
                }
            }, broadcastInterval * 20L, broadcastInterval * 20L);
        }
    }
}