package org.myname.flixeldemo;

import java.util.ArrayList;
import java.util.Arrays;

import org.flixel.FlxEmitter;
import org.flixel.FlxFadeListener;
import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.myname.flixeldemo.parsing.Level;

import android.view.KeyEvent;
import audio.SFXPool;

public class Player extends FlxSprite
{
	public static final int HEALTH_MATT = 1;
	public static final int HEALTH_STEVE = 2;

	private static final int PLAYER_START_X = 10;
	private static final int PLAYER_START_Y = 640-230;

	private static final int PLAYER_WIDTH_PX = 32;
	private static final int PLAYER_HEIGHT_PX = 32;

	public static final int PLAYER_RUN_SPEED = 180;
	public static final int GRAVITY_ACCELERATION = 500; //-- 420 (600 seems best)
	public static final int JUMP_ACCELERATION = 400; //-- 400

	private static final String MATT_IDLE = "idle";
	private static final String MATT_RUN = "run";
	private static final String MATT_JUMP = "jump";

	private static final String STEVE_IDLE = "idle_s";
	private static final String STEVE_RUN = "run_s";
	private static final String STEVE_JUMP = "jump_s";

	/* Add to state! */
	public static final FlxEmitter CHUNKIES = new FlxEmitter(0, 0, -1.5f)
	.setXVelocity(-150.0f, 150.0f)
	.setYVelocity(-200.0f, 0.0f)
	.setRotation(-720, 720)
	.setGravity(400f);

	public Player()
	{
		super(PLAYER_START_X, PLAYER_START_Y, R.drawable.matt, true, PLAYER_WIDTH_PX, PLAYER_HEIGHT_PX);

		drag.x = PLAYER_RUN_SPEED * 8;
		acceleration.y = GRAVITY_ACCELERATION;
		maxVelocity.x = PLAYER_RUN_SPEED;
		maxVelocity.y = JUMP_ACCELERATION;

		addAnimation(MATT_IDLE, new ArrayList<Integer>(Arrays.asList(new Integer[] {0})));
		addAnimation(MATT_RUN,  new ArrayList<Integer>(Arrays.asList(new Integer[] {1, 2, 3})), 16);
		addAnimation(MATT_JUMP,  new ArrayList<Integer>(Arrays.asList(new Integer[] {4})));

		addAnimation(STEVE_IDLE, new ArrayList<Integer>(Arrays.asList(new Integer[] {5})));
		addAnimation(STEVE_RUN,  new ArrayList<Integer>(Arrays.asList(new Integer[] {6, 7, 8})), 16);
		addAnimation(STEVE_JUMP,  new ArrayList<Integer>(Arrays.asList(new Integer[] {9})));
	}

	@Override
	public void update()
	{
		acceleration.x = 0;
		//-- AJG oops (pressed not justPressed)
		if(FlxG.keys.pressed(KeyEvent.KEYCODE_DPAD_LEFT) || FlxG.keys.pressed(KeyEvent.KEYCODE_A))
		{
			setFacing(LEFT);
			acceleration.x = -drag.x;
		}
		//-- AJG oops (pressed not justPressed)
		else if(FlxG.keys.pressed(KeyEvent.KEYCODE_DPAD_RIGHT) || FlxG.keys.pressed(KeyEvent.KEYCODE_D))
		{
			setFacing(RIGHT);
			acceleration.x = drag.x;
		}
		if((FlxG.keys.justPressed(KeyEvent.KEYCODE_DPAD_UP) || FlxG.keys.justPressed(KeyEvent.KEYCODE_L)) && velocity.y==0)
		{
			velocity.y = -JUMP_ACCELERATION;
			SFXPool.playSound(R.raw.jumpsfx);
		}

		if(velocity.y != 0)
			play(this.health == HEALTH_MATT ? MATT_JUMP : STEVE_JUMP);
		else if(velocity.x == 0)
			play(this.health == HEALTH_MATT ? MATT_IDLE : STEVE_IDLE);
		else
			play(this.health == HEALTH_MATT ? MATT_RUN : STEVE_RUN);

		super.update();
	}

	@Override
	public void kill()
	{
		super.kill();

		SFXPool.playSound(R.raw.death1);
		CHUNKIES.x = this.x + (this.width>>1);
		CHUNKIES.y = this.y + (this.height>>1);

		//-- AJG Load giblets on death. This cannot be statically added due to the need
		//   to add to the FlxG current state and bitmap cache.
		CHUNKIES.createSprites(R.drawable.giblets);
		CHUNKIES.restart();

		//-- clear level saves so I am not always dead!!!
		/* TODO
		 * if implementing "lives", only clear current level and reset, so
		 * Level.java will require a method to do this.
		 * and only switch to menu when lives == 0
		 */
		Level.levelSaves.clear();

		//-- Fade out for dramatic effect before
		//   going back to the title screen.
		FlxG.fade(0xffffffff, 2, new FlxFadeListener()
		{
			public void fadeComplete()
			{
				FlxG.switchState(MenuState.class);
			}
		}
		);
	}

	@Override
	public void reset(float X, float Y)
	{
		super.reset(X, Y);
		this.health = HEALTH_MATT;
	}
}