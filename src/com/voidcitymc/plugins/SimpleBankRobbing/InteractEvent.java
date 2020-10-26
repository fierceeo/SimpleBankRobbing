package com.voidcitymc.plugins.SimpleBankRobbing;

import com.voidcitymc.api.SimplePolice.SimplePoliceAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.craftersland.money.Money;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;


public class InteractEvent implements Listener {
	private final CooldownManager cooldownManager = new CooldownManager();
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onInteract(final PlayerInteractEvent event) {



		if (event.getClickedBlock() != null) {
			if (Main.getInstance().getConfig().getString("World") != null) {
				Location loc = new Location(Bukkit.getWorld(Objects.requireNonNull(Main.getInstance().getConfig().getString("World"))), Main.getInstance().getConfig().getInt("X"), Main.getInstance().getConfig().getInt("Y"), Main.getInstance().getConfig().getInt("Z"));
				if (event.getClickedBlock().getLocation().equals(loc) && event.getClickedBlock().getType().equals(Material.PLAYER_HEAD)) {
					//if (event.getClickedBlock().getType().equals(Material.PLAYER_HEAD)) {

					SimplePoliceAPI api = (SimplePoliceAPI) Bukkit.getPluginManager().getPlugin("SimplePolice");
					ArrayList<String> policeList = api.listPolice();
					ArrayList<Player> playerList = new ArrayList<Player>();

					int i = 0;


					//online police list
					while (i < policeList.size()) {
						if (Bukkit.getPlayer(UUID.fromString(policeList.get(i))) != null) {
							playerList.add(Bukkit.getPlayer(UUID.fromString(policeList.get(i))));
						}
						i++;
					}


					final Player player = event.getPlayer();

					if (playerList.size() > 0) {

						if (!playerList.contains(player)) {

							long timeLeft = System.currentTimeMillis() - cooldownManager.getCooldown(player.getUniqueId());

							if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.DEFAULT_COOLDOWN) {
								cooldownManager.setCooldown(player.getUniqueId(), System.currentTimeMillis());

								//rob bank
								Money pl = (Money) Bukkit.getServer().getPluginManager().getPlugin("MysqlEconomyBank");
								ArrayList<Player> players = new ArrayList<Player>();
								for (Player p : Bukkit.getServer().getOnlinePlayers()) {
									players.add(p);
								}

								players.remove(player);

								double bal = 0;
								i = 0;
								double stolenMoney = 0;

								while (i < players.size()) {
									if (pl.getMoneyDatabaseInterface().hasAccount(players.get(i))) {
										bal = pl.getMoneyDatabaseInterface().getBalance(players.get(i));
										pl.getMoneyDatabaseInterface().setBalance(players.get(i), (bal * 0.9));
										stolenMoney = bal * 0.1 + stolenMoney;
										players.get(i).sendMessage(ChatColor.RED + "A player has robbed the bank, you have lost $" + Math.round(bal * 0.1));
									}
									i++;
								}


								i = 0;

								while (i < playerList.size()) {
									playerList.get(i).sendMessage(ChatColor.DARK_AQUA + "[911] " + ChatColor.WHITE + player.getName() + " has robbed the bank!");
									i++;
								}


								player.sendMessage(ChatColor.RED + "You have robbed the bank and gotten $" + Math.round(stolenMoney * 0.5));
								double myBal = pl.getMoneyDatabaseInterface().getBalance(player);
								pl.getMoneyDatabaseInterface().setBalance(player, myBal + (stolenMoney * 0.5));


								cooldownManager.setCooldown(player.getUniqueId(), System.currentTimeMillis());


							} else {
								player.sendMessage(ChatColor.RED.toString() + "You can rob the bank again in " + (CooldownManager.DEFAULT_COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timeLeft)) + " seconds");
							}

						} else {
							player.sendMessage(ChatColor.RED + "Police can't rob the bank!");
						}


					} else {
						player.sendMessage(ChatColor.RED + "You can only rob the bank when the police are online!");
					}
				}
			}
		}
     

    }
	

	    
	
}
