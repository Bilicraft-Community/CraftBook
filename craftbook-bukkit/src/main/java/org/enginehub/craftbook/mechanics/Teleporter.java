/*
 * CraftBook Copyright (C) me4502 <https://matthewmiller.dev/>
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

package org.enginehub.craftbook.mechanics;

import com.sk89q.util.yaml.YAMLProcessor;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.enginehub.craftbook.AbstractCraftBookMechanic;
import org.enginehub.craftbook.ChangedSign;
import org.enginehub.craftbook.CraftBook;
import org.enginehub.craftbook.CraftBookPlayer;
import org.enginehub.craftbook.bukkit.CraftBookPlugin;
import org.enginehub.craftbook.bukkit.util.CraftBookBukkitUtil;
import org.enginehub.craftbook.util.EventUtil;
import org.enginehub.craftbook.util.LocationUtil;
import org.enginehub.craftbook.util.ParsingUtil;
import org.enginehub.craftbook.util.ProtectionUtil;
import org.enginehub.craftbook.util.RegexUtil;
import org.enginehub.craftbook.util.SignUtil;
import org.enginehub.craftbook.util.events.SignClickEvent;

/**
 * Teleporter Mechanism. Based off Elevator
 *
 * @author sk89q
 * @author hash
 * @author Me4502
 */
public class Teleporter extends AbstractCraftBookMechanic {

