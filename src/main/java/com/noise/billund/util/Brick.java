/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.noise.billund.util;

import com.noise.billund.reference.MCColour;

public class Brick {
    public MCColour Colour;
    public int XOrigin;
    public int YOrigin;
    public int ZOrigin;
    public int Width;
    public int Height;
    public int Depth;

    public Brick(MCColour colour, int xOrigin, int yOrigin, int zOrigin, int width, int height, int depth) {
        Colour = colour;
        XOrigin = xOrigin;
        YOrigin = yOrigin;
        ZOrigin = zOrigin;
        Width = width;
        Height = height;
        Depth = depth;
    }
}
