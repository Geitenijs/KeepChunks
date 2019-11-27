# KeepRails
## An minor extension to the KeepChunks plugin, found at:
[SpigotMC Resource Page](https://www.spigotmc.org/resources/23307/)

KeepChunks allows you to select chunks to keep them loaded in the Minecraft server memory, even when there are no players around. This can be useful for large redstone circuits, minecart railroads, commandblock systems and even for reducing teleport lag.

KeepRails introduces the KeepRails command, and is a small add on to Geitenjis' incredible work. This command takes a user provided location of a minecart rail system in the game, uses a primitive pathfinder  to iteratively search for any adjacent rails within a customizable radius, and then forceloads the necessary chunks to allow the minecart system to run at all times; This includes when a player is not in the area or there are no players on the server!
