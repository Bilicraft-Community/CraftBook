package com.sk89q.craftbook.sponge.mechanics.minecart;

import com.sk89q.craftbook.sponge.mechanics.Mechanic;

@Mechanic(name = "MinecartEmptyDecay")
public class EmptyDecay {

	/*public void onVehicleExit(EntityDismountEvent event) {

		event.getGame().getScheduler().runTaskAfter(CraftBookPlugin.instance, new MinecartDecay(event.getDismounted(), 40L));
	}

	public static class MinecartDecay implements Runnable {

		Minecart cart;

		public MinecartDecay(Minecart cart) {

			this.cart = cart;
		}

		@Override
		public void run() {

			if(cart.getRider() == null) {
				cart.remove();
			}
		}
	}*/
}