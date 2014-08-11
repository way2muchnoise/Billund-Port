/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.noise.billund.tileentity;

import com.noise.billund.init.ModBlocks;
import com.noise.billund.network.MessageHandler;
import com.noise.billund.network.message.MessageTileEntityBillund;
import com.noise.billund.util.Brick;
import com.noise.billund.util.Stud;
import com.noise.billund.util.StudColour;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileEntityBillund extends TileEntity
{
	private Stud[] m_studs;

    public void setStuds(Stud[] studs) {
        this.m_studs = studs;
    }

    public TileEntityBillund()
    {
    	super();
    	m_studs = new Stud[ Stud.STUDS_PER_BLOCK ];
    }
    
    public Stud getStudLocal( int x, int y, int z )
    {
    	if( x >= 0 && x < Stud.STUDS_PER_ROW &&
    	    y >= 0 && y < Stud.STUDS_PER_COLUMN &&
    	    z >= 0 && z < Stud.STUDS_PER_ROW )
    	{
    		return m_studs[ x + (z * Stud.STUDS_PER_ROW) + (y * Stud.STUDS_PER_LAYER) ];
    	}
    	return null;
    }
    
    public void setStudLocal( int x, int y, int z, Stud stud )
    {
    	if( x >= 0 && x < Stud.STUDS_PER_ROW &&
    	    y >= 0 && y < Stud.STUDS_PER_COLUMN &&
    	    z >= 0 && z < Stud.STUDS_PER_ROW )
    	{
    		m_studs[ x + (z * Stud.STUDS_PER_ROW) + (y * Stud.STUDS_PER_LAYER) ] = stud;
    	}
    }
    
    public boolean isEmpty()
    {
    	for( int i=0; i<Stud.STUDS_PER_BLOCK; ++i )
    	{
    		if( m_studs[i] != null )
    		{
    			return false;
    		}
    	}
    	return true; 
    }
    
    public void cullOrphans()
    {
    	boolean changed = false;
    	for( int i=0; i<Stud.STUDS_PER_BLOCK; ++i )
    	{
    		Stud stud = m_studs[i];
    		if( stud != null )
    		{
    			Stud origin = Stud.getStud( worldObj, stud.XOrigin, stud.YOrigin, stud.ZOrigin );
    			if( origin == null )
    			{
    				m_studs[i] = null;
    				changed = true;
    			} 
    		}
    	}
    	if( changed )
    	{
			worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    	}
    }

    @Override
    public void updateEntity()
    {
    	super.updateEntity();
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
        writeToNBT(new NBTTagCompound());
        return MessageHandler.INSTANCE.getPacketFrom(new MessageTileEntityBillund(this.xCoord, this.yCoord, this.zCoord, this.m_studs));
    }

    @Override
    public void writeToNBT( NBTTagCompound nbttagcompound )
    {
        super.writeToNBT( nbttagcompound );

		for( int i=0; i<Stud.STUDS_PER_BLOCK; ++i )
		{
			Stud stud = m_studs[i];
			if( stud != null )
			{
		        NBTTagCompound studTag = new NBTTagCompound();
		        studTag.setInteger( "c", stud.Colour );
		        studTag.setInteger( "x", stud.XOrigin );
		        studTag.setInteger( "y", stud.YOrigin );
		        studTag.setInteger( "z", stud.ZOrigin );
		        studTag.setInteger( "w", stud.BrickWidth );
		        studTag.setInteger( "h", stud.BrickHeight );
		        studTag.setInteger( "d", stud.BrickDepth );
		        nbttagcompound.setTag( "s" + i, studTag );
			}
	    }
	}

    @Override
    public void readFromNBT( NBTTagCompound nbttagcompound )
    {
        super.readFromNBT( nbttagcompound );

		for( int i=0; i<Stud.STUDS_PER_BLOCK; ++i )
		{
			String key = "s" + i;
			if( nbttagcompound.hasKey( key ) )
			{
				Stud stud = new Stud();
		        NBTTagCompound studTag = nbttagcompound.getCompoundTag( key );
		        stud.Colour = studTag.getInteger( "c" );
		        stud.XOrigin = studTag.getInteger( "x" );
		        stud.YOrigin = studTag.getInteger( "y" );
		        stud.ZOrigin = studTag.getInteger( "z" );
		        stud.BrickWidth = studTag.getInteger( "w" );
		        stud.BrickHeight = studTag.getInteger( "h" );
		        stud.BrickDepth = studTag.getInteger( "d" );
		        m_studs[i] = stud;
			}
			else
			{
				m_studs[i] = null;
			}
	    }
    }
}
