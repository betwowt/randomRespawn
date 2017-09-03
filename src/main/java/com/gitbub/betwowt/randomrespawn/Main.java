package com.gitbub.betwowt.randomrespawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {
    private static Main main;

    public static Main getMain() {
        return main;
    }
    private Configuration config = getConfig();
    public static List<Location> lists = new ArrayList<Location>();


    private static Commands commands = new Commands();

    @Override
    public void onEnable() {
        main = this;
        this.saveDefaultConfig();
        CreateDataBase();
        readerDB();
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getLogger().info("randomRespawn 已启用");
    }

    @Override
    public void onDisable() {
        getLogger().info("randomRespawn  已卸载");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        commands.setSpawn(sender,command,label,args);

        commands.spawnlist(sender,command,label,args);

        commands.clearLastLocation(sender, command, label, args);

        commands.clearAllLocation(sender, command, label, args);
        return true;
    }

    private void readerDB(){
        World world = null;
        double x = 0;
        double y = 0;
        double z = 0;
        try {
            List<String> strings= BaseDao.SelectAll("SELECT * FROM location", 5);
            for (String s:strings){
                String[] split = s.split("\\|");
                System.out.println(split.toString());
                world = Bukkit.getServer().getWorld(split[1]);
                x = Double.parseDouble(split[2]);
                y = Double.parseDouble(split[3]);
                z = Double.parseDouble(split[4]);
                lists.add(new Location(world,x,y,z));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("查询所有记录失败"+e.getMessage());
        }

    }

    /**
     * 创建数据库和数据库表
     */
    private void CreateDataBase() {

        try {
            BaseDao.executeUpdate("CREATE  DATABASE IF NOT EXISTS  " + config.getString("mysql.database"));
        } catch (SQLException e) {
            Bukkit.getLogger().info("数据库创建失败,请检查数据库配置！");
        }
        try {
            BaseDao.executeUpdate("CREATE TABLE IF NOT EXISTS 'location' (\n" +
                    "  'location_id' INT(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  'world' VARCHAR(50) DEFAULT NULL,\n" +
                    "  'x' DOUBLE DEFAULT NULL,\n" +
                    "  'y' DOUBLE DEFAULT NULL,\n" +
                    "  'z' DOUBLE DEFAULT NULL,\n" +
                    "  PRIMARY KEY ('location_id')\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8\n");
        } catch (Exception e) {
            Bukkit.getLogger().info("数据库表创建失败");
        }
    }

}
