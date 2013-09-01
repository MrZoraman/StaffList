package com.mrz.dyndns.server.StaffList3.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.CommandSystem.SimpleCommand;
import com.mrz.dyndns.server.StaffList3.StaffList3;

public class HiddenStatusCommand implements SimpleCommand
{
	public HiddenStatusCommand(StaffList3 plugin) {
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
		
		if(player.hasPermission("stafflist.status")) {
			boolean visible = plugin.playerGoesOnStaffList(player);
			if(visible) {
				player.sendMessage(ChatColor.GREEN + "You " + ChatColor.YELLOW + "will" + ChatColor.GREEN + " show up on the staff list");
				return true;
			} else {
				player.sendMessage(ChatColor.GREEN + "You " + ChatColor.YELLOW + "will not" + ChatColor.GREEN + " show up on the staff list");
				return true;
			}
		} else {
			player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			return true;
		}
	}

}
