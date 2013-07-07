package com.deaboy.invmaster.playerfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_6_R1.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_6_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.FileUtil;
import org.jnbt.ByteTag;
import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.Tag;

public class PlayerFile
{
	private OfflinePlayer player;
	
	ItemStack[] Inventory	= new ItemStack[36];
	ItemStack[] Armor		= new ItemStack[9];
	ItemStack[] Enderchest	= new ItemStack[27];
	
	private boolean changed = false;
	
	private final static String path = Bukkit.getWorlds().get(0).getWorldFolder() + "/players/";
	
	public PlayerFile(OfflinePlayer p)
	{
		this.player = p;
		
		CompoundTag tag = load();
		
		importInventory(tag);
		importArmor(tag);
		importEnderchest(tag);
	}
	
	
	// ---------------- Getters and Setters ---------------- //
	
	/**
	 * Reloads all the data from file, overwriting any changes.
	 */
	public void reloadData()
	{
		CompoundTag tag = load();
		importInventory(tag);
		importArmor(tag);
		importEnderchest(tag);
	}
	
	/**
	 * Loads a player's data, exports this PlayerFile's inventory and
	 * Enderchest contents, then writes it all to data. If player is
	 * online, will take the player into account as well, forcing them
	 * to save and reload their data.
	 */
	public void saveInventories()
	{
		if (player.isOnline())
		{
			Bukkit.getPlayer(player.getName()).saveData();
		}
		
		CompoundTag tag = load();
		
		if (tag != null)
		{
			exportInventory(tag);
			exportEnderchest(tag);
			
			save(tag);
		}
		
		if (player.isOnline())
		{
			Bukkit.getPlayer(player.getName()).loadData();
		}
	}
	
	/**
	 * Saves everything in memory to the player's file, whether
	 * they're online or offline. Can be catastrophic.
	 * This is dangerous: DO NOT USE IF THE PLAYER IS ONLINE
	 */
	public void forceSave()
	{
		CompoundTag tag = load();
		
		if (tag != null)
		{
			exportInventory(tag);
			exportEnderchest(tag);
			save(tag);
		}
	}
	
	/**
	 * Gets the inventory contents for this player file.
	 * @return A clone of the inventory contents of this PlayerFile.
	 */
	public ItemStack[] getInventoryContents()
	{
		ItemStack[] stack = new ItemStack[Inventory.length];
		
		for (int i = 0; i < stack.length; i++)
		{
			if (Inventory[i] != null)
				stack[i] = Inventory[i].clone();
		}
		
		return stack;
	}

	/**
	 * Gets the armor contents for this player file.
	 * @return A clone of the armor contents of this PlayerFile.
	 */
	public ItemStack[] getArmorContents()
	{
		ItemStack[] stack = new ItemStack[Armor.length];
		
		for (int i = 0; i < stack.length; i++)
		{
			if (Armor[i] != null)
				stack[i] = Armor[i].clone();
		}
		
		return stack;
	}

	/**
	 * Gets the Enderchest contents for this player file.
	 * @return A clone of the Enderchest contents of this PlayerFile.
	 */
	public ItemStack[] getEnderchestContents()
	{
		ItemStack[] stack = new ItemStack[Enderchest.length];
		
		for (int i = 0; i < stack.length; i++)
		{
			if (Enderchest[i] != null)
				stack[i] = Enderchest[i].clone();
		}
		
		return stack;
	}
	
	/**
	 * Sets the inventory contents for this PlayerFile
	 * @param contents
	 */
	public void setInventoryContents(ItemStack[] contents)
	{
		for (int i = 0; i < Inventory.length; i++)
		{
			if (i < contents.length && contents[i] != null)
				Inventory[i] = contents[i].clone();
			else
				Inventory[i] = null;
		}
	}
	
	/**
	 * Sets the armor contents of this PlayerFile
	 * @param contents
	 */
	public void setArmorContents(ItemStack[] contents)
	{
		for (int i = 0; i < Armor.length; i++)
		{
			if (i < contents.length && contents[i] != null)
				Armor[i] = contents[i].clone();
			else
				Armor[i] = null;
		}
	}
	
	/**
	 * Sets the Enderchest contents of this PlayerFile
	 * @param contents
	 */
	public void setEnderchestContents(ItemStack[] contents)
	{
		for (int i = 0; i < Enderchest.length; i++)
		{
			if (i < contents.length && contents[i] != null)
				Enderchest[i] = contents[i].clone();
			else
				Enderchest[i] = null;
		}
	}
	
	public boolean isChanged()
	{
		return changed;
	}
	
	public void setChanged()
	{
		changed = true;
	}
	
	
	// -------- Inventory Methods -------- //
	
