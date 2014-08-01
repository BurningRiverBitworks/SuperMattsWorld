package org.flixel.data;

	import org.flixel.FlxG;
	
	//@desc		This is a special effects utility class to help FlxGame do the 'quake' or screenshake effect
	public class FlxQuake
	{
		protected int _zoom;
		protected float _intensity;
		protected float _length;
		protected float _timer;
		
		public int x;
		public int y;
		
		public FlxQuake(int Zoom)
		{
			_zoom = Zoom;
			reset(0);
		}
		
		//@desc		Reset and trigger this special effect
		//@param	Intensity	Percentage of screen size representing the maximum distance that the screen can move during the 'quake'
		//@param	Duration	The length in seconds that the "quake" should last
		public void reset(float Intensity, float Duration)
		{
			x = 0;
			y = 0;
			_intensity = Intensity;
			if(_intensity == 0)
			{
				_length = 0;
				_timer = 0;
				return;
			}
			_length = Duration;
			_timer = 0.01f;
		}
		
		public void reset(float Intensity)
		{
			reset(Intensity, 0.5f);
		}
		
		//@desc		Updates and/or animates this special effect
		public void update()
		{
			if(_timer > 0)
			{
				_timer += FlxG.elapsed;
				if(_timer > _length)
				{
					_timer = 0;
					x = 0;
					y = 0;
				}
				else
				{
					x = (int) ((Math.random()*_intensity*FlxG.width*2-_intensity*FlxG.width)*_zoom);
					y = (int) ((Math.random()*_intensity*FlxG.height*2-_intensity*FlxG.height)*_zoom);
				}
			}
		}
	}

