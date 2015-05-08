package com.walrusone.skywars.listeners;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.Game;
import com.walrusone.skywars.game.GamePlayer;
import com.walrusone.skywars.game.Game.GameState;

public class SignListener implements Listener {

    @EventHandler
    public void signPlaced(SignChangeEvent event) {
        String[] lines = event.getLines();
        if (lines[0].equalsIgnoreCase("[swr]")) {
        	if (SkyWarsReloaded.perms.has(event.getPlayer(), "swr.signs")) {
                Location signLocation = event.getBlock().getLocation();
                World w = signLocation.getWorld();
            	Block b = w.getBlockAt(signLocation);
            	if(b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST){
           			event.setCancelled(true);
           		   	String world = SkyWarsReloaded.get().getConfig().getString("spawn.world");
           			if (world != null) {
               			boolean added = SkyWarsReloaded.getGC().addSignJoinGame(signLocation, lines[1].toLowerCase());
               			if (added) {
               				event.getPlayer().sendMessage(ChatColor.GREEN + "Game Sign Succefully Added");
               			} else {
               				event.getPlayer().sendMessage(ChatColor.GREEN + "There is no map with that Name");
               			}
           			} else {
           				event.getPlayer().sendMessage(ChatColor.RED + "YOU MUST SET SPAWN IN THE LOBBY WORLD WITH /SWR SETSPAWN BEFORE STARTING A GAME");
           				SkyWarsReloaded.get().getLogger().info("YOU MUST SET SPAWN IN THE LOBBY WORLD WITH /SWR SETSPAWN BEFORE STARTING A GAME");
           			}
            	}
            } else {
            		event.getPlayer().sendMessage(ChatColor.RED + "YOU DO NOT HAVE PERMISSION TO CREATE SWR SIGNS");
        			event.setCancelled(true);
        } 
       }
    }
    
    @EventHandler
    public void signRemoved(BlockBreakEvent event) {
        Location blockLocation = event.getBlock().getLocation();
        World w = blockLocation.getWorld();
    	Block b = w.getBlockAt(blockLocation);
		if(b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST){
	    	Sign sign = (Sign) b.getState();
	    	String line1 = ChatColor.stripColor(sign.getLine(0));
			if (line1.equalsIgnoreCase("skywars")) {
            	 String world = blockLocation.getWorld().getName().toString();
         		 int x = blockLocation.getBlockX();
         		 int y = blockLocation.getBlockY();
         		 int z = blockLocation.getBlockZ();
         		 File signJoinFile = new File(SkyWarsReloaded.get().getDataFolder(), "signJoinGames.yml");
         		 if (signJoinFile.exists()) {
         			FileConfiguration storage = YamlConfiguration.loadConfiguration(signJoinFile);
                    for (String gameNumber : storage.getConfigurationSection("games.").getKeys(false)) {
                    	String world1 = storage.getString("games." + gameNumber + ".world");
                    	int x1 = storage.getInt("games." + gameNumber + ".x");
                    	int y1 = storage.getInt("games." + gameNumber + ".y");
                    	int z1 = storage.getInt("games." + gameNumber + ".z");
                    	if (x1 == x && y1 == y && z1 == z && world.equalsIgnoreCase(world1)) {
                    		if (SkyWarsReloaded.perms.has(event.getPlayer(), "swr.signs")) {
                          		SkyWarsReloaded.getGC().removeSignJoinGame(gameNumber);
                          	} else {
                          		event.setCancelled(true);
                            	event.getPlayer().sendMessage(ChatColor.RED + "YOU DO NOT HAVE PERMISSION TO DESTROY SWR SIGNS");
                    		}
                    	}
               	 	} 
         		 }
             }
		}
    }
    
    @EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		GamePlayer gPlayer = SkyWarsReloaded.getPC().getPlayer(player.getUniqueId());
    	if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
    		 if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST ) {
    				 Sign s = (Sign) e.getClickedBlock().getState();
    				 String line1 = ChatColor.stripColor(s.getLine(0));
    				 if (line1.equalsIgnoreCase("skywars")) {
    	            	 String world = s.getBlock().getWorld().getName().toString();
    	         		 int x = s.getBlock().getX();
    	         		 int y = s.getBlock().getY();
    	         		 int z = s.getBlock().getZ();
    	         		 File signJoinFile = new File(SkyWarsReloaded.get().getDataFolder(), "signJoinGames.yml");
    	         		 if (signJoinFile.exists()) {
    	         			FileConfiguration storage = YamlConfiguration.loadConfiguration(signJoinFile);
    	                    for (String gameNumber : storage.getConfigurationSection("games.").getKeys(false)) {
    	                    	String world1 = storage.getString("games." + gameNumber + ".world");
    	                    	int x1 = storage.getInt("games." + gameNumber + ".x");
    	                    	int y1 = storage.getInt("games." + gameNumber + ".y");
    	                    	int z1 = storage.getInt("games." + gameNumber + ".z");
    	                    	if (x1 == x && y1 == y && z1 == z && world.equalsIgnoreCase(world1)) {
    	                    		if (SkyWarsReloaded.perms.has(e.getPlayer(), "swr.play")) {
    	                    			Game game = SkyWarsReloaded.getGC().getGame(Integer.valueOf(gameNumber));
    	                    			if (game != null) {
        	                    			if (!game.isFull() && game.getState() == GameState.PREGAME) {
        	                    				game.addPlayer(gPlayer);
        	                    			}
    	                    			}
    	                    		}
    	                    	}	
    	                    }
    	                  }
    	             } 
    		 }
    	}
	}
    
}
