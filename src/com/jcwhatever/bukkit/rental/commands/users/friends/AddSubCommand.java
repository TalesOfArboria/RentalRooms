/* This file is part of RentalRooms for Bukkit, licensed under the MIT License (MIT).
 *
 * Copyright (c) JCThePants (www.jcwhatever.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


package com.jcwhatever.bukkit.rental.commands.users.friends;

import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.utils.player.PlayerUtils;
import com.jcwhatever.bukkit.rental.RentalRooms;
import com.jcwhatever.bukkit.rental.region.RentRegion;
import com.jcwhatever.bukkit.rental.region.RentRegionManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

@CommandInfo(
		parent="friends",
		command="add", 
		staticParams={"rentalName", "playerName"},
		description="Add a friend who has permission to interact with the specified rented region.",
		permissionDefault=PermissionDefault.TRUE)

public class AddSubCommand extends AbstractCommand {
	
	@Override
	public void execute(CommandSender sender, CommandArguments args) throws CommandException{

		CommandException.assertNotConsole(this, sender);
				
		Player p = (Player)sender;
		
		String regionName = args.getName("rentalName");
		String playerName = args.getName("playerName");
		
		RentRegionManager regionManager = RentalRooms.getInstance().getRegionManager();
		
		RentRegion region = regionManager.getRegion(regionName);
		if (region == null) {
			tellError(p, "Rental '{0}' was not found.", regionName);
			return; // finish
		}
		
		if (!region.hasTenant() || !region.getTenant().equals(p)) {
			tellError(p, "You are not the tenant of rental '{0}'.", regionName);
			return; // finish
		}
		
		Player friend = PlayerUtils.getPlayer(playerName);
		if (friend == null) {
			tellError(p, "'{0}' was not found or is not on the server.", playerName);
			return; // finish
		}
		
		region.getFriendManager().addFriend(friend);
						
		tellSuccess(p, "'{0}' has been added as a friend.", friend.getName());
	}
}