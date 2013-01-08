package com.deaboy.invmaster;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InvMasterListener implements Listener
{
	public InvMasterListener()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, InvMaster.getInstance());
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e)
	{
		if (InvMaster.getPlayerFile(e.getPlayer()) == null)
			return;
		
		InvMaster.savePlayerFile(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		e.getPlayer().saveData();
		
		InvMaster.reloadPlayerFile(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerInventoryClick(InventoryClickEvent e)
	{
		if (e.getRawSlot() == -999) //Player is attempting to drop stuff.
			return;
		
		if (e.getInventory().getTitle().contains("Armor"))
		{
			
			int slot;
			Player p;
			ItemStack[] stack;
			
			if (e.getRawSlot() > 8 && e.isShiftClick())
			{
				switch (e.getCurrentItem().getType())
				{
				case LEATHER_HELMET:
				case IRON_HELMET:
				case CHAINMAIL_HELMET:
				case GOLD_HELMET:
				case DIAMOND_HELMET:
					slot = 3;
					break;
				case LEATHER_CHESTPLATE:
				case IRON_CHESTPLATE:
				case CHAINMAIL_CHESTPLATE:
				case GOLD_CHESTPLATE:
				case DIAMOND_CHESTPLATE:
					slot = 2;
					break;
				case LEATHER_LEGGINGS:
				case IRON_LEGGINGS:
				case CHAINMAIL_LEGGINGS:
				case GOLD_LEGGINGS:
				case DIAMOND_LEGGINGS:
					slot = 1;
					break;
				case LEATHER_BOOTS:
				case IRON_BOOTS:
				case CHAINMAIL_BOOTS:
				case GOLD_BOOTS:
				case DIAMOND_BOOTS:
					slot = 0;
					break;
				default:
					slot = -1;
				}
				
				if (slot == -1)
				{
					e.setResult(Result.DENY);
					e.setCancelled(true);
					return;
				}
				else
				{
					e.setResult(Result.DENY);
					e.setCancelled(true);
					ItemStack temp = e.getCurrentItem();
					e.getView().setItem(e.getRawSlot(), e.getView().getItem(slot));
					e.getView().setItem(slot, temp);
				}
				
				
			}
			else if (e.getRawSlot() > 8)
			{
				return;
			}
			else if (e.getRawSlot() > 3)
			{
				e.setResult(Result.DENY);
				e.setCancelled(true);
				e.setCursor(e.getCursor());
				return;
			}
			else
			{
				
				switch (e.getCursor().getType())
				{
				case AIR:
					slot = e.getRawSlot();
					break;
				case LEATHER_HELMET:
				case IRON_HELMET:
				case CHAINMAIL_HELMET:
				case GOLD_HELMET:
				case DIAMOND_HELMET:
					slot = 3;
					break;
				case LEATHER_CHESTPLATE:
				case IRON_CHESTPLATE:
				case CHAINMAIL_CHESTPLATE:
				case GOLD_CHESTPLATE:
				case DIAMOND_CHESTPLATE:
					slot = 2;
					break;
				case LEATHER_LEGGINGS:
				case IRON_LEGGINGS:
				case CHAINMAIL_LEGGINGS:
				case GOLD_LEGGINGS:
				case DIAMOND_LEGGINGS:
					slot = 1;
					break;
				case LEATHER_BOOTS:
				case IRON_BOOTS:
				case CHAINMAIL_BOOTS:
				case GOLD_BOOTS:
				case DIAMOND_BOOTS:
					slot = 0;
					break;
				default:
					slot = -1;
				}
				
				if (e.getRawSlot() != slot)
				{
					e.setResult(Result.DENY);
					e.setCancelled(true);
					e.getView().setCursor(e.getCursor());
					return;
				}
			}
			
			p = Bukkit.getPlayer(e.getInventory().getTitle().substring(0, e.getInventory().getTitle().indexOf("'s") - 1));
			
			if (p == null)
				return;
			
			stack = new ItemStack[4];
			
			for (int i = 0; i < 4; i++)
			{
				stack[i] = e.getInventory().getContents()[i] == null ? null : e.getInventory().getContents()[i].clone();
			}
			
			p.getInventory().setArmorContents(stack);
		}
		else
		{
			if (e.getInventory().getType() != InventoryType.PLAYER)
				return;
			
			Player p;
			Inventory inv;
			ItemStack[] stack;
			
			p = (Player) ((PlayerInventory) e.getInventory()).getHolder();
			
			if (((PlayerInventory) e.getInventory()).getHolder() == null)
				return;
			
			inv = InvMaster.getPlayerArmor(p);
			
			if (inv == null)
				return;
			
			
			stack = new ItemStack[9];
			
			for (int i = 0; i < 4; i++)
				stack[i] = p.getInventory().getArmorContents()[i].clone();
			
			inv.clear();
			inv.setContents(stack);
			
		}
		
	}
	
	@EventHandler
	public void onPlayerInventoryClose(InventoryCloseEvent e)
	{
		String title;
		Player p;
		ItemStack[] stack;
		
		title = e.getInventory().getTitle();
		
		if (!title.contains("Armor"))
			return;
		
		p = Bukkit.getPlayer(title.substring(0, title.indexOf("'s") - 1));
		
		if (p == null)
			return;
		
		stack = new ItemStack[4];
		
		for (int i = 0; i < 4; i++)
			stack[i] = e.getInventory().getContents()[i] == null ? null : e.getInventory().getContents()[i].clone();
		
		p.getInventory().setArmorContents(stack);
	}
}