	/**
	 * Imports inventory contents from file. Does not include armor.
	 * @return
	 */
	private ItemStack[] importInventory(CompoundTag tag)
	{
		ItemStack[] inv = new ItemStack[36];
		
		for (Tag t : ((ListTag) tag.getValue().get("Inventory")).getValue())
		{
			int slot = ((ByteTag) ((CompoundTag) t).getValue().get("Slot")).getValue();
			if (slot < 36)
			{
				inv[slot] = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R1.ItemStack.createStack(((CompoundTag) t).toNBTTag()));
			}
		}
		
		Inventory = inv;
		return inv;
	}
	
	/**
	 * Imports armor contents from file.
	 * @return
	 */
	private ItemStack[] importArmor(CompoundTag tag)
	{
		ItemStack[] inv = new ItemStack[4];
		
		for (Tag t : ((ListTag) tag.getValue().get("Inventory")).getValue())
		{
			int slot = ((ByteTag) ((CompoundTag) t).getValue().get("Slot")).getValue();
			if (slot >= 100 && slot <= 103)
			{
				inv[slot-100] = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R1.ItemStack.createStack(((CompoundTag) t).toNBTTag()));
			}
		}
		
		Armor = inv;
		return inv;
	}
	
	/**
	 * Imports Enderchest contents from file.
	 * @return
	 */
	private ItemStack[] importEnderchest(CompoundTag tag)
	{
		ItemStack[] inv = new ItemStack[27];
		
		for (Tag t : ((ListTag) tag.getValue().get("EnderItems")).getValue())
		{
			int slot = ((ByteTag) ((CompoundTag) t).getValue().get("Slot")).getValue();
			if (slot < 36)
			{
				inv[slot] = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R1.ItemStack.createStack(((CompoundTag) t).toNBTTag()));
			}
		}
		
		Enderchest = inv;
		return inv;
	}
	
	/**
	 * Exports inventory and armor contents to file, but does not write to disk.
	 * @param contents
	 * @param armour
	 */
	private void exportInventory(CompoundTag tag)
	{
		List<Tag> list = new ArrayList<Tag>();
		
		for (byte i = 0; i < Inventory.length; i++)
		{
			if (Inventory[i] == null)
				continue;
			
			Tag t = Tag.fromNBTTag(CraftItemStack.asNMSCopy(Inventory[i]).save(new NBTTagCompound()));
			
			((CompoundTag) t).getValue().put("Slot", new ByteTag("Slot", i));
			
			if (t != null)
				list.add(t);
		}
		
		for (byte i = 0; i < Armor.length; i++)
		{
			if (Armor[i] == null)
				continue;
			
			Tag t = Tag.fromNBTTag(CraftItemStack.asNMSCopy(Armor[i]).save(new NBTTagCompound()));
			
			((CompoundTag) t).getValue().put("Slot", new ByteTag("Slot", (byte) (i + 100)));
			
			if (t != null)
				list.add(t);
		}
		
		tag.getValue().put("Inventory", new ListTag("Inventory", CompoundTag.class ,list));
	}
	
	/**
	 * Exports Enderchest contents to file, but does not write to disk.
	 * @param enderitems
	 */
	private void exportEnderchest(CompoundTag tag)
	{
		List<Tag> list = new ArrayList<Tag>();
		
		for (byte i = 0; i < Enderchest.length; i++)
		{
			if (Enderchest[i] == null)
				continue;

			Tag t = Tag.fromNBTTag(CraftItemStack.asNMSCopy(Enderchest[i]).save(new NBTTagCompound()));
			
			((CompoundTag) t).getValue().put("Slot", new ByteTag("Slot", i));
			
			if (t != null)
				list.add(t);
		}
		
		tag.getValue().put("EnderItems", new ListTag("EnderItems", CompoundTag.class, list));
	}
	
	
	// -------- File loading and saving -------- //
	
	/**
	 * Loads the PlayerFile from file
	 */
	private CompoundTag load()
	{
		File file = new File(path + player.getName() + ".dat");
		FileInputStream input_stream;
		NBTInputStream nbt_input;
		CompoundTag tag;
		
		try
		{
			input_stream = new FileInputStream(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		try
		{
			nbt_input = new NBTInputStream(input_stream);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		try
		{
			tag = (CompoundTag) nbt_input.readTag();
		}
		catch (IOException e)
		{
			tag = null;
			e.printStackTrace();
		}
		try
		{
			nbt_input.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return tag;
	}

	/**
	 * Saves the PlayerFile to file
	 */
	private void save(CompoundTag tag)
	{
		File file = new File(path + player.getName() + ".dat");
		File temp;
		FileOutputStream output_stream;
		NBTOutputStream nbt_output;
		
		if (!file.exists())
		{
			if (file.getParent() != null)
				file.getParentFile().mkdirs();
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
		}
		
		try
		{
			temp = File.createTempFile(player.getName(), ".dat.temp");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		try
		{
			output_stream = new FileOutputStream(temp);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		try
		{
			nbt_output = new NBTOutputStream(output_stream);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		try
		{
			nbt_output.writeTag(tag);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			nbt_output.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		FileUtil.copy(temp, file);
		temp.delete();
		
		
		changed = false;
	}
	
}
