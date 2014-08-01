package org.flixel.data;

	import org.flixel.*;
	
	//@desc		This is a special effects utility class to help FlxGame do the 'flash' effect
	public class FlxFlash extends FlxSprite
	{
		protected float _delay;
		protected FlxFadeListener _complete;
		protected float _helper;
		
		//@desc		Constructor for this special effect
		public FlxFlash()
		{
			super();
			createGraphic(FlxG.width,FlxG.height,0,true);
			scrollFactor.x = 0;
			scrollFactor.y = 0;
			visible = false;
		}
		
		//@desc		Reset and trigger this special effect
		//@param	Color			The color you want to use
		//@param	Duration		How long it takes for the flash to fade
		//@param	FlashComplete	A function you want to run when the flash finishes
		//@param	Force			Force the effect to reset
		public void restart(int Color, float Duration, FlxFadeListener FlashComplete, boolean Force)
		{
			if(Duration == 0)
			{
				visible = false;
				return;
			}
			if(!Force && visible) return;
			fill(Color);
			_delay = Duration;
			_complete = FlashComplete;
			_helper = 1;
			setAlpha(1);
			visible = true;
		}
		
		public void restart(int Color, float Duration) {restart(0, Duration, null, false);}
		public void restart() {restart(0xFFFFFFFF, 1, null, false);}
		
		//@desc		Updates and/or animates this special effect
		public void update()
		{
			if(visible)
			{
				_helper -= FlxG.elapsed/_delay;
				setAlpha(_helper);
				if(getAlpha() <= 0)
				{
					visible = false;
					if(_complete != null)
						_complete.fadeComplete();
				}
			}
		}
	}

