package org.flixel;

import java.io.IOException;

import flash.geom.Point;

import android.media.MediaPlayer;

	/*import flash.events.Event;
	import flash.geom.Point;
	import flash.media.Sound;
	import flash.media.SoundChannel;
	import flash.media.SoundTransform;
	import flash.net.URLRequest;*/
	
	//@desc		This is the universal flixel sound object, used for streaming, music, and sound effects
	public class FlxSound extends FlxCore
	{
		//@desc		Whether or not this sound should be automatically destroyed when you switch states
		public boolean survive;
		
		protected boolean _init;
		protected MediaPlayer _mediaPlayer;
		//protected Sound _sound;
		//protected SoundChannel _channel;
		//protected SoundTransform _transform;
		protected float _panValue;
		protected float _volume;
		protected float _volumeAdjust;
		protected boolean _looped;
		protected FlxCore _core;
		protected float _radius;
		protected boolean _pan;
		protected float _fadeOutTimer;
		protected float _fadeOutTotal;
		protected boolean _pauseOnFadeOut;
		protected float _fadeInTimer;
		protected float _fadeInTotal;
		
		//@desc		The FlxSound constructor gets all the variables initialized, but NOT ready to play a sound yet
		public FlxSound()
		{
			super();
			//_transform = new SoundTransform();
			init();
		}
		
		//@desc		An internal function for clearing all the variables used by sounds
		protected void init()
		{
			_volume = 1.0f;
			_volumeAdjust = 1.0f;
			_looped = false;
			_core = null;
			_radius = 0;
			_pan = false;
			_fadeOutTimer = 0;
			_fadeOutTotal = 0;
			_pauseOnFadeOut = false;
			_fadeInTimer = 0;
			_fadeInTotal = 0;
			active = false;
			visible = false;
			dead = true;
			survive = false;
		}
		
		//@desc		One of two main setup functions for sounds, this function loads a sound from an embedded MP3
		//@param	EmbeddedSound	An embedded Class object representing an MP3 file
		//@param	Looped			Whether or not this sound should loop endlessly
		//@return	This FlxSound instance (nice for chaining stuff together, if you're into that)
		public FlxSound loadEmbedded(int EmbeddedSound, boolean Looped)
		{
			stop();
			init();
			_mediaPlayer = MediaPlayer.create(FlxResourceManager.context, EmbeddedSound);
			_looped = Looped;
			updateTransform();
			active = true;
			return this;
		}
		
		public FlxSound loadEmbedded(int EmbeddedSound) {return loadEmbedded(EmbeddedSound, false);}
		
		//@desc		One of two main setup functions for sounds, this function loads a sound from a URL
		//@param	EmbeddedSound	A string representing the URL of the MP3 file you want to play
		//@param	Looped			Whether or not this sound should loop endlessly
		//@return	This FlxSound instance (nice for chaining stuff together, if you're into that)
		public FlxSound loadStream(String SoundURL, boolean Looped)
		{
			stop();
			init();
			_mediaPlayer = new MediaPlayer();
			
			try 
			{
				_mediaPlayer.setDataSource(SoundURL);
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalStateException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			try 
			{
				_mediaPlayer.prepare();
			} 
			catch (IllegalStateException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			_looped = Looped;
			updateTransform();
			active = true;
			return this;
		}
		
		public FlxSound loadStream(String SoundURL) {return loadStream(SoundURL, false);}
		
		//@desc		Call this function if you want this sound's volume to change based on distance from a particular FlxCore object
		//@param	X		The X position of the sound
		//@param	Y		The Y position of the sound
		//@param	Core	The object you want to track
		//@param	Radius	The maximum distance this sound can travel
		//@return	This FlxSound instance (nice for chaining stuff together, if you're into that)
		public FlxSound proximity(float X,float Y,FlxCore Core,float Radius,boolean Pan)
		{
			x = X;
			y = Y;
			_core = Core;
			_radius = Radius;
			_pan = Pan;
			return this;
		}
		
		public FlxSound proximity(float X,float Y,FlxCore Core,float Radius) {return proximity(X, Y, Core, Radius, true);}
		
		//@desc		Call this function to play the sound
		public void play()
		{
			if(_mediaPlayer == null)
				return;
			
			_mediaPlayer.setLooping(_looped);
			_mediaPlayer.start();			
		}
		
		//@desc		Call this function to pause this sound
		public void pause()
		{
			if(_mediaPlayer == null)
				return;
			
			_mediaPlayer.pause();
		}
		
		//@desc		Call this function to stop this sound
		public void stop()
		{
			if(_mediaPlayer == null)
				return;
			
			_mediaPlayer.stop();
		}
		
		//@desc		Call this function to make this sound fade out over a certain time interval
		//@param	Seconds		The amount of time the fade out operation should take
		public void fadeOut(float Seconds,boolean PauseInstead)
		{
			_pauseOnFadeOut = PauseInstead;
			_fadeInTimer = 0;
			_fadeOutTimer = Seconds;
			_fadeOutTotal = _fadeOutTimer;
		}
		
		public void fadeOut(float Seconds) {fadeOut(Seconds, false);}
		
		//@desc		Call this function to make a sound fade in over a certain time interval (calls play() automatically)
		//@param	Seconds		The amount of time the fade in operation should take
		public void fadeIn(float Seconds)
		{
			_fadeOutTimer = 0;
			_fadeInTimer = Seconds;
			_fadeInTotal = _fadeInTimer;
			play();
		}
		
		//@desc		This is the setter for FlxSound.volume
		//@param	Volume		The new volume for this sound (a number between 0 and 1.0)
		public void setVolume(float Volume)
		{
			_volume = Volume;
			if(_volume < 0)
				_volume = 0;
			else if(_volume > 1)
				_volume = 1;
			
			updateTransform();
		}
		
		//@desc		This is the getter for FlxSound.volume
		//@return	The volume of this sound (a number between 0 and 1.0)
		public float getVolume()
		{
			return _volume;
		}
		
		//@desc		The basic game loop update function - doesn't do much except optional proximity and fade calculations
		public void update()
		{
			super.update();
			
			float radial = 1.0f;
			float fade = 1.0f;
			
			//Distance-based volume control
			if(_core != null)
			{
				Point pc = new Point();
				Point pt = new Point();
				_core.getScreenXY(pc);
				getScreenXY(pt);
				float dx = pc.x - pt.x;
				float dy = pc.y - pt.y;
				radial = (float) ((_radius - Math.sqrt(dx*dx + dy*dy))/_radius);
				if(radial < 0) radial = 0;
				if(radial > 1) radial = 1;
				
				if(_pan)
				{
					_panValue = -dx/_radius;
					if(_panValue < -1) _panValue = -1;
					else if(_panValue > 1) _panValue = 1;
				}
			}
			
			//Cross-fading volume control
			if(_fadeOutTimer > 0)
			{
				_fadeOutTimer -= FlxG.elapsed;
				if(_fadeOutTimer <= 0)
				{
					if(_pauseOnFadeOut)
						pause();
					else
						stop();
				}
				fade = _fadeOutTimer/_fadeOutTotal;
				if(fade < 0) fade = 0;
			}
			else if(_fadeInTimer > 0)
			{
				_fadeInTimer -= FlxG.elapsed;
				fade = _fadeInTimer/_fadeOutTotal;
				if(fade < 0) fade = 0;
				fade = 1 - fade;
			}
			
			_volumeAdjust = radial*fade;
			updateTransform();
		}
		
		//@desc		The basic class destructor, stops the music and removes any leftover events
		public void destroy()
		{
			if(active)
				stop();
		}
		
		//@desc		An internal function used to help organize and change the volume of the sound
		void updateTransform()
		{
			float volume = FlxG.getMuteValue()*FlxG.getVolume()*_volume*_volumeAdjust;
			float normalisedPan = _panValue + 1;
			float panLeft = normalisedPan > 1.0f ? 2.0f - normalisedPan : 1.0f;
			float panRight = normalisedPan < 1.0f ? normalisedPan : 1.0f;
			_mediaPlayer.setVolume(volume * panLeft, volume * panRight);
		}
	}

