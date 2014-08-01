package org.flixel;

import java.util.ArrayList;	
import flash.geom.Point;
	
	//@desc		A simple particle system class
	public class FlxEmitter extends FlxCore
	{
		public Point minVelocity;
		public Point maxVelocity;
		public float minRotation;
		public float maxRotation;
		public float gravity;
		public float drag;
		public float delay;
		protected ArrayList<FlxSprite> _sprites;
		protected float _timer;
		protected int _particle;
		protected FlxEmitterListener listener;
		
		//@desc		Constructor
		//@param	X				The X position of the emitter
		//@param	Y				The Y position of the emitter
		//@param	Delay			A negative number defines the lifespan of the particles that are launched all at once.  A positive number tells it how often to fire a new particle.
		public FlxEmitter(float X, float Y, float Delay)
		{
			super();
			constructor(X, Y, Delay);
		}
		
		public FlxEmitter()
		{
			super();
			constructor(0, 0, 0.1f);
		}
		
		protected void constructor(float X, float Y, float Delay)
		{			
			visible = false;
			x = X;
			y = Y;
			width = 0;
			height = 0;
			
			minVelocity = new Point(-100,-100);
			maxVelocity = new Point(100,100);
			minRotation = -360;
			maxRotation = 360;
			gravity = 400;
			drag = 0;
			delay = Delay;
			
			_sprites = null;
			listener = null;
			kill();
		}
		
		public FlxEmitter setListener(FlxEmitterListener listener)
		{
			this.listener = listener;
			return this;
		}
		
		//@desc		This function attaches an existing array of sprites to this particle emitter
		//@param	Sprites			A pre-configured FlxArrayList of FlxSprite objects for the emitter to use
		//@return	This FlxEmitter instance (nice for chaining stuff together, if you're into that)
		public FlxEmitter loadSprites(ArrayList<FlxSprite> Sprites)
		{
			_sprites = Sprites;
			int sl = _sprites.size();
			for(int i = 0; i < sl; i++)
				(_sprites.get(i)).scrollFactor = scrollFactor;
			kill();
			if(delay > 0)
				restart();
			return this;
		}
		
		//@desc		This function generates a new array of sprites to attach to the emitter
		//@param	Graphics		If you opted to not pre-configure an array of FlxSprite objects, you can simply pass in a particle image or sprite sheet
		//@param	Quantity		The number of particles to generate when using the "create from image" option
		//@param	Multiple		Whether the image in the Graphics param is a single particle or a bunch of particles (if it's a bunch, they need to be square!)
		//@param	Parent			A FlxLayer object that you can attach things to instead of the current state
		//@return	This FlxEmitter instance (nice for chaining stuff together, if you're into that)
		public FlxEmitter createSprites(int Graphics, int Quantity, boolean Multiple, FlxLayer Parent)
		{
			int i;
			int sl;
			_sprites = new ArrayList<FlxSprite>();
			for(i = 0; i < Quantity; i++)
			{
				if(Multiple)
				{
					FlxSprite s = new FlxSprite();
					s.loadGraphic(Graphics,true);
					s.randomFrame();
					_sprites.add(s);
				}
				else
					_sprites.add(new FlxSprite(0,0,Graphics));
			}
			sl = _sprites.size();
			for(i = 0; i < sl; i++)
			{
				if(Parent == null)
					FlxG.state.add((FlxCore) _sprites.get(i));
				else
					Parent.add(_sprites.get(i));
				(_sprites.get(i)).scrollFactor = scrollFactor;
			}
			kill();
			if(delay > 0)
				restart();
			return this;
		}
		
		public FlxEmitter createSprites(int Graphics) {return createSprites(Graphics, 50, true, null);}
		
		//@desc		A more compact way of setting the width and height of the emitter
		//@param	Width	The desired width of the emitter (particles are spawned randomly within these dimensions)
		//@param	Height	The desired height of the emitter
		public FlxEmitter setSize(int Width,int Height)
		{
			width = Width;
			height = Height;
			return this;
		}
		
		//@desc		A more compact way of setting the X velocity range of the emitter
		//@param	Min		The minimum value for this range
		//@param	Max		The maximum value for this range
		public FlxEmitter setXVelocity(float Min,float Max)
		{
			minVelocity.x = Min;
			maxVelocity.x = Max;
			return this;
		}
		
		//@desc		A more compact way of setting the Y velocity range of the emitter
		//@param	Min		The minimum value for this range
		//@param	Max		The maximum value for this range
		public FlxEmitter setYVelocity(float Min,float Max)
		{
			minVelocity.y = Min;
			maxVelocity.y = Max;
			return this;
		}
		
		//@desc		A more compact way of setting the angular velocity constraints of the emitter
		//@param	Min		The minimum value for this range
		//@param	Max		The maximum value for this range
		public FlxEmitter setRotation(float Min,float Max)
		{
			minRotation = Min;
			maxRotation = Max;
			return this;
		}	
		
		public FlxEmitter setGravity(float gravity)
		{
			this.gravity = gravity;
			return this;
		}
		
		//@desc		Called automatically by the game loop, decides when to launch particles and when to "die"
		public void update()
		{
			_timer += FlxG.elapsed;
			if(delay < 0)
			{
				if(_timer > -delay) { kill(); return; }
				if(((FlxSprite)_sprites.get(0)).exists) return;
				int sl = _sprites.size();
				for(int i = 0; i < sl; i++) emit();
				return;
			}
			while(_timer > delay)
			{
				_timer -= delay;
				emit();
			}
		}
		
		//@desc		Call this function to reset the emitter (if you used a negative delay, calling this function "explodes" the emitter again)
		public void restart()
		{
			if(_sprites == null)
			{
				FlxG.log("ERROR: You must attach sprites to an emitter for it to work.\nSee FlxEmitter.loadSprites() and FlxEmitter.createSprites() for more info.");
				return;
			}
			active = true;
			_timer = 0;
			_particle = 0;
		}
		
		//@desc		This function can be used both internally and externally to emit the next particle
		public void emit()
		{
			FlxSprite s = (FlxSprite)_sprites.get(_particle);
			s.reset(x - (s.width>>1) + FlxG.random()*width, y - (s.height>>1) + FlxG.random()*height);
			s.velocity.x = minVelocity.x;
			if(minVelocity.x != maxVelocity.x) s.velocity.x += FlxG.random()*(maxVelocity.x-minVelocity.x);
			s.velocity.y = minVelocity.y;
			if(minVelocity.y != maxVelocity.y) s.velocity.y += FlxG.random()*(maxVelocity.y-minVelocity.y);
			s.acceleration.y = gravity;
			s.angularVelocity = minRotation;
			if(minRotation != maxRotation) s.angularVelocity += FlxG.random()*(maxRotation-minRotation);
			if(s.angularVelocity != 0) s.angle = FlxG.random()*360-180;
			s.drag.x = drag;
			s.drag.y = drag;
			_particle++;
			if(_particle >= _sprites.size())
				_particle = 0;
			s.onEmit();
		}
		
		//@desc		Call this function to turn off all the particles and the emitter
		public void kill()
		{
			active = false;
			if(_sprites == null) return;
			int sl = _sprites.size();
			for(int i = 0; i < sl; i++)
				((FlxSprite)_sprites.get(i)).exists = false;
			
			if (listener != null)
				listener.emitterKilled(this);
		}
	}

