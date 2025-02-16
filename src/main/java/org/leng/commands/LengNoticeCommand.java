package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.leng.LengNotice;

import java.util.Map;

public class LengNoticeCommand implements CommandExecutor {
    private final LengNotice plugin;

    public LengNoticeCommand(LengNotice plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lengnotice.use")) {
            sender.sendMessage("§c你没有权限使用这个指令！");
            return true;
        }

        // 如果没有任何参数，直接显示帮助信息
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        // 根据第一个参数执行对应的操作
        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("lengnotice.reload")) {
                    sender.sendMessage("§c你没有权限重载配置！");
                    return true;
                }
                plugin.reloadConfigData();
                sender.sendMessage("§a配置已重载！");
                return true;
            case "on":
                if (!sender.hasPermission("lengnotice.broadcast.on")) {
                    sender.sendMessage("§c你没有权限开启广播！");
                    return true;
                }
                plugin.broadcastEnabled = true;
                sender.sendMessage("§a广播已开启！");
                return true;
            case "off":
                if (!sender.hasPermission("lengnotice.broadcast.off")) {
                    sender.sendMessage("§c你没有权限关闭广播！");
                    return true;
                }
                plugin.broadcastEnabled = false;
                sender.sendMessage("§c广播已关闭！");
                return true;
            case "list":
                if (!sender.hasPermission("lengnotice.broadcast.list")) {
                    sender.sendMessage("§c你没有权限查看广播内容！");
                    return true;
                }
                if (plugin.broadcastMessages.isEmpty()) {
                    sender.sendMessage("§c没有可用的广播内容！");
                } else {
                    sender.sendMessage("§e广播内容列表:");
                    for (Map.Entry<Integer, String> entry : plugin.broadcastMessages.entrySet()) {
                        sender.sendMessage("§f- 编号: §e" + entry.getKey() + " §f内容: " + entry.getValue());
                    }
                }
                return true;
            case "actionbar":
                if (!sender.hasPermission("lengnotice.actionbar.set")) {
                    sender.sendMessage("§c你没有权限设置动作栏消息！");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§c用法: /ln actionbar <消息>");
                    return true;
                }
                String actionBarMessage = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                plugin.getConfig().set("actionbar-message", actionBarMessage);
                plugin.saveConfig();
                plugin.reloadConfigData();
                sender.sendMessage("§a动作栏消息已更新！");
                return true;
            case "add":
                if (!sender.hasPermission("lengnotice.broadcast.add")) {
                    sender.sendMessage("§c你没有权限添加广播内容！");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage("§c用法: /ln add <编号> <内容>");
                    return true;
                }
                int id = Integer.parseInt(args[1]);
                String message = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
                plugin.broadcastMessages.put(id, message);
                plugin.getConfig().set("broadcast-messages." + id, message);
                plugin.saveConfig();
                sender.sendMessage("§a广播内容已添加！");
                return true;
            case "remove":
                if (!sender.hasPermission("lengnotice.broadcast.remove")) {
                    sender.sendMessage("§c你没有权限删除广播内容！");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§c用法: /ln remove <编号>");
                    return true;
                }
                int removeId = Integer.parseInt(args[1]);
                if (plugin.broadcastMessages.containsKey(removeId)) {
                    plugin.broadcastMessages.remove(removeId);
                    plugin.getConfig().set("broadcast-messages." + removeId, null);
                    plugin.saveConfig();
                    sender.sendMessage("§a广播内容已删除！");
                } else {
                    sender.sendMessage("§c未找到编号为 §e" + removeId + " §c的广播内容！");
                }
                return true;
            default:
                // 如果输入的命令不存在，显示帮助信息
                showHelp(sender);
                return true;
        }
    }

    /**
     * 显示帮助信息
     */
    private void showHelp(CommandSender sender) {
        sender.sendMessage("§eLengNotice 帮助信息:");
        sender.sendMessage("§e/ln reload - 重载配置");
        sender.sendMessage("§e/ln on - 开启广播");
        sender.sendMessage("§e/ln off - 关闭广播");
        sender.sendMessage("§e/ln list - 显示广播内容");
        sender.sendMessage("§e/ln actionbar <消息> - 设置动作栏消息");
        sender.sendMessage("§e/ln add <编号> <内容> - 添加广播内容");
        sender.sendMessage("§e/ln remove <编号> - 删除广播内容");
        sender.sendMessage("§e/ln - 显示帮助信息");
    }
}