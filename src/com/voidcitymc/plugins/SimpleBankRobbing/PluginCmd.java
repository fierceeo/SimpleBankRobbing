package com.voidcitymc.plugins.SimpleBankRobbing;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PluginCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (sender.isOp()) Main.getInstance().reloadConfig();
		return true;
	}

}
	