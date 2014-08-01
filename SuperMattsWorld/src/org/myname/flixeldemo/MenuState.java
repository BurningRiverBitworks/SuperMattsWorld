package org.myname.flixeldemo;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.myname.flixeldemo.parsing.Level;

import android.view.KeyEvent;

public class MenuState extends FlxState 
{
	public MenuState()
	{
		final int titlex = FlxG.width >> 2;
		final int titley = FlxG.height >> 2;
		FlxText text = new FlxText(titlex,  titley, 250, "Super Matt's World!");
		text.setSize(22);
		super.add(text);
		text = new FlxText(titlex + 10, titley + 75, 250, "Press the center DPad key to continue.");
		text.flicker(1F);
		super.add(text);
	}
	
	public void update()
	{
		super.update();
		if (FlxG.keys.pressed(KeyEvent.KEYCODE_DPAD_CENTER) || FlxG.keys.pressed(KeyEvent.KEYCODE_ENTER))
			FlxG.switchState(Level.class);
	}
}
