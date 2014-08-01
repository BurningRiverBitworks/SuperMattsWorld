package org.myname.flixeldemo;

import java.util.ArrayList;
import java.util.Arrays;

import org.flixel.FlxCore;
import org.flixel.FlxG;

import flash.display.BitmapData;

public class DeathBlock extends FlxCore
{		
	protected ArrayList<AnimatedBlock> blocks = new ArrayList<AnimatedBlock>();
	
	/*
	 * Uses an ArrayList of AnimatedBlocks because there is no simple way to animate  a line of blocks
	 * The ArrayList contains either a single entry if the block is stationary, or has width/n entries
	 * where each entry is n pixels wide. When setting the width of the DeathBlock its best for the 
	 * width to be evenly divisible by the pixel width of the image(or one section of the image if
	 * is animated). Height cannot be specified because will cause issues if the height specified is
	 * different than the height of the graphic
	 */
	public DeathBlock(int x, int y, int width, Integer Graphic)
	{
		BitmapData pixels = FlxG.addBitmap(Graphic, false);
		
		int numberOfFrames = pixels.width / pixels.height;

		if(numberOfFrames > 1)
		{
			//animated deathblock
			Integer[] imageFrames = new Integer[numberOfFrames];
			for(int i = 0; i < numberOfFrames; i++)
			{
				imageFrames[i] = i;
			}
			ArrayList<Integer> Frames = new ArrayList<Integer>(Arrays.asList(imageFrames));
			
			int widthOfBlock = pixels.width / numberOfFrames;
			int numberOfBlocks = width / widthOfBlock;
			for(int i = 0; i < numberOfBlocks; i++)
			{
				blocks.add(new AnimatedBlock(x + i*widthOfBlock, y, widthOfBlock, pixels.height));
				blocks.get(i).loadGraphic(Graphic, true, widthOfBlock, pixels.height);
				blocks.get(i).addAnimation("idle", Frames, 2);
			}
		}
		else
		{
			//non-animated deathblock
			blocks.add(new AnimatedBlock(x, y, width, pixels.height));
			blocks.get(0).loadGraphic(Graphic);
		}
	}
	
	public void update()
	{
		for(AnimatedBlock ab : blocks)
		{
			if(ab.isAnimated && ab.onScreen())
			{
				ab.play("idle");
			}
			ab.update();
		}
	}
	
	public void render()
	{
		for(AnimatedBlock ab : blocks)
		{
			ab.render();
		}
	}
	
	public boolean collide(FlxCore Core)
	{
		boolean collision = false;
		for(AnimatedBlock ab : blocks)
		{
			if(ab.collide(Core))
			{
				collision = true;
			}
		}
		return collision;
	}
	
	public boolean overlaps(FlxCore Core)
	{
		boolean overlap = false;
		for(AnimatedBlock ab : blocks)
		{
			if(ab.overlaps(Core))
			{
				overlap = true;
			}
		}
		return overlap;
	}
}
