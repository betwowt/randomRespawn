package com.gitbub.betwowt.randomrespawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Commands {


    public boolean setSpawn(CommandSender sender, Command command, String label, String[] args){
        if (command.getName().equalsIgnoreCase("setSpawn")) {
            if (args.length >= 1) {
                sender.sendMessage("未知指令,您是不是想输入/setSpawn");
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location location = player.getLocation();
                Main.lists.add(location);
                try {
                    BaseDao.executeUpdate("INSERT INTO location VALUES (NULL ,?,?,?,?)",location.getWorld().getName(),location.getX(),location.getY(),location.getZ());
                } catch (SQLException e) {
                    Bukkit.getLogger().info("插入操作执行失败");
                    e.printStackTrace();
                }

                player.sendMessage("设置重生点" + Main.lists.get(Main.lists.size() - 1).toString() + "成功!");
                return true;
            } else {
                sender.sendMessage("这个指令只能是玩家使用");
                return false;
            }
        }

        return false;
    }

    public boolean spawnlist(CommandSender sender, Command command, String label, String[] args){
        if (command.getName().equalsIgnoreCase("spawnlist")) {
            if (args.length >= 1) {
                sender.sendMessage("未知指令,您是不是想输入/spawnlist");
                return false;
            }
            if (sender.hasPermission("spawnList.view")) {
                sender.sendMessage("——位置列表——");
                for (Location location : Main.lists) {
                    sender.sendMessage("世界名:" + location.getWorld().getName() + "      X坐标: " + location.getX() + "      Y坐标:" + location.getY() + "      Z坐标:" + location.getZ());
                }
                return true;
            }
        }
        return false;
    }



    public boolean clearLastLocation(CommandSender sender, Command command, String label, String[] args){
        if (command.getName().equalsIgnoreCase("clearLastLocation") || command.getName().equalsIgnoreCase("cll")) {
            if (args.length >= 1) {
                sender.sendMessage("未知指令,您是不是想输入/clearLastLocation");
                return false;
            }
            if (!sender.hasPermission("clearLastLocation.use")) {
                sender.sendMessage("您没有clearLastLocation.use权限");
                return false;
            }

            if (sender instanceof Player) {

                Player player = (Player) sender;
                if (Main.lists.size() > 0) {
                    player.sendMessage("已清除上一个位置  :" + Main.lists.get(Main.lists.size() - 1).toString());

                    Main.lists.remove(Main.lists.size() - 1);
                    try {
                        BaseDao.executeUpdate("DELETE FROM location WHERE 1 ORDER BY location_id DESC LIMIT 1");
                    } catch (SQLException e) {
                        Bukkit.getLogger().info("删除操作执行失败");
                        e.printStackTrace();
                    }
                }
                return true;
            } else {
                sender.sendMessage("控制台无法执行这个命令");
            }
        }
        return false;
    }

    public boolean clearAllLocation(CommandSender sender, Command command, String label, String[] args){
        if (command.getName().equalsIgnoreCase("clearAllLocation") || command.getName().equalsIgnoreCase("calll")) {
            if (args.length >= 1) {
                sender.sendMessage("未知指令,您是不是想输入/clearAllLocation");
                return false;
            }
            if (!sender.hasPermission("clearAllLocation.use")) {
                sender.sendMessage("您没有clearAllLocation.use权限");
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("已清空所有的位置信息");
                Main.lists.clear();
                try {
                    BaseDao.executeUpdate("DELETE FROM location");
                } catch (SQLException e) {
                    Bukkit.getLogger().info("删除所有操作执行失败");
                }
                return true;
            } else {
                sender.sendMessage("控制台无法执行这个命令");
            }
        }
        return false;
    }




    }
