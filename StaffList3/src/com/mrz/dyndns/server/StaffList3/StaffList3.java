package com.mrz.dyndns.server.StaffList3;

import java.util.HashSet;
import java.util.Set;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.mrz.dyndns.server.CommandSystem.CommandSystem;
import com.mrz.dyndns.server.StaffList3.commands.*;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class StaffList3 extends JavaPlugin
{
	private DisguiseCraftAPI dcAPI;
	private boolean usingDC;
	
	private Chat chat;
	
	private CommandSystem cs;
	
	private Set<String> invisiblePlayers;
	
	private boolean putBracketsAroundPrefix, displayAmountOfPlayers, hideDisguisedPlayers, hideOps;
	
	@Override
	public void onEnable()
	{
		dcAPI = null;
		usingDC = false;
		
		chat = null;
		
		invisiblePlayers = new HashSet<String>();
		
		cs = new CommandSystem(this);
		cs.registerCommand("stafflist", new ViewStaffListCommand(this));
		cs.registerCommand("stafflist reload", new ReloadConfigCommand(this));
		ToggleHideCommand thc = new ToggleHideCommand(this);
		cs.registerCommand("stafflist hide", thc);
		cs.registerCommand("stafflist toggle", thc);
		cs.registerCommand("stafflist unhide", thc);
		cs.registerCommand("stafflist status", new HiddenStatusCommand(this));
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		putBracketsAroundPrefix = getConfig().getBoolean("putBracketsAroundPrefix");
		displayAmountOfPlayers = getConfig().getBoolean("displayAmountOfPlayers");
		hideDisguisedPlayers = getConfig().getBoolean("hideDisguisedPlayers");
		hideOps = getConfig().getBoolean("hideOps");
		
		if(getServer().getPluginManager().getPlugin("DisguiseCraft") != null)
		{
			dcAPI = DisguiseCraft.getAPI();
			getLogger().info("Found and using DisguiseCraft");
			usingDC = true;
		}
		
		if (getServer().getPluginManager().getPlugin("Vault") != null)
		{
			try 
			{
				setupChat();
			} 
			catch (NullPointerException e)
			{
				Bukkit.getConsoleSender().sendMessage("[StaffList] " + ChatColor.RED + "You have vault, but theres no chat system to hook into! Disabling...");
				//logger.warning("You have vault, but theres no chat system to hook into! Disabling...");
				this.getPluginLoader().disablePlugin(this);
				return;
			}

			getLogger().info("Vault and a chat system found! :)");
		}
		else
		{
			Bukkit.getConsoleSender().sendMessage("[StaffList] " + ChatColor.RED + "Vault not found! Disabling...");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
	}
	
	@Override
	public void onDisable()
	{
		cs.close();
	}
	
	private boolean setupChat() throws NullPointerException
	{
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if(rsp.getProvider() == null)
        {
        	throw new NullPointerException();
        }
        chat = rsp.getProvider();
        return chat != null;
	}
	
	public String getPrefix(Player player)
	{
		return chat.getPlayerPrefix(player);
	}
	
	public boolean playerGoesOnStaffList(Player player)
	{
		if(usingDC && dcAPI.isDisguised(player) && isHideDisguisedPlayers()) {
			return false;
		}
		
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			return false;
		}
		
		if(player.hasPermission("StaffList.show") == false)
		{
			return false;
		}
		
		if(invisiblePlayers.contains(player.getName())) {
			return false;
		}
		
		if(player.isOp() && isHideOps()) {
			return false;
		}
		
		return true;
	}
	
	public Set<String> getInvisiblePlayers() {
		return invisiblePlayers;
	}

	public boolean isPutBracketsAroundPrefix()
	{
		return putBracketsAroundPrefix;
	}

	public boolean isDisplayAmountOfPlayers()
	{
		return displayAmountOfPlayers;
	}

	public boolean isHideDisguisedPlayers()
	{
		return hideDisguisedPlayers;
	}

	public boolean isHideOps()
	{
		return hideOps;
	}
}
