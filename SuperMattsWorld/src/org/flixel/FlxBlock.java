package org.flixel;

import java.util.ArrayList;

import org.myname.flixeldemo.Player;
import org.myname.flixeldemo.R;

import audio.SFXPool;

import flash.display.BitmapData;
import flash.geom.Point;
import flash.geom.Rectangle;

//@desc		This is the basic "environment object" class, used to create walls and floors
public class FlxBlock extends FlxCore
{
	protected BitmapData _pixels = null;
	protected ArrayList<Rectangle> _rects = null;
	protected int _tileSize;
	protected Point _p;
	
	//@desc		Constructor
	//@param	X			The X position of the block
	//@param	Y			The Y position of the block
	//@param	Width		The width of the block
	//@param	Height		The height of the block
	public FlxBlock(int X, int Y, int Width, int Height)
	{
		super();
		x = X;
		y = Y;
		width = Width;
		height = Height;
		fixed = true;
	}
	
	//@desc		Fills the block with a randomly arranged selection of graphics from the image provided
	//@param	TileGraphic The graphic class that contains the tiles that should fill this block
	//@param	Empties		The number of "empty" tiles to add to the auto-fill algorithm (e.g. 8 tiles + 4 empties = 1/3 of block will be open holes)
	public FlxBlock loadGraphic(int TileGraphic, int Empties)
	{
		//if(TileGraphic == null)
			//return;

		_pixels = FlxG.addBitmap(TileGraphic);
		_rects = new ArrayList<Rectangle>();
		_p = new Point();
		_tileSize = _pixels.height;
		int widthInTiles = (int) Math.ceil(width/_tileSize);
		int heightInTiles = (int) Math.ceil(height/_tileSize);
		width = widthInTiles*_tileSize;
		height = heightInTiles*_tileSize;
		int numTiles = widthInTiles*heightInTiles;
		int numGraphics = _pixels.width/_tileSize;
		for(int i = 0; i < numTiles; i++)
		{
			float random = FlxG.random();
			if(random*(numGraphics+Empties) > Empties)
				_rects.add(new Rectangle((float) (_tileSize*Math.floor(FlxG.random()*numGraphics)),0,_tileSize,_tileSize));
			else
				_rects.add(null);
		}
		
		return this;
	}
	
	public FlxBlock loadGraphic(int TileGraphic) {return loadGraphic(TileGraphic, 0);}
	
	//@desc		Draws this block
	public void render()
	{
		if(!this.onScreen())
			return;
		super.render();
		getScreenXY(_p);
		int opx = (int) _p.x;
		int rl = _rects.size();
		for(int i = 0; i < rl; i++)
		{
			if(_rects.get(i) != null) FlxG.buffer.copyPixels(_pixels,_rects.get(i),_p,null,null,true);
			_p.x += _tileSize;
			if(_p.x >= opx + width)
			{
				_p.x = opx;
				_p.y += _tileSize;
			}
		}
	}

	@Override
	/*
	 * Player HeadBlock fix.
	 */
	public boolean collideY(FlxCore Core)
	{
		final boolean collide = super.collideY(Core);

		if(collide && Core instanceof Player 
				&& this.y < Core.y)
		{
			final Player p = (Player)Core;
			p.acceleration.y = Player.JUMP_ACCELERATION;
			//-- do not let the player go stationary.
			p.velocity.y = 2;
			p.y += 1;

			SFXPool.playSound(R.raw.head_blocksfx);
		}

		return collide;
	}
}