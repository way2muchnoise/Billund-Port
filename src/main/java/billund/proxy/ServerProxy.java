/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.proxy;

import net.minecraft.entity.player.EntityPlayer;

public class ServerProxy extends CommonProxy {

    // IProxy implementation

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public void initRenderingAndTextures() {

    }

    @Override
    public void openOrderFormGUI(EntityPlayer player) {
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();
    }
}
