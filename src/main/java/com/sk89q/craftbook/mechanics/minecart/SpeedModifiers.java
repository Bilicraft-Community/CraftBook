package com.sk89q.craftbook.mechanics.minecart;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.util.Vector;

import com.sk89q.craftbook.AbstractCraftBookMechanic;
import com.sk89q.craftbook.util.EventUtil;
import com.sk89q.util.yaml.YAMLProcessor;

public class SpeedModifiers extends AbstractCraftBookMechanic {



    @EventHandler(priority = EventPriority.HIGH)
    public void onVehicleCreate(VehicleCreateEvent event) {

        if(!EventUtil.passesFilter(event)) return;

        if (!(event.getVehicle() instanceof Minecart)) return;

        if(onlyRegularMinecart && event.getVehicle().getType() == EntityType.MINECART) return;

        if (offRail > 0)
            ((Minecart) event.getVehicle()).setDerailedVelocityMod(new Vector(offRail, offRail, offRail));
        if(maxSpeed != 1)
            ((Minecart) event.getVehicle()).setMaxSpeed(((Minecart) event.getVehicle()).getMaxSpeed() * maxSpeed);
    }

    private double maxSpeed;
    private double offRail;
    private boolean onlyRegularMinecart;

    @Override
    public void loadConfiguration (YAMLProcessor config, String path) {

        config.setComment(path + "max-speed", "Sets the max speed modifier of carts. Normal max speed speed is 0.4D");
        maxSpeed = config.getDouble(path + "max-speed", 1);

        config.setComment(path + "off-rail-speed", "Sets the off-rail speed modifier of carts. 0 is none.");
        offRail = config.getDouble(path + "off-rail-speed", 0);

        config.setComment(path + "only-regular-minecart", "Sets only apply regular minecart speed.");
        onlyRegularMinecart = config.getBoolean(path + "only-regular-minecart", true);
    }
}