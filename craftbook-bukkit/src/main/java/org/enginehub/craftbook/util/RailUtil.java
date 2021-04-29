/*
 * CraftBook Copyright (C) EngineHub and Contributors <https://enginehub.org/>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package org.enginehub.craftbook.util;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.enginehub.craftbook.CraftBook;
import org.enginehub.craftbook.mechanic.MechanicTypes;

import java.util.ArrayList;
import java.util.List;

public final class RailUtil {

    private RailUtil() {
    }

    public static List<Chest> getNearbyChests(Block body) {

        int x = body.getX();
        int y = body.getY();
        int z = body.getZ();
        List<Chest> containers = new ArrayList<>();
        if (body.getWorld().getBlockAt(x, y, z).getType() == Material.CHEST || body.getWorld().getBlockAt(x, y, z).getType() == Material.TRAPPED_CHEST) {
            containers.add((Chest) body.getWorld().getBlockAt(x, y, z).getState());
        }
        if (body.getWorld().getBlockAt(x - 1, y, z).getType() == Material.CHEST || body.getWorld().getBlockAt(x - 1, y, z).getType() == Material.TRAPPED_CHEST) {
            containers.add((Chest) body.getWorld().getBlockAt(x - 1, y, z).getState());
            if (body.getWorld().getBlockAt(x - 2, y, z).getType() == Material.CHEST || body.getWorld().getBlockAt(x - 2, y, z).getType() == Material.TRAPPED_CHEST) {
                containers.add((Chest) body.getWorld().getBlockAt(x - 2, y, z).getState());
            }
        }
        if (body.getWorld().getBlockAt(x + 1, y, z).getType() == Material.CHEST || body.getWorld().getBlockAt(x + 1, y, z).getType() == Material.TRAPPED_CHEST) {
            containers.add((Chest) body.getWorld().getBlockAt(x + 1, y, z).getState());
            if (body.getWorld().getBlockAt(x + 2, y, z).getType() == Material.CHEST || body.getWorld().getBlockAt(x + 2, y, z).getType() == Material.TRAPPED_CHEST) {
                containers.add((Chest) body.getWorld().getBlockAt(x + 2, y, z).getState());
            }
        }
        if (body.getWorld().getBlockAt(x, y, z - 1).getType() == Material.CHEST || body.getWorld().getBlockAt(x, y, z - 1).getType() == Material.TRAPPED_CHEST) {
            containers.add((Chest) body.getWorld().getBlockAt(x, y, z - 1).getState());
            if (body.getWorld().getBlockAt(x, y, z - 2).getType() == Material.CHEST || body.getWorld().getBlockAt(x, y, z - 2).getType() == Material.TRAPPED_CHEST) {
                containers.add((Chest) body.getWorld().getBlockAt(x, y, z - 2).getState());
            }
        }
        if (body.getWorld().getBlockAt(x, y, z + 1).getType() == Material.CHEST || body.getWorld().getBlockAt(x, y, z + 1).getType() == Material.TRAPPED_CHEST) {
            containers.add((Chest) body.getWorld().getBlockAt(x, y, z + 1).getState());
            if (body.getWorld().getBlockAt(x, y, z + 2).getType() == Material.CHEST || body.getWorld().getBlockAt(x, y, z + 2).getType() == Material.TRAPPED_CHEST) {
                containers.add((Chest) body.getWorld().getBlockAt(x, y, z + 2).getState());
            }
        }

        return containers;
    }

    public static boolean isTrack(Material id) {
        if (CraftBook.getInstance().getPlatform().getMechanicManager().getMechanic(MechanicTypes.MINECART_MORE_RAILS)
            .map(moreRails -> moreRails.isValidRail(id))
            .orElse(false)) {
            return true;
        }

        return Tag.RAILS.isTagged(id);
    }
}
