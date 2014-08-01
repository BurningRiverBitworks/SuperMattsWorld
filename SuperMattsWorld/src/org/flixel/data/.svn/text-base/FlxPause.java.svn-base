package org.flixel.data;

import org.flixel.*;
import android.graphics.Paint.Align;

public class FlxPause extends FlxLayer
{
	public FlxPause()
	{
		super();
		scrollFactor.x = 0;
		scrollFactor.y = 0;
		
		int w = 95;
		int h = 160;
		x = (FlxG.width-w)/2;
		y = (FlxG.height-h)/2;
		FlxSprite background = new FlxSprite().createGraphic(w,h,0xFF000000,true);
		background.setAlpha(0.5f);
		add(background,true);			
		((FlxText)add(new FlxText(0,10,w,"PAUSED"), true)).setFormat(null,16,0xFFFFFFFF,Align.CENTER);
		
		int keypResource = FlxResourceManager.getDrawableResource("key_p");
		if (keypResource != -1)
			add(new FlxSprite(4,40,keypResource),true);
		
		add(new FlxText(28,40,w-16,"Pause Game"),true);
		
		int key0Resource = FlxResourceManager.getDrawableResource("key_0");
		if (key0Resource != -1)
			add(new FlxSprite(4,66,key0Resource),true);
		
		add(new FlxText(28,66,w-16,"Mute Sound"),true);
		
		int keyMinusResource = FlxResourceManager.getDrawableResource("key_minus");
		if (keyMinusResource != -1)
			add(new FlxSprite(4,92,keyMinusResource),true);
		
		add(new FlxText(28,92,w-16,"Sound Down"),true);
		
		int keyPlusResource = FlxResourceManager.getDrawableResource("key_plus");
		if (keyPlusResource != -1)
			add(new FlxSprite(4,118,keyPlusResource),true);
		
		add(new FlxText(28,118,w-16,"Sound Up"),true);
	}
}

