package com.deaboy.invmaster;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.deaboy.invmaster.playerfile.PlayerFile;

public class InvMaster extends JavaPlugin
{
	private static HashMap<OfflinePlayer, PlayerFile> playerfiles = new HashMap<OfflinePlayer, PlayerFile>();
	private static HashMap<PlayerFile, Inventory> inventories = new HashMap<PlayerFile, Inventory>();
	private static HashMap<PlayerFile, Inventory> enderchests = new HashMap<PlayerFile, Inventory>();
	private static HashMap<PlayerFile, Inventory> armor = new HashMap<PlayerFile, Inventory>();
	
	@Override
	public void onEnable()
	{
		new InvMasterListener();
		new InvMasterCommands();
		
		// Schedule the autosaver
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable()
		{
			public void run()
			{
				for (OfflinePlayer player : playerfiles.keySet())
				{
					savePlayerFile(player);
				}
			}
		}, 600, 600);
	}
	
	@Override
	public void onDisable()
	{
		for (OfflinePlayer player : playerfiles.keySet())
		{
			savePlayerFile(player);
		}
		
		playerfiles.clear();
		inventories.clear();
		enderchests.clear();
		armor.clear();
	}
	
	public static InvMaster getInstance()
	{
		return (InvMaster) Bukkit.getServer().getPluginManager().getPlugin("InvMaster");
	}
	
	/**
	 * Opens a player's .dat file and loads its contents into memory.
	 * @param p
	 * @return
	 */
	public static boolean openPlayerFile(OfflinePlayer p)
	{
		if (p == null || !playerFileExists(p))
			return false;
		
		if (playerfiles.containsKey(p))
			return false;
		
		if (p.isOnline())
		{
			((Player) p).saveData();
		}
		
		PlayerFile pfile = new PlayerFile(p);
		
		playerfiles.put(p, pfile);
		inventories.put(pfile, Bukkit.createInventory(null, 36, p.getName() + "'s Inventory"));
		enderchests.put(pfile, Bukkit.createInventory(null, 27, p.getName() + "'s Ender Chest"));
		armor.put(pfile, Bukkit.createInventory(null, 9, p.getName() + "'s Armor"));

		inventories.get(pfile).setContents(pfile.getInventoryContents());
		enderchests.get(pfile).setContents(pfile.getEnderchestContents());
		armor.get(pfile).setContents(pfile.getArmorContents());
		
		return true;
	}
	
	/**
	 * Gets a loaded player's PlayerFile.
	 * @param p The OfflinePlayer pointer to the player
	 * @return The PlayerFile object for that player. Null if it doesn't exist.
	 */
	public static PlayerFile getPlayerFile(OfflinePlayer p)
	{
		if (p == null)
			return null;
		
		if (playerfiles.containsKey(p))
		{
			playerfiles.get(p).setChanged();
			return playerfiles.get(p);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns the player's inventory and null if the player's file isn't already loaded.
	 * @param p
	 * @return The player's inventory.
	 */
	public static Inventory getPlayerInventory(OfflinePlayer p)
	{
		if (p == null)
			return null;
		
		if (p.isOnline())
		{
			return Bukkit.getPlayer(p.getName()).getInventory();
		}
		else if (playerfiles.containsKey(p) && inventories.containsKey(playerfiles.get(p)))
		{
			playerfiles.get(p).setChanged();
			return inventories.get(playerfiles.get(p));
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns the player's Enderchest and null if the player's file isn't already loaded.
	 * @param p
	 * @return The player's inventory.
	 */
	public static Inventory getPlayerEnderchest(OfflinePlayer p)
	{
		if (p == null)
			return null;
		
		if (p.isOnline())
		{
			return Bukkit.getPlayer(p.getName()).getEnderChest();
		}
		else if (playerfiles.containsKey(p) && enderchests.containsKey(playerfiles.get(p)))
		{
			playerfiles.get(p).setChanged();
			return enderchests.get(playerfiles.get(p));
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns the player's Armor contents, and null if the player's file isn't already loaded.
	 * Reloads the player's armor contents from Player object because there is no separate
	 * inventory for player armor that exists.
	 * @param p
	 * @return The player's armor.
	 */
	public static Inventory getPlayerArmor(OfflinePlayer p)
	{
		if (p == null)
			return null;
		
		if (!(playerfiles.containsKey(p) && armor.containsKey(playerfiles.get(p))))
			return null;
		
		if (p.isOnline())
		{
			playerfiles.get(p).setChanged();
			armor.get(getPlayerFile(p)).setContents(Bukkit.getPlayer(p.getName()).getInventory().getArmorContents());
			return armor.get(getPlayerFile(p));
		}
		else if (playerfiles.containsKey(p) && enderchests.containsKey(playerfiles.get(p)))
		{
			playerfiles.get(p).setChanged();
			return armor.get(playerfiles.get(p));
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * Gets a pointer to an inventory and returns true if it's an armor inventory.
	 */
	public static boolean isArmorInventory(Inventory inv)
	{
		for (Inventory i : armor.values())
			if (i.getTitle().equals(inv.getTitle()))
				return true;
		return false;
	}
	
	/**
	 * Saves a given player's inventory and enderchest.
	 * @param p
	 */
	public static void savePlayerFile(OfflinePlayer p)
	{
		if (p == null || !playerfiles.containsKey(p))
			return;
		
		PlayerFile pfile = playerfiles.get(p);
		
		if (pfile.isChanged())
		{
			if (inventories.containsKey(pfile))
			{
				pfile.setInventoryContents(inventories.get(pfile).getContents());
			}
			if (enderchests.containsKey(pfile))
			{
				pfile.setEnderchestContents(enderchests.get(pfile).getContents());
			}
			if (armor.containsKey(pfile))
			{
				pfile.setArmorContents(armor.get(pfile).getContents());
			}
			pfile.saveInventories();
		}
	}
	
	/**
	 * Reloads a given player's inventory and stuff from file
	 * @param p
	 */
	public static void reloadPlayerFile(OfflinePlayer p)
	{
		if (p == null || !playerfiles.containsKey(p))
			return;
		
		PlayerFile pfile = playerfiles.get(p);
		pfile.reloadData();
		
		Inventory inv;
		
		inv = (inventories.containsKey(pfile) ? inventories.get(pfile) : Bukkit.createInventory(null, 36, p.getName() + "'s Inventory"));
		inv.setContents(pfile.getInventoryContents());
		inventories.put(pfile, inv);
		
		inv = (enderchests.containsKey(pfile) ? enderchests.get(pfile) : Bukkit.createInventory(null, 27, p.getName() + "'s Ender Chest"));
		inv.setContents(pfile.getEnderchestContents());
		enderchests.put(pfile, inv);
		
		inv = (armor.containsKey(pfile) ? armor.get(pfile) : Bukkit.createInventory(null, 27, p.getName() + "'s Armor"));
		inv.setContents(pfile.getArmorContents());
		armor.put(pfile, inv);
		
		
	}
	
	/**
	 * Checks if a player's .dat file exists in the main world's folder.
	 * @param p The OfflinePlayer object to use to check
	 * @return True if the player's .dat file exists on disk, false if not.
	 */
	public static boolean playerFileExists(OfflinePlayer p)
	{
		return new File(Bukkit.getWorlds().get(0).getWorldFolder() + "/players/" + p.getName() + ".dat").exists();
	}
}
