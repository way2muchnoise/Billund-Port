/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.client.gui;

import billund.network.MessageHandler;
import billund.network.message.MessageBillund;
import billund.reference.Resources;
import billund.util.BillundSet;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class GuiOrderForm extends GuiScreen
{
    private static final int xSize = 192;
    private static final int ySize = 185;

    private static final int NUM_SETS = 5;

    private EntityPlayer m_player;
    private boolean[] m_orders;
    private boolean m_ordered;

    public GuiOrderForm(EntityPlayer player)
    {
        m_player = player;
        m_orders = new boolean[NUM_SETS];
        for (int i = 0; i < NUM_SETS; ++i)
        {
            m_orders[i] = false;
        }
        m_ordered = false;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void updateScreen()
    {
        // TODO: If the item got lost, close GUI
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char c, int k)
    {
        super.keyTyped(c, k);
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);

        int startY = (height - ySize) / 2;
        int startX = (width - xSize) / 2;
        if (button == 0)
        {
            int localX = x - startX;
            int localY = y - startY;

            // Test tickboxes
            if (localX >= 160 && localX < 177 && localY >= 33)
            {
                int tickBoxIndex = (localY - 33) / 23;
                int tickBoxLocalY = (localY - 33) % 23;
                if (tickBoxIndex >= 0 && tickBoxIndex < NUM_SETS && tickBoxLocalY < 17)
                    if (!m_ordered)
                        for (int i = 0; i < NUM_SETS; ++i)
                            m_orders[i] = i == tickBoxIndex && !m_orders[i];
            }

            // Test order button
            if (localX >= 102 && localX < 177 && localY >= 149 && localY < 170)
                if (canPlayerOrder()) order();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f)
    {
        // Draw background
        drawDefaultBackground();

        // Draw the form
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI.ORDER_FORM);

        int startY = (height - ySize) / 2;
        int startX = (width - xSize) / 2;
        drawTexturedModalRect(startX, startY, 0, 0, xSize, ySize);
        drawTexturedModalRect(startX, startY - 5, 0, ySize, xSize, 5);

        // Draw the ticks
        for (int i = 0; i < NUM_SETS; ++i)
            if (m_orders[i])
                drawTexturedModalRect(startX + 160, startY + 31 + i * 23, xSize, 0, 19, 19);

        // Draw the text
        fontRendererObj.drawString("Billund Order Form", startX + 8, startY + 10, 0x4c5156);

        String currency = getPlayerBalance() + " / " + getOrderCost();
        int currencyColour = canPlayerAffordOrder() ? 0x4c5156 : 0xae1e22;
        fontRendererObj.drawString(currency, startX + xSize - 25 - fontRendererObj.getStringWidth(currency), startY + 10, currencyColour);

        for (int i = 0; i < NUM_SETS; ++i)
            fontRendererObj.drawString(getSetName(i), startX + 16, startY + 38 + i * 23, 0x4c5156);

        String order = m_ordered ? "Placed" : "Place Order";
        int colour = canPlayerOrder() ? 0x4c5156 : 0xb3a8a7;
        fontRendererObj.drawString(order, startX + 102 + (75 - fontRendererObj.getStringWidth(order)) / 2, startY + 156, colour);

        super.drawScreen(mouseX, mouseY, f);
    }

    // Private stuff

    private boolean canPlayerOrder()
    {
        if (!m_ordered)
        {
            int order = getOrderCost();
            if (order > 0)
            {
                int balance = getPlayerBalance();
                return balance >= order;
            }
        }
        return false;
    }

    private boolean canPlayerAffordOrder()
    {
        int order = getOrderCost();
        int balance = getPlayerBalance();
        return balance >= order;
    }

    private void order()
    {
        if (!m_ordered)
        {
            // Send our orders to the server
            for (int i = 0; i < NUM_SETS; ++i)
            {
                if (m_orders[i])
                {
                    MessageBillund packet = new MessageBillund();
                    packet.packetType = MessageBillund.OrderSet;
                    packet.dataInt = new int[]{i};
                    MessageHandler.INSTANCE.sendToServer(packet);
                }
            }

            // Ensure we don't order again
            m_ordered = true;
        }
    }

    private int getPlayerBalance()
    {
        int total = 0;
        for (int i = 0; i < m_player.inventory.getSizeInventory(); ++i)
        {
            ItemStack stack = m_player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == Items.emerald)
                total += stack.stackSize;
        }
        return total;
    }

    private int getOrderCost()
    {
        int total = 0;
        for (int i = 0; i < NUM_SETS; ++i)
            if (m_orders[i])
                total += getSetCost(i);
        return total;
    }

    private String getSetName(int i)
    {
        return BillundSet.get(i).getDescription();
    }

    private int getSetCost(int i)
    {
        return BillundSet.get(i).getCost();
    }
}
