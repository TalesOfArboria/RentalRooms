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


package com.jcwhatever.bukkit.rental.commands.admin;

import org.bukkit.command.CommandSender;

import com.jcwhatever.bukkit.generic.commands.AbstractCommand;
import com.jcwhatever.bukkit.generic.commands.CommandInfo;
import com.jcwhatever.bukkit.generic.commands.arguments.CommandArguments;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidValueException;
import com.jcwhatever.bukkit.rental.RentalRooms;
import com.jcwhatever.bukkit.rental.region.RentRegion;
import com.jcwhatever.bukkit.rental.region.RentRegionManager;


@CommandInfo(
        command="del", 
        staticParams="rentalName",
        usage="/rent del <rentalName>",
        description="Remove a rental room.")

public class DelCommand extends AbstractCommand {
    
    @Override
    public void execute(CommandSender sender, CommandArguments args) throws InvalidValueException {
        
        String rentalName = args.getName("rentalName");
        
        RentRegionManager regionManager = RentalRooms.getInstance().getRegionManager();
        
        RentRegion region = regionManager.getRegion(rentalName);
        if (region == null) {
            tellError(sender, "A rental region named '{0}' was not found.", rentalName);
            return; // finish
        }
        
        if (!regionManager.removeRegion(rentalName)) {
            tellError(sender, "Failed to remove rental region.");
        }
        
        tellSuccess(sender, "Rental region '{0}' removed.", rentalName);
    }
    

}
