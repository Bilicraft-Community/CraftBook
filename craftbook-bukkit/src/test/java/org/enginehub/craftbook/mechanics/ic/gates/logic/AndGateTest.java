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

package org.enginehub.craftbook.mechanics.ic.gates.logic;

import org.enginehub.craftbook.mechanics.ic.gates.logic.LogicICTests.LogicICTest;

import static org.junit.Assert.assertTrue;

public class AndGateTest implements LogicICTest {

    AndGate ic;

    public void testGetResult() {

        assertTrue(ic.getResult(2, 2));
        assertTrue(!ic.getResult(2, 1));
        assertTrue(!ic.getResult(2, 3));
        assertTrue(ic.getResult(3, 3));
        assertTrue(!ic.getResult(3, 0));
    }

    @Override
    public boolean testIC() {

        ic = new AndGate(null, null, null);

        try {
            testGetResult();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}