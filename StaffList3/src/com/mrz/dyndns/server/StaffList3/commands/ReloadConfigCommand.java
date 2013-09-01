package com.mrz.dyndns.server.StaffList3.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mrz.dyndns.server.CommandSystem.SimpleCommand;
import com.mrz.dyndns.server.StaffList3.StaffList3;

public class ReloadConfigCommand implements SimpleCommand
{
	public ReloadConfigCommand(StaffList3 plugin) {
		this.plugin = plugin;
	}
	
	private final StaffList3 plugin;
	
	@Override
	public boolean Execute(String commandName, CommandSender sender, List<String> args)
	{
		if(sender.hasPermission("stafflist.reload")) {
			plugin.reloadConfig();
			plugin.onDisable();
			plugin.onEnable();
			sender.sendMessage(ChatColor.GREEN + "StaffList reloaded");
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			return true;
		}
	}

}
