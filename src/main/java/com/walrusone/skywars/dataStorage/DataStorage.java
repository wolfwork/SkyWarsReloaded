package com.walrusone.skywars.dataStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.GamePlayer;

public class DataStorage {

	public void savePlayerSync(final GamePlayer player) {
		boolean sqlEnabled = SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled");
		if (!sqlEnabled) {
			try {
	            File dataDirectory = SkyWarsReloaded.get().getDataFolder();
	            File playerDataDirectory = new File(dataDirectory, "player_data");

	            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
	                System.out.println("Failed to load player " + player.getName() + ": Could not create player_data directory.");
	                return;
	            }

	            File playerFile = new File(playerDataDirectory, player.getUUID().toString() + ".yml");
	            if (!playerFile.exists() && !playerFile.createNewFile()) {
	                System.out.println("Failed to load player " + player.getName() + ": Could not create player file.");
	                return;
	            }

	            copyDefaults(playerFile);
	            FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
	            fc.set("name", player.getName());
	            fc.set("wins", player.getWins());
	            fc.set("kills", player.getKills());
	            fc.set("deaths", player.getDeaths());
	            fc.set("gamesPlayed", player.getGamesPlayed());
	            fc.set("score", player.getScore());
	            fc.set("balance", player.getBalance());
	            double killDeath = 0;
	            if (player.getDeaths() != 0) {
	                killDeath = ((double) player.getKills())/player.getDeaths();
	            }
	            fc.set("killDeath", killDeath);
	            fc.set("blocksPlaced", player.getBlocks());
	            fc.save(playerFile);
	            
	        } catch (IOException ioException) {
	            System.out.println("Failed to load player " + player + ": " + ioException.getMessage());
	        }
		} else {
            Database database = SkyWarsReloaded.getDB();

            if (!database.checkConnection()) {
                return;
            }

            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = null;

            try {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("UPDATE `swreloaded_player` SET ");
                queryBuilder.append("`playername` = ?, `score` = ?, `games_played` = ?, ");
                queryBuilder.append("`games_won` = ?, `kills` = ?, ");
                queryBuilder.append("`deaths` = ?, `killdeath` = ?, `blocksplaced` = ?, `last_seen` = NOW(), `balance` = ? ");
                queryBuilder.append("WHERE `uuid` = ?;");

                preparedStatement = connection.prepareStatement(queryBuilder.toString());
                preparedStatement.setString(1, player.getName());
                preparedStatement.setInt(2, player.getScore());
                preparedStatement.setInt(3, player.getGamesPlayed());
                preparedStatement.setInt(4, player.getWins());
                preparedStatement.setInt(5, player.getKills());
                preparedStatement.setInt(6, player.getDeaths());
                double killDeath = 0;
                if (player.getDeaths() != 0) {
                	killDeath = ((double) player.getKills())/player.getDeaths();
                }
                preparedStatement.setDouble(7, killDeath);
                preparedStatement.setInt(8, player.getBlocks());
                preparedStatement.setInt(9, player.getBalance());
                preparedStatement.setString(10, player.getUUID().toString());
                preparedStatement.executeUpdate();

            } catch (final SQLException sqlException) {
                sqlException.printStackTrace();

            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (final SQLException ignored) {
                    }
                }
            }
        }
	}

	public void savePlayerAsync(final GamePlayer player) {
		Bukkit.getScheduler().runTaskAsynchronously(SkyWarsReloaded.get(), new Runnable() {
            @Override
            public void run() {
        		boolean sqlEnabled = SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled");
        		if (!sqlEnabled) {
        			try {
        	            File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        	            File playerDataDirectory = new File(dataDirectory, "player_data");

        	            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
        	                System.out.println("Failed to load player " + player.getName() + ": Could not create player_data directory.");
        	                return;
        	            }

        	            File playerFile = new File(playerDataDirectory, player.getUUID().toString() + ".yml");
        	            if (!playerFile.exists() && !playerFile.createNewFile()) {
        	                System.out.println("Failed to load player " + player.getName() + ": Could not create player file.");
        	                return;
        	            }

        	            copyDefaults(playerFile);
        	            FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
        	            fc.set("name", player.getName());
        	            fc.set("wins", player.getWins());
        	            fc.set("kills", player.getKills());
        	            fc.set("deaths", player.getDeaths());
        	            fc.set("gamesPlayed", player.getGamesPlayed());
        	            fc.set("score", player.getScore());
        	            fc.set("balance", player.getBalance());
        	            double killDeath = 0;
        	            if (player.getDeaths() != 0) {
        	                killDeath = ((double) player.getKills())/player.getDeaths();
        	            }
        	            fc.set("killDeath", killDeath);
        	            fc.set("blocksPlaced", player.getBlocks());
        	            fc.save(playerFile);
        	            
        	        } catch (IOException ioException) {
        	            System.out.println("Failed to load player " + player + ": " + ioException.getMessage());
        	        }
        		} else {
                    Database database = SkyWarsReloaded.getDB();

                    if (!database.checkConnection()) {
                        return;
                    }

                    Connection connection = database.getConnection();
                    PreparedStatement preparedStatement = null;

                    try {
                        StringBuilder queryBuilder = new StringBuilder();
                        queryBuilder.append("UPDATE `swreloaded_player` SET ");
                        queryBuilder.append("`playername` = ?, `score` = ?, `games_played` = ?, ");
                        queryBuilder.append("`games_won` = ?, `kills` = ?, ");
                        queryBuilder.append("`deaths` = ?, `killdeath` = ?, `blocksplaced` = ?, `last_seen` = NOW(), `balance` = ? ");
                        queryBuilder.append("WHERE `uuid` = ?;");

                        preparedStatement = connection.prepareStatement(queryBuilder.toString());
                        preparedStatement.setString(1, player.getName());
                        preparedStatement.setInt(2, player.getScore());
                        preparedStatement.setInt(3, player.getGamesPlayed());
                        preparedStatement.setInt(4, player.getWins());
                        preparedStatement.setInt(5, player.getKills());
                        preparedStatement.setInt(6, player.getDeaths());
                        double killDeath = 0;
                        if (player.getDeaths() != 0) {
                        	killDeath = ((double) player.getKills())/player.getDeaths();
                        }
                        preparedStatement.setDouble(7, killDeath);
                        preparedStatement.setInt(8, player.getBlocks());
                        preparedStatement.setInt(9, player.getBalance());
                        preparedStatement.setString(10, player.getUUID().toString());
                        preparedStatement.executeUpdate();

                    } catch (final SQLException sqlException) {
                        sqlException.printStackTrace();

                    } finally {
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (final SQLException ignored) {
                            }
                        }
                    }
                }
            }
		});
	}
	
	public void loadPlayer(final GamePlayer player) {
		boolean sqlEnabled = SkyWarsReloaded.get().getConfig().getBoolean("sqldatabase.enabled");
		if (sqlEnabled) {
	        Bukkit.getScheduler().runTaskAsynchronously(SkyWarsReloaded.get(), new Runnable() {
	            @Override
	            public void run() {
	                Database database = SkyWarsReloaded.getDB();

	                if (!database.checkConnection()) {
	                    return;
	                }

	                if (!database.doesPlayerExist(player.getP().getUniqueId().toString())) {
	                    database.createNewPlayer(player.getUUID().toString());

	                } else {
	                    Connection connection = database.getConnection();
	                    PreparedStatement preparedStatement = null;
	                    ResultSet resultSet = null;

	                    try {
	                        StringBuilder queryBuilder = new StringBuilder();
	                        queryBuilder.append("SELECT `score`, `games_played`, `games_won`, `kills`, `deaths`, `blocksplaced`, `balance` ");
	                        queryBuilder.append("FROM `swreloaded_player` ");
	                        queryBuilder.append("WHERE `uuid` = ? ");
	                        queryBuilder.append("LIMIT 1;");

	                        preparedStatement = connection.prepareStatement(queryBuilder.toString());
	                        preparedStatement.setString(1, player.getP().getUniqueId().toString());
	                        resultSet = preparedStatement.executeQuery();

	                        if (resultSet != null && resultSet.next()) {
	                            player.setScore(resultSet.getInt("score"));
	                            player.setGamesPlayed(resultSet.getInt("games_played"));
	                            player.setWins(resultSet.getInt("games_won"));
	                            player.setKills(resultSet.getInt("kills"));
	                            player.setDeaths(resultSet.getInt("deaths"));
	                            player.setBlocks(resultSet.getInt("blocksplaced"));
	                            player.setBalance(resultSet.getInt("balance"));
	                        }

	                    } catch (final SQLException sqlException) {
	                        sqlException.printStackTrace();

	                    } finally {
	                        if (resultSet != null) {
	                            try {
	                                resultSet.close();
	                            } catch (final SQLException ignored) {
	                            }
	                        }

	                        if (preparedStatement != null) {
	                            try {
	                                preparedStatement.close();
	                            } catch (final SQLException ignored) {
	                            }
	                        }
	                    }
	                }
	            }
	        });
		} else {
			Bukkit.getScheduler().runTaskAsynchronously(SkyWarsReloaded.get(), new Runnable() {
	            @Override
	            public void run() {
	    			try {
	    	            File dataDirectory = SkyWarsReloaded.get().getDataFolder();
	    	            File playerDataDirectory = new File(dataDirectory, "player_data");

	    	            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
	    	                System.out.println("Failed to load player " + player.getName() + ": Could not create player_data directory.");
	    	                return;
	    	            }

	    	            File playerFile = new File(playerDataDirectory, player.getP().getUniqueId().toString() + ".yml");
	    	            if (!playerFile.exists() && !playerFile.createNewFile()) {
	    	                System.out.println("Failed to load player " + player.getName() + ": Could not create player file.");
	    	                return;
	    	            }

	    	            copyDefaults(playerFile);
	    	            FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
	    	            player.setWins(fc.getInt("wins"));
	    	            player.setKills(fc.getInt("kills"));
	    	            player.setDeaths(fc.getInt("deaths"));
	    	            player.setGamesPlayed(fc.getInt("gamesPlayed"));
	    	            player.setScore(fc.getInt("score"));
	    	            player.setBlocks(fc.getInt("blocksPlaced"));
	    	            player.setBalance(fc.getInt("balance"));

	    	            
	    	        } catch (IOException ioException) {
	    	            System.out.println("Failed to load player " + player + ": " + ioException.getMessage());
	    	        }
	            }
			});
		}
	}
	
	private void copyDefaults(File playerFile) {
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		Reader defConfigStream = new InputStreamReader(SkyWarsReloaded.get().getResource("playerFile.yml"));
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			playerConfig.options().copyDefaults(true);
			playerConfig.setDefaults(defConfig);
			try {
				playerConfig.save(playerFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
