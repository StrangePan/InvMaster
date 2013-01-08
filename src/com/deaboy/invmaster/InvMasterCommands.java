package com.deaboy.invmaster;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InvMasterCommands implements CommandExecutor
{
	public InvMasterCommands()
	{
		Bukkit.getPluginCommand("inventory").setExecutor(this);
		Bukkit.getPluginCommand("enderchest").setExecutor(this);
		Bukkit.getPluginCommand("armor").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("inventory")) // View a player's enderchest.
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage("The server cannot do that!");
				return true;
			}
			if (!sender.isOp())
			{
				sender.sendMessage("You don't have permission to do that!");
				return true;
			}
			if (args.length != 1)
			{
				return false;
			}

			OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
			Inventory inv = InvMaster.getPlayerInventory(p);
			
			if (inv == null)
			{
				if (!InvMaster.openPlayerFile(p))
				{
					sender.sendMessage("That player does not exist.");
					return true;
				}
				else
				{
					inv = InvMaster.getPlayerInventory(p);
				}
			}
			
			((Player) sender).openInventory(inv);
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("enderchest"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage("The server cannot do that!");
				return true;
			}
			if (!sender.isOp())
			{
				sender.sendMessage("You don't have permission to do that!");
				return true;
			}
			if (args.length != 1)
			{
				return false;
			}

			OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
			Inventory inv = InvMaster.getPlayerEnderchest(p);
			
			if (inv == null)
			{
				if (!InvMaster.openPlayerFile(p))
				{
					sender.sendMessage("That player does not exist.");
					return true;
				}
				else
				{
					inv = InvMaster.getPlayerEnderchest(p);
				}
			}
			
			((Player) sender).openInventory(inv);
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("armor"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage("The server cannot do that!");
				return true;
			}
			if (!sender.isOp())
			{
				sender.sendMessage("You don't have permission to do that!");
				return true;
			}
			if (args.length != 1)
			{
				return false;
			}

			OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
			Inventory inv = InvMaster.getPlayerArmor(p);
			
			if (inv == null)
			{
				if (!InvMaster.openPlayerFile(p))
				{
					sender.sendMessage("That player does not exist.");
					return true;
				}
				else
				{
					inv = InvMaster.getPlayerArmor(p);
				}
			}
			
			((Player) sender).openInventory(inv);
			
			return true;
		}
		
		return false;
	}

}
