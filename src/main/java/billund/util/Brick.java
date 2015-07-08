/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.util;

import billund.reference.Colour;

public class Brick
{
    public Colour colour;
    public int xOrigin;
    public int yOrigin;
    public int zOrigin;
    public int width;
    public int height;
    public int depth;

    public Brick(Colour colour, int xOrigin, int yOrigin, int zOrigin, int width, int height, int depth)
    {
        this.colour = colour;
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.zOrigin = zOrigin;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }
}
