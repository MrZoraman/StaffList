package com.mrz.dyndns.server.StaffList3.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mrz.dyndns.server.CommandSystem.SimpleCommand;
import com.mrz.dyndns.server.StaffList3.StaffList3;

public class ViewStaffListCommand implements SimpleCommand
{
	public ViewStaffListCommand(StaffList3 plugin) {
		this.plugin = plugin;
	}
	
	private final StaffList3 plugin;
	
	@Override
	public boolean Execute(String commandName, CommandSender sender, List<String> args)
	{
		if(sender.hasPermission("stafflist.view")) {
			Player[] onlinePlayers = Bukkit.getOnlinePlayers();
			
			int amountOfOnlinePlayers = onlinePlayers.length;
			
			List<Player> staffPlayers = new ArrayList<Player>();
			for(Player p : onlinePlayers) {
				if(plugin.playerGoesOnStaffList(p)) {
					staffPlayers.add(p);
				} else if (p.hasPermission("stafflist.show") == true) {
					if(amountOfOnlinePlayers > 0) {
						amountOfOnlinePlayers--;
					}
				}
			}
			
			if(plugin.isDisplayAmountOfPlayers()) {
				sender.sendMessage(ChatColor.BLUE + "There are " + ChatColor.AQUA + "(" + amountOfOnlinePlayers + 
						"/" + Bukkit.getServer().getMaxPlayers() + ") " + ChatColor.BLUE + "Players Online.");
			}
			
			String playerListMessage = ChatColor.RED + "[Staff Players] " + ChatColor.GRAY;
			
			if(staffPlayers.size() == 0) {
				sender.sendMessage(playerListMessage + ChatColor.YELLOW + "No staff players online.");
				return true;
			} else {
				for(Player p : staffPlayers) {
					String prefix = plugin.getPrefix(p);
					
					if(plugin.isPutBracketsAroundPrefix()) {
						prefix = ChatColor.GRAY + "[" + ChatColor.WHITE + prefix + ChatColor.GRAY + "]";
					} else {
						prefix = prefix + ChatColor.GRAY;
					}
					playerListMessage = playerListMessage + prefix + p.getDisplayName() + ", ";
				}
				playerListMessage = playerListMessage.substring(0, playerListMessage.length() - 2);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', playerListMessage));
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			return true;
		}
	}

}
