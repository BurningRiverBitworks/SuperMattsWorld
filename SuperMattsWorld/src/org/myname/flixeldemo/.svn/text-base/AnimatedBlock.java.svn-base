package org.myname.flixeldemo;
import java.util.ArrayList;

import org.flixel.FlxAnimationListener;
import org.flixel.FlxBlock;
import org.flixel.FlxG;
import org.flixel.data.FlxAnim;

import flash.display.BitmapData;
import flash.geom.ColorTransform;
import flash.geom.Matrix;
import flash.geom.Point;
import flash.geom.Rectangle;

/*
 * FlxBlock does not offer a way to animate a block.
 * This class is used to make animated blocks.
 * Most of this code was taken from FlxSprite.
 */
public class AnimatedBlock extends FlxBlock
{
	static protected final Point _pZero = new Point();
	public float angle;
	//@desc	The origin of the sprite will default to its center - if you change this, the visuals and the collisions will likely be pretty out-of-sync!  Just a heads up.
	public Point origin;
	//@desc	Scale doesn't currently affect collisions automatically, you will need to adjust the width, height and offset manually.  WARNING: scaling sprites decreases rendering performance for this sprite by a factor of 10x!
	public Point scale;
	public String blend;
	//@desc	Controls whether the object is smoothed when rotated, affects performance, off by default
	public boolean antialiasing;

	public boolean scaleAndRotateFromCentre = true;
	//@desc	Whether the current animation has finished its first (or only) loop
	public boolean finished;
	protected ArrayList<FlxAnim> _animations;
	protected FlxAnim _curAnim;
	protected int _curFrame;
	protected int _caf;
	protected float _frameTimer;
	protected FlxAnimationListener _callback;
	//helpers
	protected int _bw;
	protected int _bh;
	protected Rectangle _r;
	protected Rectangle _r2;
	protected Point _p;
	protected BitmapData _pixels;
	protected BitmapData _framePixels;
	protected ColorTransform _ct;
	protected Matrix _mtx;
	
	protected boolean isAnimated;
	
	public AnimatedBlock(int x, int y, int width, int height)
	{
		super(x, y, width, height);

		_p = new Point();
		_r = new Rectangle();
		_r2 = new Rectangle();
		origin = new Point();
		angle = 0;
		scale = new Point(1,1);
		blend = null;
		antialiasing = false;	
		finished = false;
		_animations = new ArrayList<FlxAnim>();
		_curAnim = null;
		_curFrame = 0;
		_caf = 0;
		_frameTimer = 0;
		_mtx = new Matrix();
		_callback = null;
		isAnimated = false;
	}
	
	public AnimatedBlock loadGraphic(int Graphic)
	{
		super.loadGraphic(Graphic);
		this.isAnimated = false;
		return this;
	}
	
	public AnimatedBlock loadGraphic(int Graphic, boolean Animated, int Width, int Height)
	{
		_pixels = FlxG.addBitmap(Graphic,false);
		this.isAnimated = Animated;
		
		if(Width == 0)
		{
			if(Animated)
				Width = _pixels.height;
			else
				Width = _pixels.width;
		}
		width = _bw = Width;
		if(Height == 0)
		{
			if(Animated)
				Height = width;
			else
				Height = _pixels.height;
		}
		height = _bh = Height;
		resetHelpers();
		return this;
	}
	
	protected void resetHelpers()
	{
		_r.setX(0);
		_r.setY(0);
		_r.setWidth(_bw);
		_r.setHeight(_bh);
		_r2.setX(0);
		_r2.setY(0);
		_r2.setWidth(_pixels.width);
		_r2.setHeight(_pixels.height);
		if((_framePixels == null) || (_framePixels.width != width) || (_framePixels.height != height))
			_framePixels = new BitmapData(width,height);
		origin.x = _bw/2.0f;
		origin.y = _bh/2.0f;
		_framePixels.copyPixels(_pixels,_r,_pZero);
	}
	
	public void addAnimation(String Name, ArrayList<Integer> Frames, float FrameRate)
	{
		_animations.add(new FlxAnim(Name,Frames,FrameRate,true));
	}
	
	public void play(String AnimName)
	{
		if((_curAnim != null) && (AnimName == _curAnim.name) || !isAnimated) return;
		_curFrame = 0;
		_caf = 0;
		_frameTimer = 0;
		int al = _animations.size();
		for(int i = 0; i < al; i++)
		{
			if(((FlxAnim)_animations.get(i)).name.equals(AnimName))
			{
				finished = false;
				_curAnim = (FlxAnim) _animations.get(i);
				Integer frame = _curAnim.frames.get(_curFrame);
				_caf = frame.intValue();
				calcFrame();
				return;
			}
		}
	}
	
	protected void calcFrame()
	{
		int rx = _caf*_bw;
		int ry = 0;
		//Handle sprite sheets
		int w = _pixels.width;
		if(rx >= w)
		{
			ry = (int)(rx/w)*_bh;
			rx %= w;
		}
		
		BitmapData source = _pixels;
		
		//Update display bitmap
		_r.setX(rx);
		_r.setY(ry);
		_framePixels.clearBitmap();
		_framePixels.copyPixels(source, _r, _pZero);
		_r.setX(0); _r.setY(0);
		if(_ct != null) _framePixels.colorTransform(_r,_ct);
		if(_callback != null) _callback.animationChanged(_curAnim.name,_curFrame,_caf);
	}
	
	public void update()
	{
		super.update();
		
		if(!active || !isAnimated) return;
		
		//animation
		if((_curAnim != null) && (_curAnim.delay > 0) && (_curAnim.looped || !finished))
		{
			_frameTimer += FlxG.elapsed;
			if(_frameTimer > _curAnim.delay)
			{
				_frameTimer -= _curAnim.delay;
				if(_curFrame == _curAnim.frames.size()-1)
				{
					if(_curAnim.looped) _curFrame = 0;
					finished = true;
				}
				else
					_curFrame++;
				_caf = _curAnim.frames.get(_curFrame).intValue();
				calcFrame();
			}
		}
	}
	
	public void render()
	{
		if(!isAnimated)
		{
			super.render();
		}
		else
		{
			if(!visible)
				return;
			
			getScreenXY(_p);
			
			//Simple render
			if((angle == 0) && (scale.x == 1) && (scale.y == 1) && (blend == null))
			{
				FlxG.buffer.copyPixels(_framePixels,_r,_p,null,null,true);
				return;
			}
			
			Point matrixOrigin = new Point();
			if (scaleAndRotateFromCentre)
			{
				matrixOrigin.x = origin.x;
				matrixOrigin.y = matrixOrigin.y;
			}	
			
			//Advanced render
			_mtx.identity();
			_mtx.translate(-matrixOrigin.x, -matrixOrigin.y);
			_mtx.scale(scale.x,scale.y);
			if(angle != 0) _mtx.rotate((float) (Math.PI * 2 * (angle / 360)));
			_mtx.translate(_p.x + matrixOrigin.x, _p.y + matrixOrigin.y);
			FlxG.buffer.draw(_framePixels,_mtx,null,blend,null,antialiasing);
		}
	}
}
