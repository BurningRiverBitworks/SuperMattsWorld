package org.myname.flixeldemo;

import java.util.ArrayList;

import org.flixel.FlxCore;
import org.flixel.FlxG;

import flash.display.BitmapData;
import flash.geom.Point;
import flash.geom.Rectangle;

public class BackgroundBlock extends FlxCore
{

	protected BitmapData _pixels = null;
	protected ArrayList<Rectangle> _rects = null;
	//protected int _tileSize;
	protected int _tileHeight;
	protected int _tileWidth;
	protected Point _p;

	//@desc		Constructor
	//@param	X			The X position of the block
	//@param	Y			The Y position of the block
	//@param	Width		The width of the block
	//@param	Height		The height of the block
	public BackgroundBlock(int X, int Y, int Width, int Height)
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
	public BackgroundBlock loadGraphic(int TileGraphic, int Empties)
	{
		//if(TileGraphic == null)
		//return;

		_pixels = FlxG.addBitmap(TileGraphic);
		_rects = new ArrayList<Rectangle>();
		_p = new Point();
		_tileHeight = _pixels.height;
		_tileWidth = _pixels.width;
		int widthInTiles = (int) Math.ceil(width/_tileWidth);
		int heightInTiles = (int) Math.ceil(height/_tileHeight);
		width = widthInTiles*_tileWidth;
		height = heightInTiles*_tileHeight;
		int numTiles = widthInTiles*heightInTiles;
		int numGraphics = _pixels.width/_tileWidth;
		for(int i = 0; i < numTiles; i++)
		{
			float random = FlxG.random();
			if(random*(numGraphics+Empties) > Empties)
				_rects.add(new Rectangle((float) (_tileWidth*Math.floor(FlxG.random()*numGraphics)),0,_tileWidth,_tileHeight));
			else
				_rects.add(null);
		}

		return this;
	}

	public BackgroundBlock loadGraphic(int TileGraphic) {return loadGraphic(TileGraphic, 0);}

	//@desc		Draws this block
	public void render()
	{
		//-- performance ++
		if(!this.onScreen())
			return;

		super.render();
		getScreenXY(_p);
		int opx = (int) _p.x;
		int rl = _rects.size();
		for(int i = 0; i < rl; i++)
		{
			if(_rects.get(i) != null) FlxG.buffer.copyPixels(_pixels,_rects.get(i),_p,null,null,true);
			_p.x += _tileWidth;
			if(_p.x >= opx + width)
			{
				_p.x = opx;
				_p.y += _tileHeight;
			}
		}
	}
}
