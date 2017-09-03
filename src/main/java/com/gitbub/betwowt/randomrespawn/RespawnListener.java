package com.gitbub.betwowt.randomrespawn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnListener implements Listener {




    @EventHandler()
    public void respawn(PlayerRespawnEvent event){
        if (Main.lists.size() == 0){
            event.getPlayer().sendMessage("返回默认的重生点");
        }else {
            new BukkitRunnable(){
                public void run() {
                    event.getPlayer().setBedSpawnLocation(Main.lists.get(createRandomNumber()),true);
//                    event.getPlayer().teleport(Main.lists.get(createRandomNumber()));
                    event.getPlayer().sendMessage("成功传送。。");
                }
            }.runTaskLater(Main.getMain(),-10L);
        }
    }

    private int createRandomNumber(){
        return (int)(Math.random()* Main.lists.size());
    }



}
