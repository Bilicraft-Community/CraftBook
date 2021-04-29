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

package org.enginehub.craftbook.mechanics.ic;

import com.sk89q.worldedit.util.Location;
import org.enginehub.craftbook.ChangedSign;

/**
 * IC families handle the logic required in figuring out where pins are located and reading them.
 * One ICFamily
 * instance is created and attached to the
 * IC manager.
 *
 * @author sk89q
 */
public interface ICFamily {

    /**
     * Return a {@link ChipState} that provides an interface to access the I/O pins.
     *
     * @param source
     * @param sign
     * @return
     */
    ChipState detect(Location source, ChangedSign sign);

    /**
     * Return a {@link ChipState} that provides an interface to access the I/O pins.
     *
     * @param source
     * @param sign
     * @return
     */
    ChipState detectSelfTriggered(Location source, ChangedSign sign);

    /**
     * Gets the sign modifier of the family. In case of the base ic thats MC.
     *
     * @return family modifier
     */
    String getSuffix();

    /**
     * Gets the name of this IC Family.
     *
     * @return the name
     */
    String getName();
}
