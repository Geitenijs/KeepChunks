package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Main;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Command_Keeprail implements CommandExecutor, TabCompleter {

	public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
		if (args.length == 2) {    //Command is "current", so start seeking from player location.
			if (s instanceof Player) {

				Player p = (Player) s;
				Location start = p.getLocation();
				Material m = start.getBlock().getType();
				boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);

				HashSet<Location> explored = new HashSet<>();    //Tracks all rails seen to prevent backtracking.
				Queue<Location> agenda = new LinkedList<>();    //All rails found, but not explored

				if (!isRail) {
					Utilities.msg(s, "§4Please try again while standing on a rail.§4");
					return true;
				} else {
					Utilities.msg(s, "Looking for rails...");
					agenda.add(start);
					int totalRails = 0;
					while (!agenda.isEmpty()) {        //Might be issue if start is in the spawn chunks...
						Location cur = agenda.peek();
						agenda.remove();
						getAdjacent(cur, explored, agenda);
						explored.add(cur);
						++totalRails;
					}
					Utilities.msg(s, "§2Found " + totalRails + " rails!§2");
				}
			} else if (args.length == 6 && args[2].equalsIgnoreCase("coords")) {
				if (s instanceof Player) {
					try {
						final int x = Integer.parseInt(args[2]);
						final int y = Integer.parseInt(args[3]);
						final int z = Integer.parseInt(args[4]);
						final String world = args[5];
						final String chunk = x + "#" + z + "#" + world;
						if (Utilities.railchunks.contains(chunk)) {
							Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world + "'&c is already marked.");
						} else {
							Utilities.railchunks.add(chunk);
							Utilities.data.set("railchunks", new ArrayList<>(Utilities.railchunks));
							Utilities.saveDataFile();
							Utilities.reloadDataFile();
							if (Utilities.config.getBoolean("chunkload.dynamic")) {
								if (Utilities.config.getBoolean("general.debug")) {
									Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
								}
								try {
									Main.plugin.getServer().getWorld(world).loadChunk(x, z);
									if (Main.version.contains("v1_14_R1")) {
										try {
											Main.plugin.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
										} catch (NoSuchMethodError ignored) {
										}
									}
								} catch (NullPointerException ex) {
									if (Utilities.config.getBoolean("general.debug")) {
										Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "The world '" + world + "' could not be found. Has it been removed?");
									}
								}
							}
							Utilities.msg(s, "&fMarked chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
						}
					} catch (NumberFormatException ex) {
						Utilities.msg(s, Strings.UNUSABLE);
					}
				} else {
					Utilities.msg(s, Strings.ONLYPLAYER);
				}
			} else {
				Utilities.msg(s, Strings.KEEPRAILUSAGE);
			}
		}
		return true;
	}
		public void getN(Location pos, HashSet<Location> history, Queue<Location> todo) {
			for (int i = -1; i < 2; ++i) {
				Location candidate = new Location(pos.getWorld(), pos.getBlockX(), pos.getBlockY() + i, pos.getBlockZ() - 1);
				Material m = candidate.getBlock().getType();	//Grabs Material type
				boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);

				if (isRail && !history.contains(candidate)) {	//If the candidate block is a rail and has not been visited before
					if (!candidate.getChunk().isForceLoaded()) {
						updateData(candidate.getChunk()); //Load the chunk and the 8 chunk perimeter, and update data files.
						Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + pos.getX() + "," + pos.getZ() + ") in world '" + pos.getWorld() + "'.");
					}
					todo.add(candidate);	//Add to the the agenda of rails that still need to be explored.
				}
			}
		}
		public void getS(Location pos, HashSet<Location> history, Queue<Location> todo) {
			for(int i = -1; i < 2; ++i){
				Location candidate = new Location(pos.getWorld(),pos.getBlockX(),pos.getBlockY()+i,pos.getBlockZ()+1);
				Material m = candidate.getBlock().getType();	//Grabs Material type
				boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);

				if (isRail && !history.contains(candidate)) {
					if (!candidate.getChunk().isForceLoaded()) {
						updateData(candidate.getChunk()); //Load the chunks and the 8 chunk perimeter, and update data files.
						Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + pos.getX() + "," + pos.getZ() + ") in world '" + pos.getWorld() + "'.");
					}
					todo.add(candidate);
				}
			}
		}
		public void getE(Location pos, HashSet<Location> history, Queue<Location> todo) {
			for(int i = -1; i < 2; ++i){
				Location candidate = new Location(pos.getWorld(),pos.getBlockX()+1,pos.getBlockY()+i,pos.getBlockZ());
				Material m = candidate.getBlock().getType();	//Grabs Material type
				boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);

				if (isRail && !history.contains(candidate)) {
					if (!candidate.getChunk().isForceLoaded()) {
						updateData(candidate.getChunk()); //Load the chunks and the 8 chunk perimeter, and update data files.
						Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + pos.getX() + "," + pos.getZ() + ") in world '" + pos.getWorld() + "'.");
					}
					todo.add(candidate);
				}
			}
		}
		public void getW(Location pos, HashSet<Location> history, Queue<Location> todo) {
			for(int i = -1; i < 2; ++i){
				Location candidate = new Location(pos.getWorld(),pos.getBlockX()-1,pos.getBlockY()+i,pos.getBlockZ());
				Material m = candidate.getBlock().getType();	//Grabs Material type
				boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);

				if (isRail && !history.contains(candidate)) {
					if (!candidate.getChunk().isForceLoaded()) {
						updateData(candidate.getChunk()); //Load the chunks and the 8 chunk perimeter, and update data files.
						Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + pos.getX() + "," + pos.getZ() + ") in world '" + pos.getWorld() + "'.");
					}
					todo.add(candidate);
				}
			}
		}
		public void getAdjacent(Location pos, HashSet<Location> history, Queue<Location> todo){
			getN(pos,history,todo);
			getS(pos,history,todo);
			getE(pos,history,todo);
			getW(pos,history,todo);
		}
		public void updateData(Chunk currentChunk) {
			final String world = currentChunk.getWorld().getName();

			for(int i = -1; i < 2; ++i) {
				final int x = currentChunk.getX()+i;

				for (int j = -1; j < 2; ++j) {
					final int z = currentChunk.getZ() + j;
					final String chunk = x + "#" + z + "#" + world;

					Utilities.railchunks.add(chunk);
					Utilities.data.set("railchunks", new ArrayList<>(Utilities.railchunks));
					Utilities.saveDataFile();
					Utilities.reloadDataFile();

					if (Utilities.config.getBoolean("chunkload.dynamic")) {
						if (Utilities.config.getBoolean("general.debug")) {
							Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
						}
						try {
							Main.plugin.getServer().getWorld(world).loadChunk(x, z);
							if (Main.version.contains("v1_14_R1")) {
								try {
									Main.plugin.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
								} catch (NoSuchMethodError ignored) {
								}
							}
						} catch (NullPointerException ex) {
							if (Utilities.config.getBoolean("general.debug")) {
								Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "The world '" + world + "' could not be found. Has it been removed?");
							}
						}
					}
				}
			}
		}
		public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
			ArrayList<String> tabs = new ArrayList<>();
			String[] newArgs = CommandWrapper.getArgs(args);
			if (newArgs.length == 1) {
				tabs.add("current");
				tabs.add("coords");
			}
			if (args[1].equals("coords")) {
				if (s instanceof Player) {
					Player player = (Player) s;
					Location loc = player.getLocation();
					if (newArgs.length == 2) {
						tabs.add(String.valueOf(loc.getChunk().getX()));
					}
					if (newArgs.length == 3) {
						tabs.add(String.valueOf(loc.getY()));
					}
					if (newArgs.length == 4) {
						tabs.add(String.valueOf(loc.getChunk().getZ()));
					}
					if (newArgs.length == 5) {
						tabs.add(loc.getWorld().getName());
					}
				} else {
					if (newArgs.length == 2) {
						tabs.add("<0>");
					}
					if (newArgs.length == 3) {
						tabs.add("<0>");
					}
					if (newArgs.length == 4) {
						tabs.add("<0>");
					}
				}
			}
			if (args[1].equals("current")) {
				tabs.clear();
			}
			return CommandWrapper.filterTabs(tabs, args);
		}
	}
