package org.flixel;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

//import flash.display.Bitmap;
import flash.display.BitmapData;
import flash.geom.Matrix;
import flash.geom.Point;
//import flash.net.URLRequest;
//import flash.net.navigateToURL;

import org.flixel.data.FlxKeyboard;
//import com.mcasperson.flixel.data.FlxKong;
//import com.mcasperson.flixel.data.FlxMouse;
	
	//@desc		This is a global helper class full of useful functions for audio, input, basic info, and the camera system
	public class FlxG
	{
		//[Embed(source="data/cursor.png")] static protected Class ImgDefaultCursor;
		
		static public String LIBRARY_NAME = "flixel";
		static public int LIBRARY_MAJOR_VERSION = 1;
		static public int LIBRARY_MINOR_VERSION = 50;
		static protected boolean _pause;
		static protected FlxGame _game;
		static public boolean debug;
		
		//@desc Represents the amount of time in seconds that passed since last frame
		static public float elapsed;
		//@desc A reference or pointer to the current FlxState object being used by the game
		static public FlxState state;
		//@desc The width of the screen in game pixels
		static public int width;
		//@desc The height of the screen in game pixels
		static public int height;
		//@desc FlxG.levels and FlxG.scores are generic global variables that can be used for various cross-state stuff
		static public ArrayList<Object> levels;
		static public int level;
		static public ArrayList<Object> scores;
		static public int score;
		//@desc FlxG.saves is a generic bucket for storing FlxSaves so you can access them whenever you want
		//static public ArrayList<FlxSave> saves; 
		static public int save;
		//@desc The current game coordinates of the mouse pointer (not necessarily the screen coordinates)
		//static public FlxMouse mouse;
		static public FlxKeyboard keys;
		
		//audio
		static public FlxSound music;
		static public ArrayList<FlxSound> sounds;
		static protected boolean _mute;
		static protected float _volume;
		
		//Ccmera system variables
		static public FlxCore followTarget;
		static public Point followLead;
		static public float followLerp;
		static public Point followMin;
		static public Point followMax;
		static protected Point _scrollTarget;
		
		//graphics stuff
		static public Point scroll;
		static public BitmapData buffer;
		static protected Dictionary<String, BitmapData> _cache;
		
		//Kongregate API object
		//static public FlxKong kong;
		
		//Random number stuff
		static protected float _seed;
		static protected float _originalSeed;
		
		//@desc		The setter for FlxG.pause - pauses the game + sounds and displays the pause message
		static public void setPause(boolean Pause)
		{
			boolean op = _pause;
			_pause = Pause;
			if(_pause != op)
			{
				if(_pause)
				{
					_game.pauseGame();
					pauseSounds();
				}
				else
				{
					_game.unpauseGame();
					playSounds();
				}
			}
		}
		
		//@desc		The getter for FlxG.pause - just returns whether the game is paused or not
		static public boolean getPause()
		{
			return _pause;
		}
		
		//@desc		Call this function to reset the input objects (useful when changing screens or states)
		static public void resetInput()
		{
			keys.reset();
			//mouse.reset();
		}
		
		//@desc		Picks an entry at random from an array
		//@param	A		The array you want to pick the object from
		//@return	Any object
		static public Object getRandom(ArrayList<? extends Object> A)
		{
			return A.get((int)(FlxG.random()*A.size()));
		}
		
		//@desc		Find the first entry in the array that doesn't "exist"
		//@param	A		The array you want to search
		//@return	Anything based on FlxCore (FlxSprite, FlxText, FlxBlock, etc)
		static public FlxCore getNonexist(ArrayList<? extends FlxCore> A)
		{
			int l = A.size();
			if(l <= 0) return null;
			int i = 0;
			do
			{
				if(!((FlxCore)A.get(i)).exists)
					return (FlxCore) A.get(i);
			} while (++i < l);
			return null;
		}
		
		//@desc		Set up and autoplay a music track
		//@param	Music		The sound file you want to loop in the background
		//@param	Volume		How loud the sound should be, from 0 to 1
		static public void playMusic(int Music,float Volume, boolean survives)
		{
			if(music == null)
				music = new FlxSound();
			else if(music.active)
				music.stop();
			music.loadEmbedded(Music,true);
			music.setVolume(Volume);
			music.survive = survives;
			music.play();
		}
		
		static public void playMusic(int Music) {playMusic(Music, 1.0f, false);}
		static public void playMusic(int Music, float Volume) {playMusic(Music, Volume, false);}
		
		//@desc		Creates a new sound object from an embedded Class object
		//@param	EmbeddedSound	The sound you want to play
		//@param	Volume			How loud to play it (0 to 1)
		//@param	Looped			Whether or not to loop this sound
		//@return	A FlxSound object
		static public FlxSound play(int EmbeddedSound, float Volume, boolean Looped)
		{
			int i;
			int sl = sounds.size();
			for(i = 0; i < sl; i++)
				if(!sounds.get(i).active)
					break;
			if (i >= sounds.size())
				sounds.add(new FlxSound());
			FlxSound s = sounds.get(i);
			s.loadEmbedded(EmbeddedSound,Looped);
			s.setVolume(Volume);
			s.play();
			return s;
		}
		
		static public FlxSound play(int EmbeddedSound, float Volume) {return play(EmbeddedSound, Volume, false);}
		static public FlxSound play(int EmbeddedSound) {return play(EmbeddedSound, 1.0f, false);}
		
		//@desc		Creates a new sound object from a URL
		//@param	EmbeddedSound	The sound you want to play
		//@param	Volume			How loud to play it (0 to 1)
		//@param	Looped			Whether or not to loop this sound
		//@return	A FlxSound object
		static public FlxSound stream(String URL,float Volume,boolean Looped)
		{
			int i;
			int sl = sounds.size();
			for(i = 0; i < sl; i++)
				if(!sounds.get(i).active)
					break;
			if(sounds.get(i) == null)
				sounds.set(i, new FlxSound());
			FlxSound s = sounds.get(i);
			s.loadStream(URL,Looped);
			s.setVolume(Volume);
			s.play();
			return s;
		}
		
		static public FlxSound stream(String URL) {return stream(URL, 1.0f, false);}
		
		//@desc		Mutes the sound
		//@param	SoundOff	Whether the sound should be off or on
		static public void setMute(boolean Mute)
		{
			_mute = Mute;
			changeSounds();
		}
		
		//@desc		Check to see if the game is muted
		//@return	Whether the game is muted
		static public boolean getMute()
		{
			return _mute;
		}
		
		//@desc		Get a number we can multiply into a sound transform
		//@return	An unsigned integer - 0 if muted, 1 if not muted
		static public int getMuteValue()
		{
			if(_mute)
				return 0;
			else
				return 1;
		}
		
		//@desc		Change the volume of the game
		//@param	Volume		A number from 0 to 1
		static public void setVolume(float Volume)
		{
			_volume = Volume;
			if(_volume < 0)
				_volume = 0;
			else if(_volume > 1)
				_volume = 1;
			changeSounds();
		}
		
		//@desc		Find out how loud the game is currently
		//@param	A number from 0 to 1
		static public float getVolume() { return _volume; }
		
		//@desc		Called by FlxGame on state changes to stop and destroy sounds
		static void destroySounds(boolean ForceDestroy)
		{
			if(sounds == null)
				return;
			if((music != null) && (ForceDestroy || !music.survive))
				music.destroy();
			int sl = sounds.size();
			for(int i = 0; i < sl; i++)
				if(ForceDestroy || !((FlxSound)sounds.get(i)).survive)
					((FlxSound)sounds.get(i)).destroy();
		}
		static void destroySounds() {destroySounds(false);}
		
		//@desc		An internal function that adjust the volume levels and the music channel after a change
		static protected void changeSounds()
		{
			if((music != null) && music.active)
				music.updateTransform();
			int sl = sounds.size();
			for(int i = 0; i < sl; i++)
				if(((FlxSound)sounds.get(i)).active)
					((FlxSound)sounds.get(i)).updateTransform();
		}
		
		//@desc		Called by the game loop to make sure the sounds get updated each frame
		static void updateSounds()
		{
			if((music != null) && music.active)
				music.update();
			int sl = sounds.size();
			for(int i = 0; i < sl; i++)
				if(((FlxSound)sounds.get(i)).active)
					((FlxSound)sounds.get(i)).update();
		}
		
		static protected void pauseSounds()
		{
			if((music != null) && music.active)
				music.pause();
			int sl = sounds.size();
			for(int i = 0; i < sl; i++)
				if(((FlxSound)sounds.get(i)).active)
					((FlxSound)sounds.get(i)).pause();
		}
		
		static protected void playSounds()
		{
			if((music != null) && music.active)
				music.play();
			int sl = sounds.size();
			for(int i = 0; i < sl; i++)
				if(((FlxSound)sounds.get(i)).active)
					((FlxSound)sounds.get(i)).play();
		}
		
		//@desc		Generates a new BitmapData object (basically a colored square :P) and caches it
		//@param	Width	How wide the square should be
		//@param	Height	How high the square should be
		//@param	Color	What color the square should be
		//@return	This object is used during the sprite blitting process
		static public BitmapData createBitmap(int Width, int Height, int Color,boolean Unique)
		{
			String key = Width+"x"+Height+":"+Color;
			String ukey = key;
			//boolean gen = false;
			if(Unique && (_cache.get(ukey) != null))
			{
				int inc = 0;
				do { ukey = key + inc++;
				} while(_cache.get(ukey) != null);
				key = ukey;
			}
			_cache.put(key, new BitmapData(Width,Height,true,Color));
			return (BitmapData) _cache.get(key);
		}
		
		//@desc		Loads a bitmap from a file, caches it, and generates a horizontally flipped version if necessary
		//@param	Graphic		The image file that you want to load
		//@param	Reverse		Whether to generate a flipped version
		static public BitmapData addBitmap(int Graphic, boolean Reverse)
		{
			boolean needReverse = false;
			String key = "" + Graphic;
			if(_cache.get(key) == null)
			{
				_cache.put(key, FlxResourceManager.getImage(Graphic));
				if(Reverse) needReverse = true;
			}
			BitmapData pixels = (BitmapData) _cache.get(key);
			if(!needReverse && Reverse && (pixels.width == FlxResourceManager.getImage(Graphic).getBitmap().getWidth()))
				needReverse = true;
			if(needReverse)
			{
				BitmapData newPixels = new BitmapData(pixels.width,pixels.height,true,0x00000000);
				Matrix mtx = new Matrix();
				mtx.setScale(-1, 1);
				mtx.postTranslate(pixels.width, 0);
				newPixels.draw(pixels, mtx);
				pixels = newPixels;
			}
			return pixels;
		}
		
		static public BitmapData addBitmap(int Graphic) {return addBitmap(Graphic, false);}
		
		//@desc		Rotates a point in 2D space around another point by the given angle
		//@param	X		The X coordinate of the point you want to rotate
		//@param	Y		The Y coordinate of the point you want to rotate
		//@param	PivotX	The X coordinate of the point you want to rotate around
		//@param	PivotY	The Y coordinate of the point you want to rotate around
		//@param	Angle	Rotate the point by this many degrees
		//@return	A Flash Point object containing the coordinates of the rotated point
		static public Point rotatePoint(float X, float Y, float PivotX, float PivotY, float Angle)
		{
			float radians = (float) (-Angle / 180 * Math.PI);
			float dx = X-PivotX;
			float dy = PivotY-Y;
			return new Point((float)(PivotX + Math.cos(radians)*dx - Math.sin(radians)*dy), (float)(PivotY - (Math.sin(radians)*dx + Math.cos(radians)*dy)));
		};
		
		//@desc		Calculates the angle between a point and the origin (0,0)
		//@param	X		The X coordinate of the point
		//@param	Y		The Y coordinate of the point
		//@return	The angle in degrees
		static public float getAngle(float X, float Y)
		{
			return (float) (Math.atan2(Y,X) * 180 / Math.PI);
		};
		//@desc		Tells the camera subsystem what FlxCore object to follow
		//@param	Target		The object to follow
		//@param	Lerp		How much lag the camera should have (can help smooth out the camera movement)
		static public void follow(FlxCore Target, float Lerp)
		{
			followTarget = Target;
			followLerp = Lerp;
			scroll.x = _scrollTarget.x = (width>>1)-followTarget.x-(followTarget.width>>1);
			scroll.y = _scrollTarget.y = (height>>1)-followTarget.y-(followTarget.height>>1);
			doFollow();
		}
		static public void follow(FlxCore Target) {follow(Target, 1);}
		
		//@desc		Specify an additional camera component - the velocity-based "lead", or amount the camera should track in front of a sprite
		//@param	LeadX		Percentage of X velocity to add to the camera's motion
		//@param	LeadY		Percentage of Y velocity to add to the camera's motion
		static public void followAdjust(float LeadX, float LeadY)
		{
			followLead = new Point(LeadX,LeadY);
		}
		
		static public void followAdjust() {followAdjust(0, 0);}
		
		//@desc		Specify the boundaries of the level or where the camera is allowed to move
		//@param	MinX	The smallest X value of your level (usually 0)
		//@param	MinY	The smallest Y value of your level (usually 0)
		//@param	MaxX	The largest X value of your level (usually the level width)
		//@param	MaxY	The largest Y value of your level (usually the level height)
		static public void followBounds(int MinX, int MinY, int MaxX, int MaxY)
		{
			followMin = new Point(-MinX,-MinY);
			followMax = new Point(-MaxX+width,-MaxY+height);
			if(followMax.x > followMin.x)
				followMax.x = followMin.x;
			if(followMax.y > followMin.y)
				followMax.y = followMin.y;
			doFollow();
		}
		
		static public void followBounds() {followBounds(0, 0, 0, 0);}
		
		//@desc		A fairly stupid tween-like function that takes a starting velocity and some other factors and returns an altered velocity
		//@param	Velocity		Any component of velocity (e.g. 20)
		//@param	Acceleration	Rate at which the velocity is changing
		//@param	Drag			Really kind of a deceleration, this is how much the velocity changes if Acceleration is not set
		//@param	Max				An absolute value cap for the velocity
		static public float computeVelocity(float Velocity, float Acceleration, float Drag, float Max)
		{
			if(Acceleration != 0)
				Velocity += Acceleration*FlxG.elapsed;
			else if(Drag != 0)
			{
				float d = Drag*FlxG.elapsed;
				if(Velocity - d > 0)
					Velocity -= d;
				else if(Velocity + d < 0)
					Velocity += d;
				else
					Velocity = 0;
			}
			if((Velocity != 0) && (Max != 10000))
			{
				if(Velocity > Max)
					Velocity = Max;
				else if(Velocity < -Max)
					Velocity = -Max;
			}
			return Velocity;
		}
		
		static public float computeVelocity(float Velocity) {return computeVelocity(Velocity, 0, 0, 10000);}
		
		//@desc		Checks to see if a FlxCore overlaps any of the FlxCores in the array, and calls a function when they do
		//@param	Cores		An array of FlxCore objects
		//@param	Core		A FlxCore object
		//@param	Collide		A function that takes two sprites as parameters (first the one from ArrayList, then Sprite)
		static public void overlapArrayList(ArrayList<? extends FlxCore> Cores, FlxCore Core, FlxCollideListener Collide)
		{
			if((Core == null) || !Core.exists || Core.dead) return;
			FlxCore c;
			int l = Cores.size();
			for(int i = 0; i < l; i++)
			{
				c = Cores.get(i);
				if((c == Core) || (c == null) || !c.exists || c.dead) continue;
				if(c.overlaps(Core))
				{
					if(Collide != null)
						Collide.Collide(c,Core);
					else
					{
						c.kill();
						Core.kill();
					}
				}
			}
		}
		
		static public void overlapArrayList(ArrayList<? extends FlxCore>Cores,FlxCore Core) {overlapArrayList(Cores, Core, null);}
		
		//@desc		Checks to see if any FlxCore in ArrayList1 overlaps any FlxCore in ArrayList2, and calls Collide when they do
		//@param	Cores1		An array of FlxCore objects
		//@param	Cores2		Another array of FlxCore objects
		//@param	Collide		A function that takes two FlxCore objects as parameters (first the one from ArrayList1, then the one from ArrayList2)
		static public void overlapArrayLists(ArrayList<? extends FlxCore> Cores1, ArrayList<? extends FlxCore> Cores2, FlxCollideListener Collide)
		{
			int i;
			int j;
			FlxCore core1;
			FlxCore core2;
			int l1 = Cores1.size();
			int l2 = Cores2.size();
			if(Cores1 == Cores2)
			{
				for(i = 0; i < l1; i++)
				{
					core1 = Cores1.get(i);
					if((core1 == null) || !core1.exists || core1.dead) continue;
					for(j = i+1; j < l2; j++)
					{
						core2 = Cores2.get(j);
						if((core2 == null) || !core2.exists || core2.dead) continue;
						if(core1.overlaps(core2))
						{
							if(Collide != null)
								Collide.Collide(core1,core2);
							else
							{
								core1.kill();
								core2.kill();
							}
						}
					}
				}
			}
			else
			{
				for(i = 0; i < Cores1.size(); i++)
				{
					core1 = Cores1.get(i);
					if((core1 == null) || !core1.exists || core1.dead) continue;
					for(j = 0; j < Cores2.size(); j++)
					{
						core2 = Cores2.get(j);
						if((core1 == core2) || (core2 == null) || !core2.exists || core2.dead) continue;
						if(core1.overlaps(core2))
						{
							if(Collide != null)
								Collide.Collide(core1,core2);
							else
							{
								core1.kill();
								core2.kill();
							}
						}
					}
				}
			}
		}
		
		static public void overlapArrayLists(ArrayList<? extends FlxCore> Cores1, ArrayList<? extends FlxCore> Cores2) {overlapArrayLists(Cores1, Cores2, null);}
		
		//@desc		Collides a FlxCore against the FlxCores in the array 
		//@param	Cores		An array of FlxCore objects
		//@param	Core		A FlxCore object
		static public void collideArrayList(ArrayList<? extends FlxCore> Cores,FlxCore Core)
		{
			if((Core == null) || !Core.exists || Core.dead) return;
			FlxCore core;
			int l = Cores.size();
			for(int i = 0; i < l; i++)
			{
				core = Cores.get(i);
				if((core == Core) || (core == null) || !core.exists || core.dead) continue;
				core.collide(Core);
			}
		}
		
		//@desc		Collides a FlxCore against the FlxCores in the array on the X axis ONLY
		//@param	Cores		An array of FlxCore objects
		//@param	Core		A FlxCore object
		static public void collideArrayListX(ArrayList<? extends FlxCore> Cores,FlxCore Core)
		{
			if((Core == null) || !Core.exists || Core.dead) return;
			FlxCore core;
			int l = Cores.size();
			for(int i = 0; i < l; i++)
			{
				core = Cores.get(i);
				if((core == Core) || (core == null) || !core.exists || core.dead) continue;
				core.collideX(Core);
			}
		}
		
		//@desc		Collides a FlxSprite against the FlxCores in the array on the Y axis ONLY
		//@param	Cores		An array of FlxCore objects
		//@param	Core		A FlxSprite object
		static public void collideArrayListY(ArrayList<? extends FlxCore> Cores,FlxCore Core)
		{
			if((Core == null) || !Core.exists || Core.dead) return;
			FlxCore core;
			int l = Cores.size();
			for(int i = 0; i < l; i++)
			{
				core = Cores.get(i);
				if((core == Core) || (core == null) || !core.exists || core.dead) continue;
				core.collideY(Core);
			}
		}
		
		//@desc		Collides the first array of FlxCores against the second array of FlxCores
		//@param	ArrayList1		An array of FlxCore objects
		//@param	ArrayList2		An array of FlxCore objects
		static public void collideArrayLists(ArrayList<? extends FlxCore> Cores1, ArrayList<? extends FlxCore> Cores2)
		{
			int i;
			int j;
			FlxCore core1;
			FlxCore core2;
			int l1 = Cores1.size();
			int l2 = Cores2.size();
			if(Cores1 == Cores2)
			{
				for(i = 0; i < l1; i++)
				{
					core1 = Cores1.get(i);
					if((core1 == null) || !core1.exists || core1.dead) continue;
					for(j = i+1; j < l2; j++)
					{
						core2 = Cores2.get(j);
						if((core2 == null) || !core2.exists || core2.dead) continue;
						core1.collide(core2);
					}
				}
			}
			else
			{
				for(i = 0; i < l1; i++)
				{
					core1 = Cores1.get(i);
					if((core1 == null) || !core1.exists || core1.dead) continue;
					for(j = 0; j < l2; j++)
					{
						core2 = Cores2.get(j);
						if((core1 == core2) || (core2 == null) || !core2.exists || core2.dead) continue;
						core1.collide(core2);
					}
				}
			}
		}
		
		//@desc		Collides the first array of FlxCores against the second array of FlxCores on the X axis only
		//@param	ArrayList1		An array of FlxCore objects
		//@param	ArrayList2		An array of FlxCore objects
		static public void collideArrayListsX(ArrayList<? extends FlxCore> Cores1, ArrayList<? extends FlxCore> Cores2)
		{
			int i;
			int j;
			FlxCore core1;
			FlxCore core2;
			int l1 = Cores1.size();
			int l2 = Cores2.size();
			if(Cores1 == Cores2)
			{
				for(i = 0; i < l1; i++)
				{
					core1 = Cores1.get(i);
					if((core1 == null) || !core1.exists || core1.dead) continue;
					for(j = i+1; j < l2; j++)
					{
						core2 = Cores2.get(j);
						if((core2 == null) || !core2.exists || core2.dead) continue;
						core1.collideX(core2);
					}
				}
			}
			else
			{
				for(i = 0; i < l1; i++)
				{
					core1 = Cores1.get(i);
					if((core1 == null) || !core1.exists || core1.dead) continue;
					for(j = 0; j < l2; j++)
					{
						core2 = Cores2.get(j);
						if((core1 == core2) || (core2 == null) || !core2.exists || core2.dead) continue;
						core1.collideX(core2);
					}
				}
			}
		}
		
		//@desc		Collides the first array of FlxCores against the second array of FlxCores on the Y axis only
		//@param	ArrayList1		An array of FlxCore objects
		//@param	ArrayList2		An array of FlxCore objects
		static public void collideArrayListsY(ArrayList<? extends FlxCore> Cores1, ArrayList<? extends FlxCore> Cores2)
		{
			int i;
			int j;
			FlxCore core1;
			FlxCore core2;
			int l1 = Cores1.size();
			int l2 = Cores2.size();
			if(Cores1 == Cores2)
			{
				for(i = 0; i < l1; i++)
				{
					core1 = Cores1.get(i);
					if((core1 == null) || !core1.exists || core1.dead) continue;
					for(j = i+1; j < l2; j++)
					{
						core2 = Cores2.get(j);
						if((core2 == null) || !core2.exists || core2.dead) continue;
						core1.collideY(core2);
					}
				}
			}
			else
			{
				for(i = 0; i < l1; i++)
				{
					core1 = Cores1.get(i);
					if((core1 == null) || !core1.exists || core1.dead) continue;
					for(j = 0; j < l2; j++)
					{
						core2 = Cores2.get(j);
						if((core1 == core2) || (core2 == null) || !core2.exists || core2.dead) continue;
						core1.collideY(core2);
					}
				}
			}
		}
		
		//@desc		Switch from one FlxState to another
		//@param	State		The class name of the state you want (e.g. PlayState)
		static public void switchState(Class<? extends FlxState> State)
		{
			_game.switchState(State);
		}
		
		//@desc		Log data to the developer console
		//@param	Data		The data (in string format) that you wanted to write to the console
		static public void log(Object Data)
		{
			//_game._console.log(Data.toString());
		}
		
		//@desc		Shake the screen
		//@param	Intensity	Percentage of screen size representing the maximum distance that the screen can move during the 'quake'
		//@param	Duration	The length in seconds that the "quake" should last
		static public void quake(float Intensity,float Duration)
		{
			//_game._quake.reset(Intensity,Duration);
		}
		static public void quake(float Intensity) {quake(Intensity, 0.5f);}
		
		//@desc		Temporarily fill the screen with a certain color, then fade it out
		//@param	Color			The color you want to use
		//@param	Duration		How long it takes for the flash to fade
		//@param	FlashComplete	A function you want to run when the flash finishes
		//@param	Force			Force the effect to reset
		static public void flash(int Color, float Duration, FlxFlashListener FlashComplete, boolean Force)
		{
			//_game._flash.restart(Color,Duration,FlashComplete,Force);
		}
		static public void flash(int Color) {flash(Color, 1, null, false);}
		
		//@desc		Fade the screen out to this color
		//@param	Color			The color you want to use
		//@param	Duration		How long it should take to fade the screen out
		//@param	FadeComplete	A function you want to run when the fade finishes
		//@param	Force			Force the effect to reset
		static public void fade(int Color, float Duration, FlxFadeListener FadeComplete, boolean Force)
		{
			_game._fade.restart(Color,Duration,FadeComplete,Force);
		}
		static public void fade(int Color) {fade(Color, 1, null, false);}
		static public void fade(int Color, float Duration) {fade(Color, Duration, null, false);}
		static public void fade(int Color, float Duration, FlxFadeListener FadeComplete) {fade(Color, Duration, FadeComplete, false);}
		
		//@desc		Set the mouse cursor to some graphic file
		//@param	CursorGraphic	The image you want to use for the cursor
		static public void showCursor(String CursorGraphic)
		{
			/*if(CursorGraphic == null)
				_game._cursor = _game._buffer.addChild(new ImgDefaultCursor) as Bitmap;
			else
				_game._cursor = _game._buffer.addChild(new CursorGraphic) as Bitmap;*/
		}
		static public void showCursor() {showCursor(null);}
		
		//@desc		Hides the mouse cursor
		static public void hideCursor()
		{
			/*if(_game._cursor == null) return;
			_game._buffer.removeChild(_game._cursor);
			_game._cursor = null;*/
		}
		
		//@desc		Switch to a different web page
		static public void openURL(String URL)
		{
			//navigateToURL(new URLRequest(URL), "_blank");
		}
		//@desc		Tell the support panel to slide onto the screen
		//@param	Top		Whether to slide on from the top or the bottom
		static public void showSupportPanel(boolean Top)
		{
			//_game._panel.show(Top);
		}
		static public void showSupportPanel() {showSupportPanel(true);}
		
		//@desc		Conceals the support panel
		static public void hideSupportPanel()
		{
			//_game._panel.hide();
		}
		
		//@desc		Generate a pseudo-random number
		//@param	UseGlobalSeed		Whether or not to use the stored FlxG.seed value to calculate it
		//@return	A pseudo-random float object
		static public float random(boolean UseGlobalSeed)
		{
			if(UseGlobalSeed && !Float.isNaN(_seed))
			{
				float random = randomize(_seed);
				_seed = mutate(_seed,random);
				return random;
			}
			else
				return (float) Math.random();
		}
		static public float random() {return random(true);}
		
		//@desc		Generate a pseudo-random number
		//@param	Seed		The number to use to generate a new random value
		//@return	A pseudo-random float object
		static public float randomize(float Seed)
		{
			return ((69621 * (int)(Seed * 0x7FFFFFFF)) % 0x7FFFFFFF) / 0x7FFFFFFF;
		}
		
		//@desc		Mutate a seed, usually using the result of randomize()
		//@param	Seed		The number to mutate
		//@param	Mutator		The value to use in the mutation
		//@return	A predictably-altered version of the Seed
		static public float mutate(float Seed,float Mutator)
		{
			Seed += Mutator;
			if(Seed > 1) Seed -= (int)(Seed);
			return Seed;
		}
		//@desc		Fetches the original global FlxG.seed value the user set
		//@return	The original seed value
		static public float getSeed()
		{
			return _originalSeed;
		}
		
		//@desc		Allow the user to set the global seed
		//@param	Seed		The new number to use as a seed in random()
		static public void setSeed(float Seed)
		{
			_seed = Seed;
			_originalSeed = _seed;
		}
		//@desc		This function is only used by the FlxGame class to do important internal management stuff
		static void setGameData(FlxGame Game,int Width,int Height)
		{
			_game = Game;
			_cache = new Hashtable<String, BitmapData>();
			width = Width;
			height = Height;
			_mute = false;
			_volume = 0.5f;
			sounds = new ArrayList<FlxSound>();
			//mouse = new FlxMouse();
			keys = new FlxKeyboard();
			unfollow();
			FlxG.levels = new ArrayList<Object>();
			FlxG.scores = new ArrayList<Object>();
			level = 0;
			score = 0;
			_seed = Float.NaN;
			//kong = null;
			_pause = false;
		}
		//@desc		This function is only used by the FlxGame class to do important internal management stuff
		static void doFollow()
		{
			if(followTarget != null)
			{
				if(followTarget.exists && !followTarget.dead)
				{
					_scrollTarget.x = (width>>1)-followTarget.x-(followTarget.width>>1);
					_scrollTarget.y = (height>>1)-followTarget.y-(followTarget.height>>1);
					if((followLead != null) && (followTarget instanceof FlxSprite))
					{
						_scrollTarget.x -= ((FlxSprite)followTarget).velocity.x*followLead.x;
						_scrollTarget.y -= ((FlxSprite)followTarget).velocity.y*followLead.y;
					}
				}
				scroll.x += (_scrollTarget.x-scroll.x)*followLerp*FlxG.elapsed;
				scroll.y += (_scrollTarget.y-scroll.y)*followLerp*FlxG.elapsed;
				
				if(followMin != null)
				{
					if(scroll.x > followMin.x)
						scroll.x = followMin.x;
					if(scroll.y > followMin.y)
						scroll.y = followMin.y;
				}
				
				if(followMax != null)
				{
					if(scroll.x < followMax.x)
						scroll.x = followMax.x;
					if(scroll.y < followMax.y)
						scroll.y = followMax.y;
				}
			}
		}
		
		//@desc		This function is only used by the FlxGame class to do important internal management stuff
		static void unfollow()
		{
			followTarget = null;
			followLead = null;
			followLerp = 1;
			followMin = null;
			followMax = null;
			scroll = new Point();
			_scrollTarget = new Point();
		}
		
		//@desc		This function is only used by the FlxGame class to do important internal management stuff
		static void updateInput()
		{
			keys.update();
			//mouse.update(state.mouseX-scroll.x,state.mouseY-scroll.y);
		}
	}

