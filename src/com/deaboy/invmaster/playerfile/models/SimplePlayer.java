package com.deaboy.invmaster.playerfile.models;

import java.util.ArrayList;
import java.util.List;

public class SimplePlayer
{
	private final String player_name;
	
	public Byte		OnGround;
	public Byte		Sleeping;
	
	public Short	Air;
	public Short	AttackTime;
	public Short	DeathTime;
	public Short	Fire;
	public Short	Health;
	public Short	HurtTime;
	public Short	SleepTimer;
	
	public Integer	Dimension;
	public Integer	foodLevel;
	public Integer	foodTickTimer;
	public Integer	playerGameType;
	public Integer	XpLevel;
	public Integer	XpTotal;
	
	public Long		UUIDLeast;
	public Long		UUIDMost;
	public Long		WorldUUIDLeast;
	public Long		WorldUUIDMost;

	public Float	FallDistance;
	public Float	foodExhaustionLevel;
	public Float	foodSaturationLevel;
	public Float	XpP;
	
	public String	SpawnWorld;
	
	public List<SimpleItem>	EnderItems	= new ArrayList<SimpleItem>();
	public List<SimpleItem>	Inventory	= new ArrayList<SimpleItem>();
	public List<Double>		Motion		= new ArrayList<Double>();
	public List<Double>		Pos			= new ArrayList<Double>();
	public List<Float>		Rotation	= new ArrayList<Float>();
	
	public SimplePlayerAbilities	abilities;
	public SimplePlayerBukkit		bukkit;
	
	public SimplePlayer(String name)
	{
		this.player_name = name;
	}
	
	public String getName()
	{
		return player_name;
	}
	
}
