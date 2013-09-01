package com.mrz.dyndns.server.StaffList3.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.CommandSystem.SimpleCommand;
import com.mrz.dyndns.server.StaffList3.StaffList3;

public class ToggleHideCommand implements SimpleCommand
{
	public ToggleHideCommand(StaffList3 plugin) {
		this.plugin = plugin;
	}
	
	private final StaffList3 plugin;

	@Override
	public boolean Execute(String commandName, CommandSender sender, List<String> args)
	{
		Player player;
		if(sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use that command!");
			return true;
		}
		
		if(sender.hasPermission("stafflist.togglehide")) {
			Set<String> invisiblePlayers = plugin.getInvisiblePlayers();
			if(invisiblePlayers.contains(player.getName())) {
				invisiblePlayers.remove(player.getName());
				player.sendMessage(ChatColor.GREEN + "You will now appear on the staff list.");
				return true;
			} else {
				invisiblePlayers.add(player.getName());
				player.sendMessage(ChatColor.GREEN + "You will no longer appear on the staff list.");
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			return true;
		}
	}
}
