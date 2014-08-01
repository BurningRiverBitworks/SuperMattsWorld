package org.flixel;

import org.flixel.data.*;

import android.graphics.Bitmap;

import flash.geom.Point;

public class FlxLogoState extends FlxState 
{
	protected float _logoTimer;
	protected Bitmap _poweredBy;
	protected Bitmap _logoFade;

	protected int _fc;
	
	public FlxLogoState()
	{
		_logoTimer = 0;
		_fc = 0xFFFFFFFF;

		int scale = 1;
		if(FlxG.height > 200)
			scale = 2;
		int pixelSize = 32 * scale;
		int top = FlxG.height/2 - pixelSize*3;
		int left = FlxG.width/2 - pixelSize;
		
		add(new FlxLogoPixel(left+pixelSize, top, pixelSize, 0, _fc, pixelSize));
		add(new FlxLogoPixel(left, top+pixelSize, pixelSize, 1, _fc, pixelSize));
		add(new FlxLogoPixel(left, top+pixelSize*2,pixelSize, 2, _fc, pixelSize));
		add(new FlxLogoPixel(left+pixelSize, top+pixelSize*2, pixelSize, 3, _fc, pixelSize));
		add(new FlxLogoPixel(left, top+pixelSize*3, pixelSize, 4, _fc, pixelSize));
		
		int poweredbyResource = FlxResourceManager.getDrawableResource("poweredby");
		if (poweredbyResource != -1)
		{
			FlxSprite poweredBy = new FlxSprite(left, top+pixelSize*4+16, poweredbyResource);
			poweredBy.scale = new Point(scale, scale);
			poweredBy.scaleAndRotateFromCentre = false;
			add(poweredBy);
		}
		
		int flixelResource = FlxResourceManager.getRawResource("flixel");
		if (flixelResource != -1)
			FlxG.play(flixelResource);
	}
	
	public void update()
	{
		super.update();
		
		_logoTimer += FlxG.elapsed;
			
		if(_logoTimer > 2)
			FlxG.switchState(FlxGame.initialGameState);
	}
}