    @EventHandler(priority = EventPriority.HIGH)
    public void onSignChange(SignChangeEvent event) {

        if (!EventUtil.passesFilter(event)) return;

        if (!event.getLine(1).equalsIgnoreCase("[Teleporter]")) return;

        CraftBookPlayer localPlayer = CraftBookPlugin.inst().wrapPlayer(event.getPlayer());

        if (!localPlayer.hasPermission("craftbook.mech.teleporter")) {
            if (CraftBook.getInstance().getPlatform().getConfiguration().showPermissionMessages)
                localPlayer.printError("mech.create-permission");
            SignUtil.cancelSignChange(event);
            return;
        }

        String[] pos = RegexUtil.COLON_PATTERN.split(ParsingUtil.parseLine(event.getLine(2), event.getPlayer()));
        if (pos.length <= 2) {
            localPlayer.printError("mech.teleport.invalidcoords");
            SignUtil.cancelSignChange(event);
            return;
        }

        localPlayer.print("mech.teleport.create");
        event.setLine(1, "[Teleporter]");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRightClick(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (SignUtil.isSign(event.getClickedBlock())) return;

        onCommonClick(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRightClick(SignClickEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        onCommonClick(event);
    }

    public void onCommonClick(PlayerInteractEvent event) {

        if (!EventUtil.passesFilter(event) || event.getHand() != EquipmentSlot.HAND)
            return;

        CraftBookPlayer localPlayer = CraftBookPlugin.inst().wrapPlayer(event.getPlayer());

        Block trigger = null;

        if (SignUtil.isSign(event.getClickedBlock())) {
            ChangedSign s = CraftBookBukkitUtil.toChangedSign(event.getClickedBlock());
            if (!s.getLine(1).equals("[Teleporter]")) return;
            String[] pos = RegexUtil.COLON_PATTERN.split(s.getLine(2));
            if (pos.length <= 2) {
                localPlayer.printError("mech.teleport.invalidcoords");
                return;
            }
            trigger = event.getClickedBlock();
        } else if (Tag.BUTTONS.isTagged(event.getClickedBlock().getType())) {
            Directional b = (Directional) event.getClickedBlock().getBlockData();
            if (b == null || b.getFacing() == null) return;
            Block sign = event.getClickedBlock().getRelative(b.getFacing().getOppositeFace(), 2);
            if (SignUtil.isSign(sign)) {
                ChangedSign s = CraftBookBukkitUtil.toChangedSign(sign);
                if (!s.getLine(1).equals("[Teleporter]")) return;
                String[] pos = RegexUtil.COLON_PATTERN.split(s.getLine(2));
                if (pos.length <= 2) {
                    localPlayer.printError("mech.teleport.invalidcoords");
                    return;
                }
                trigger = sign;
            }
        } else
            return;

        if (trigger == null) return;

        if (!localPlayer.hasPermission("craftbook.mech.teleporter.use")) {
            if (CraftBook.getInstance().getPlatform().getConfiguration().showPermissionMessages)
                localPlayer.printError("mech.use-permission");
            return;
        }

        if (!ProtectionUtil.canUse(event.getPlayer(), event.getClickedBlock().getLocation(), event.getBlockFace(), event.getAction())) {
            if (CraftBook.getInstance().getPlatform().getConfiguration().showPermissionMessages)
                localPlayer.printError("area.use-permissions");
            return;
        }

        makeItSo(localPlayer, trigger);

        event.setCancelled(true);
    }

    private void makeItSo(CraftBookPlayer player, Block trigger) {
        // start with the block shifted vertically from the player
        // to the destination sign's height (plus one).
        // check if this looks at all like something we're interested in first

        double toX = 0;
        double toY = 0;
        double toZ = 0;

        if (SignUtil.isSign(trigger)) {
            ChangedSign s = CraftBookBukkitUtil.toChangedSign(trigger);
            String[] pos = RegexUtil.COLON_PATTERN.split(s.getLine(2));
            if (pos.length > 2) {
                try {
                    toX = Double.parseDouble(pos[0]);
                    toY = Double.parseDouble(pos[1]);
                    toZ = Double.parseDouble(pos[2]);
                } catch (Exception e) {
                    player.printError("mech.teleport.arriveonly");
                    return;
                }
            } else {
                player.printError("mech.teleport.arriveonly");
                return;
            }
        }

        if (requireSign) {
            Block location = trigger.getWorld().getBlockAt((int) toX, (int) toY, (int) toZ);
            if (SignUtil.isSign(location)) {
                if (!checkTeleportSign(player, location)) {
                    return;
                }
            } else if (Tag.BUTTONS.isTagged(location.getType())) {
                Directional b = (Directional) location.getBlockData();
                Block sign = location.getRelative(b.getFacing(), 2);
                if (!checkTeleportSign(player, sign)) {
                    return;
                }
            } else {
                player.printError("mech.teleport.sign");
                return;
            }
        }

        Block floor = trigger.getWorld().getBlockAt((int) Math.floor(toX), (int) (Math.floor(toY) + 1),
            (int) Math.floor(toZ));
        // well, unless that's already a ceiling.
        if (floor.getType().isSolid())
            floor = floor.getRelative(BlockFace.DOWN);

        // now iterate down until we find enough open space to stand in
        // or until we're 5 blocks away, which we consider too far.
        int foundFree = 0;
        for (int i = 0; i < 5; i++) {
            if (!floor.getType().isSolid() || SignUtil.isSign(floor))
                foundFree++;
            else
                break;
            if (floor.getY() == 0x0) break;
            floor = floor.getRelative(BlockFace.DOWN);
        }
        if (foundFree < 2) {
            player.printError("mech.teleport.obstruct");
            return;
        }

        // Teleport!
        Location subspaceRift = player.getLocation();
        subspaceRift = subspaceRift.setX(floor.getX() + 0.5);
        subspaceRift = subspaceRift.setY(floor.getY() + 1.0);
        subspaceRift = subspaceRift.setZ(floor.getZ() + 0.5);

        if (maxRange > 0) {
            if (subspaceRift.toVector().distanceSq(player.getLocation().toVector()) > maxRange * maxRange) {
                player.print("mech.teleport.range");
                return;
            }
        }

        if (player.isInsideVehicle()) {
            org.bukkit.Location newLocation = BukkitAdapter.adapt(subspaceRift);
            Entity teleportedVehicle = LocationUtil.ejectAndTeleportPlayerVehicle(player, newLocation);

            player.teleport(subspaceRift);

            LocationUtil.addVehiclePassengerDelayed(teleportedVehicle, player);
        } else {
            player.teleport(subspaceRift);
        }

        player.print("mech.teleport.alert");
    }

    private static boolean checkTeleportSign(CraftBookPlayer player, Block sign) {
        if (!SignUtil.isSign(sign)) {
            player.printError("mech.teleport.sign");
            return false;
        }

        ChangedSign s = CraftBookBukkitUtil.toChangedSign(sign);
        if (!s.getLine(1).equals("[Teleporter]")) {
            player.printError("mech.teleport.sign");
            return false;
        }

        return true;
    }

    private boolean requireSign;
    private int maxRange;

    @Override
    public void loadFromConfiguration(YAMLProcessor config) {

        config.setComment("require-sign", "Require a sign to be at the destination of the teleportation.");
        requireSign = config.getBoolean("require-sign", false);

        config.setComment("max-range", "The maximum distance between the start and end of a teleporter. Set to 0 for infinite.");
        maxRange = config.getInt("max-range", 0);
    }
}