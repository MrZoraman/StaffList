package com.mrz.dyndns.server.CommandSystem;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface SimpleCommand {
	public boolean Execute(String commandName, CommandSender sender, List<String> args);
}
