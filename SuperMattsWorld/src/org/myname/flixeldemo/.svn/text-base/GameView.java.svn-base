package org.myname.flixeldemo;

import org.flixel.FlxGame;
import org.flixel.FlxGameView;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

public class GameView extends FlxGameView 
{
	/*
	 * Made this public for outside use. May be moved in the future
	 */
	public static Resources res;

	public GameView(Context context, AttributeSet attrs)
	{
		super(
			new FlxGame(455, 320, MenuState.class, context, R.class), 
			context, 
			attrs
		);

		res = super.getResources();
	}
}
