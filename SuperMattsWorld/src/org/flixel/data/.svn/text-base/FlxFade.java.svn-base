package org.flixel.data;

	import org.flixel.*;
	
	//@desc		This is a special effects utility class to help FlxGame do the 'fade' effect
	public class FlxFade extends FlxSprite
	{
		protected float _delay;
		protected FlxFadeListener _complete;
		protected float _helper;
		
		//@desc		Constructor initializes the fade object
		public FlxFade()
		{
			super();
			createGraphic(FlxG.width,FlxG.height,0,true);
			scrollFactor.x = 0;
			scrollFactor.y = 0;
			visible = false;
		}
		
		//@desc		Reset and trigger this special effect
		//@param	Color			The color you want to use
		//@param	Duration		How long it should take to fade the screen out
		//@param	FadeComplete	A function you want to run when the fade finishes
		//@param	Force			Force the effect to reset
		public void restart(int Color, float Duration, FlxFadeListener FadeComplete, boolean Force)
		{
			if(Duration == 0)
			{
				visible = false;
				return;
			}
			if(!Force && visible) return;
			fill(Color);
			_delay = Duration;
			_complete = FadeComplete;
			_helper = 0;
			setAlpha(0);
			visible = true;
		}
		
		public void restart(int Color, float Duration) {restart(Color, Duration, null, false);}
		public void restart() {restart(0, 1, null, false);}
		
		//@desc		Updates and/or animates this special effect
		public void update()
		{
			if(visible && (getAlpha() != 1))
			{
				_helper += FlxG.elapsed/_delay;
				setAlpha(_helper);
				if(getAlpha() >= 1)
				{
					setAlpha(1);
					if(_complete != null)
						_complete.fadeComplete();
				}
			}
		}
	}
