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


package com.jcwhatever.rentalrooms.commands.users;

import com.jcwhatever.rentalrooms.BillCollector;
import com.jcwhatever.rentalrooms.Lang;
import com.jcwhatever.rentalrooms.Msg;
import com.jcwhatever.rentalrooms.RentalRooms;
import com.jcwhatever.rentalrooms.Tenant;
import com.jcwhatever.rentalrooms.region.RentRegion;
import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.messaging.ChatPaginator;
import com.jcwhatever.nucleus.utils.language.Localizable;
import com.jcwhatever.nucleus.utils.text.TextUtils.FormatTemplate;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Collection;

@CommandInfo(
        command="list",
        staticParams={"page=1"},
        description="Get a list of your rented regions.",
        paramDescriptions = {
                "page= {PAGE}"
        },
        permissionDefault=PermissionDefault.TRUE)

public class ListCommand extends AbstractCommand {

    @Localizable static final String _PAGINATOR_TITLE =
            "My Rents";

    @Localizable static final String _DUE_BY =
            "{0: rent amount} due by {1: due date}";

    @Localizable static final String _LABEL_NONE =
            "<none>";

    @Override
    public void execute(CommandSender sender, CommandArguments args) throws CommandException {

        CommandException.checkNotConsole(this, sender);

        Player p = (Player)sender;

        int page = args.getInteger("page");

        BillCollector collector = RentalRooms.getBillCollector();
        Tenant tenant = Tenant.get(p);

        ChatPaginator pagin = Msg.getPaginator(Lang.get(_PAGINATOR_TITLE));

        if (tenant == null) {
            pagin.addFormatted(FormatTemplate.RAW, Lang.get(_LABEL_NONE));
        }
        else {
            Collection<RentRegion> regions = tenant.getRentRegions();
            if (regions.size() == 0) {
                pagin.addFormatted(FormatTemplate.RAW, Lang.get(_LABEL_NONE));
            }
            else {

                for (RentRegion region : regions) {
                    pagin.add(region.getName(),
                            Lang.get(_DUE_BY, collector.formatRentPrice(region),
                                    region.getFormattedExpiration()));
                }
            }
        }

        pagin.show(sender, page, FormatTemplate.CONSTANT_DEFINITION);
    }
}