package com.mrz.dyndns.server.CommandSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSystem implements CommandExecutor
{
	public CommandSystem(JavaPlugin plugin) {
		commandMap = new HashMap<String, SimpleCommand>();
		registeredCommandStrings = new HashSet<String>();
		this.plugin = plugin;
	}
	
	private Map<String, SimpleCommand> commandMap;
	private JavaPlugin plugin;
	private Set<String> registeredCommandStrings;
	
	public void registerCommand(String command, SimpleCommand executor) {
		String[] parts = command.split(" ");
		if(parts[0] != null) {
			if(!registeredCommandStrings.contains(parts[0])) {
				try {
					plugin.getCommand(parts[0]).setExecutor(this);
				} catch (NullPointerException e) {
					System.out.println("[ERROR] You forgot to register the command '" + parts[0] + "' in the plugin.yml!");
				}
			}

			commandMap.put(command, executor);
			//System.out.println("Command registerd: \'" + command + "\'");
		} else {
			plugin.getLogger().log(Level.SEVERE, "Command cannot be null!");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) 
	{
		String input = "";

		List<String> rawInput = Arrays.asList(args);
		List<String> realArgs = new ArrayList<String>();
		List<String> inputArgs = new ArrayList<String>();
		
		for(int ii = 0; ii < rawInput.size(); ii++)
		{
			if(!rawInput.get(ii).isEmpty())
			{
				inputArgs.add(rawInput.get(ii));
			}
		}
		
		for(int ii = inputArgs.size(); ii >= 0; ii--)
		{
			int jj;
			input = cmd.getName();
			for(jj = 0; jj < ii; jj++)
			{
				input = input + " " + inputArgs.get(jj);
			}
			
			if(commandMap.containsKey(input))
			{
				for(int kk = jj; kk < inputArgs.size(); kk++)
				{
					realArgs.add(inputArgs.get(kk));
				}
				break;
			}
		}
		
		if(!commandMap.containsKey(input))
		{
			//debug("commandMap does not contain key: " + input);
			sender.sendMessage("Unknown command. Type \"help\" for help.");
			return true;
		}
		else
		{
			return commandMap.get(input).Execute(cmd.getName(), sender, realArgs);
		}
	}
	
	public void close() {
		commandMap.clear();
		registeredCommandStrings.clear();
	}
}




