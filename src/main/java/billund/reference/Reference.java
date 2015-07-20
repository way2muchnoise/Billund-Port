/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.reference;

public class Reference
{
    public static final String ID = "billund";
    public static final String NAME = "Billund";

    // Main version information that will be displayed in mod listing and for other purposes.
    public static final String V_MAJOR = "@MAJOR@";
    public static final String V_MINOR = "@MINOR@";
    public static final String V_REVIS = "@REVIS@";
    public static final String VERSION_FULL = V_MAJOR + "." + V_MINOR + "." + V_REVIS;

    public static final String CLIENT_PROXY_CLASS = "billund.proxy.ClientProxy";
    public static final String SEVER_PROXY_CLASS = "billund.proxy.ServerProxy";

    public static final String GUI_FACTORY_CLASS = "billund.gui.GuiFactory";
}
