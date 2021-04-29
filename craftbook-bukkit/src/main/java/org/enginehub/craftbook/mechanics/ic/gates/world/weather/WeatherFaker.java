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

package org.enginehub.craftbook.mechanics.ic.gates.world.weather;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.enginehub.craftbook.ChangedSign;
import org.enginehub.craftbook.mechanics.ic.AbstractICFactory;
import org.enginehub.craftbook.mechanics.ic.AbstractSelfTriggeredIC;
import org.enginehub.craftbook.mechanics.ic.ChipState;
import org.enginehub.craftbook.mechanics.ic.IC;
import org.enginehub.craftbook.mechanics.ic.ICFactory;
import org.enginehub.craftbook.mechanics.ic.RestrictedIC;
import org.enginehub.craftbook.util.SearchArea;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Me4502
 */
public class WeatherFaker extends AbstractSelfTriggeredIC {

    public WeatherFaker(Server server, ChangedSign sign, ICFactory factory) {

        super(server, sign, factory);
    }

    @Override
    public String getTitle() {

        return "Weather Faker";
    }

    @Override
    public String getSignTitle() {

        return "WEATHER FAKER";
    }

    public static class Factory extends AbstractICFactory implements RestrictedIC {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(ChangedSign sign) {

            return new WeatherFaker(getServer(), sign, this);
        }

        @Override
        public String getShortDescription() {

            return "Fakes a players weather in radius.";
        }

        @Override
        public String[] getLineHelp() {

            return new String[] { "radius", "rain (If it should rain)" };
        }
    }

    @Override
    public boolean isAlwaysST() {

        return true;
    }

    @Override
    public void trigger(ChipState chip) {

        if (chip.getInput(0)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (area.isWithinArea(p.getLocation())) {
                    p.setPlayerWeather(rain ? WeatherType.DOWNFALL : WeatherType.CLEAR);
                    players.add(p.getName());
                } else if (players.contains(p.getName())) {
                    players.remove(p.getName());
                    p.resetPlayerWeather();
                }
            }
        } else {
            for (String p : players) {
                Player pp = Bukkit.getPlayerExact(p);
                if (pp == null) continue;
                pp.resetPlayerWeather();
            }
            players.clear();
        }
    }

    private Set<String> players;

    SearchArea area;
    boolean rain;

    @Override
    public void load() {

        players = new HashSet<>();

        area = SearchArea.createArea(getLocation().getBlock(), getLine(2));
        rain = getLine(3).equals("rain");
    }
}