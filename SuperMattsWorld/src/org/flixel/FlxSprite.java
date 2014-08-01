package org.flixel;

import java.util.ArrayList;	
import flash.display.BitmapData;
import flash.geom.ColorTransform;
import flash.geom.Matrix;
import flash.geom.Point;
import flash.geom.Rectangle;

import org.flixel.data.FlxAnim;

//@desc		The main "game object" class, handles basic physics and animation
public class FlxSprite extends FlxCore
{
static public final int LEFT = 0;
static public final int RIGHT = 1;
static public final int UP = 2;
static public final int DOWN = 3;
static protected final Point _pZero = new Point();
	
//@desc If you changed the size of your sprite object to shrink the bounding box, you might need to offset the new bounding box from the top-left corner of the sprite
public Point offset;
public Point velocity;
public Point acceleration;
//@desc	This isn't drag exactly, more like deceleration that is only applied when acceleration is not affecting the sprite
public Point drag;
public Point maxVelocity;
//@desc WARNING: rotating sprites decreases rendering performance for this sprite by a factor of 10x!
public float angle;
public float angularVelocity;
public float angularAcceleration;
public float angularDrag;
public float maxAngular;
//@desc	The origin of the sprite will default to its center - if you change this, the visuals and the collisions will likely be pretty out-of-sync!  Just a heads up.
public Point origin;
//@desc	If you want to do Asteroids style stuff, check out thrust (instead of directly accessing the object's velocity or acceleration)
public float thrust;
public float maxThrust;
public float health;
//@desc	Scale doesn't currently affect collisions automatically, you will need to adjust the width, height and offset manually.  WARNING: scaling sprites decreases rendering performance for this sprite by a factor of 10x!
public Point scale;
public String blend;
//@desc	Controls whether the object is smoothed when rotated, affects performance, off by default
public boolean antialiasing;

public boolean scaleAndRotateFromCentre = true;

//@desc	Whether the current animation has finished its first (or only) loop
public boolean finished;
protected ArrayList<FlxAnim> _animations;
protected boolean _flipped;
protected FlxAnim _curAnim;
protected int _curFrame;
protected int _caf;
protected float _frameTimer;
protected FlxAnimationListener _callback;
protected int _facing;

//helpers
protected int _bw;
protected int _bh;
protected Rectangle _r;
protected Rectangle _r2;
protected Point _p;
protected BitmapData _pixels;
protected BitmapData _pixelsReverse;
protected BitmapData _framePixels;
protected float _alpha;
protected int _color;
protected ColorTransform _ct;
protected Matrix _mtx;

public FlxSprite(int X,int Y, Integer SimpleGraphic, boolean Animated, int Width, int Height, int Color)
{
	super();
	constructor(X, Y, SimpleGraphic, Animated, Width, Height, Color);		
}

public FlxSprite(int X,int Y, Integer SimpleGraphic, boolean Animated, int Width, int Height)
{
	super();
	constructor(X, Y, SimpleGraphic, Animated, Width, Height, 0xFFFFFFFF);		
}

//@desc		Constructor
//@param	X				The initial X position of the sprite
//@param	Y				The initial Y position of the sprite
//@param	SimpleGraphic	The graphic you want to display (OPTIONAL - for simple stuff only, do NOT use for animated images!)
public FlxSprite(int X,int Y, Integer SimpleGraphic, boolean Animated)
{
	super();
	constructor(X, Y, SimpleGraphic, Animated, 8, 8, 0xFFFFFFFF);		
}

public FlxSprite(int X,int Y, Integer SimpleGraphic)
{
	super();
	constructor(X, Y, SimpleGraphic, false, 8, 8, 0xFFFFFFFF);		
}

public FlxSprite(int X, int Y)
{
	super();
	constructor(X, Y, null, false, 8, 8, 0xFFFFFFFF);		
}

public FlxSprite()
{
	super();
	constructor(0, 0, null, false, 8, 8, 0xFFFFFFFF);		
}

protected void constructor(int X, int Y, Integer SimpleGraphic, boolean Animated, int Width, int Height, int Color)
{
	last.x = x = X;
	last.y = y = Y;
	_p = new Point();
	_r = new Rectangle();
	_r2 = new Rectangle();
	origin = new Point();
	
	_flipped = false;
	
	if(SimpleGraphic == null)
		createGraphic(Width, Height, Color);
	else
		loadGraphic(SimpleGraphic, Animated, true);
	offset = new Point();
	
	velocity = new Point();
	acceleration = new Point();
	drag = new Point();
	maxVelocity = new Point(10000,10000);
	
	angle = 0;
	angularVelocity = 0;
	angularAcceleration = 0;
	angularDrag = 0;
	maxAngular = 10000;
	
	thrust = 0;
	
	scale = new Point(1,1);
	_alpha = 1;
	_color = 0x00ffffff;
	blend = null;
	antialiasing = false;	
	finished = false;
	_facing = RIGHT;
	_animations = new ArrayList<FlxAnim>();
	
	_curAnim = null;
	_curFrame = 0;
	_caf = 0;
	_frameTimer = 0;
	_mtx = new Matrix();
	health = 1;
	_callback = null;
}

//@desc		Load an image from an embedded graphic file
//@param	Graphic		The image you want to use
//@param	Animated	Whether the Graphic parameter is a single sprite or a row of sprites
//@param	Reverse		Whether you need this class to generate horizontally flipped versions of the animation frames
//@param	Width		OPTIONAL - Specify the width of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets)
//@param	Height		OPTIONAL - Specify the height of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets)
//@param	Unique		Whether the graphic should be a unique instance in the graphics cache
//@return	This FlxSprite instance (nice for chaining stuff together, if you're into that)
public FlxSprite loadGraphic(int Graphic,boolean Animated,boolean Reverse,int Width,int Height,boolean Unique)
{
	_pixels = FlxG.addBitmap(Graphic,false);
	if (Reverse) _pixelsReverse = FlxG.addBitmap(Graphic,true);
	
	_flipped = Reverse;
	if(Width == 0)
	{
		if(Animated)
			Width = _pixels.height;
		else
			Width = _pixels.width;
	}
	width = _bw = Width;
	if(Height == 0)
	{
		if(Animated)
			Height = width;
		else
			Height = _pixels.height;
	}
	height = _bh = Height;
	resetHelpers();
	return this;
}

public FlxSprite loadGraphic(int Graphic) {return loadGraphic(Graphic, false, false, 0, 0, false);}
public FlxSprite loadGraphic(int Graphic, boolean Animated) {return loadGraphic(Graphic, Animated, false, 0, 0, false);}
public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse) {return loadGraphic(Graphic, Animated, Reverse, 0, 0, false);}

//@desc		This function creates a flat colored square image dynamically
//@param	Width		The width of the sprite you want to generate 
//@param	Height		The height of the sprite you want to generate
//@param	Color		Specifies the color of the generated block
//@param	Unique		Whether the graphic should be a unique instance in the graphics cache
//@return	This FlxSprite instance (nice for chaining stuff together, if you're into that)
public FlxSprite createGraphic(int Width,int Height,int Color,boolean Unique)
{
	_pixels = FlxG.createBitmap(Width,Height,Color,Unique);
	width = _bw = _pixels.width;
	height = _bh = _pixels.height;
	resetHelpers();
	return this;
}

public FlxSprite createGraphic(int Width,int Height,int Color) {return createGraphic(Width, Height,Color, false);}
public FlxSprite createGraphic(int Width,int Height) {return createGraphic(Width, Height,0xFFFFFFFF, false);}

//@desc		This function allows you to set the bitmap data directly, instead of loading it through FlxG
//@param	Pixels		A flash BitmapData object containing preloaded graphic information
public void setPixels(BitmapData Pixels)
{
	_pixels = Pixels;
	width = _bw = _pixels.width;
	height = _bh = _pixels.height;
	resetHelpers();
}

//@desc		This function retrieves the reference bitmap data that powers this graphic
//@return	A flash BitmapData object
public BitmapData getPixels()
{
	return _pixels;
}

//@desc		Just resets some important background variables for sprite display
protected void resetHelpers()
{
	_r.setX(0);
	_r.setY(0);
	_r.setWidth(_bw);
	_r.setHeight(_bh);
	_r2.setX(0);
	_r2.setY(0);
	_r2.setWidth(_pixels.width);
	_r2.setHeight(_pixels.height);
	if((_framePixels == null) || (_framePixels.width != width) || (_framePixels.height != height))
		_framePixels = new BitmapData(width,height);
	origin.x = _bw/2.0f;
	origin.y = _bh/2.0f;
	_framePixels.copyPixels(_pixels,_r,_pZero);
}

//@desc		Called by game loop, handles animation and physics
public void update()
{
	super.update();
	
	if(!active) return;
	
	//animation
	if((_curAnim != null) && (_curAnim.delay > 0) && (_curAnim.looped || !finished))
	{
		_frameTimer += FlxG.elapsed;
		if(_frameTimer > _curAnim.delay)
		{
			_frameTimer -= _curAnim.delay;
			if(_curFrame == _curAnim.frames.size()-1)
			{
				if(_curAnim.looped) _curFrame = 0;
				finished = true;
			}
			else
				_curFrame++;
			_caf = _curAnim.frames.get(_curFrame).intValue();
			calcFrame();
		}
	}
	
	//motion + physics
	angle += (angularVelocity = FlxG.computeVelocity(angularVelocity,angularAcceleration,angularDrag,maxAngular))*FlxG.elapsed;
	Point thrustComponents;
	if(thrust != 0)
	{
		thrustComponents = FlxG.rotatePoint(-thrust,0,0,0,angle);
		Point maxComponents = FlxG.rotatePoint(-maxThrust,0,0,0,angle);
		float max = Math.abs(maxComponents.x);
		if(max > Math.abs(maxComponents.y))
			maxComponents.y = max;
		else
			max = Math.abs(maxComponents.y);
		maxVelocity.x = Math.abs(max);
		maxVelocity.y = Math.abs(max);
	}
	else
		thrustComponents = _pZero;
	x += (velocity.x = FlxG.computeVelocity(velocity.x,acceleration.x+thrustComponents.x,drag.x,maxVelocity.x))*FlxG.elapsed;
	y += (velocity.y = FlxG.computeVelocity(velocity.y,acceleration.y+thrustComponents.y,drag.y,maxVelocity.y))*FlxG.elapsed;
}

//@desc		Called by game loop, blits current frame of animation to the screen (and handles rotation)
public void render()
{
	if(!visible || !this.onScreen())
		return;
	getScreenXY(_p);
	
	//Simple render
	if((angle == 0) && (scale.x == 1) && (scale.y == 1) && (blend == null))
	{
		FlxG.buffer.copyPixels(_framePixels,_r,_p,null,null,true);
		return;
	}
	
	Point matrixOrigin = new Point();
	if (scaleAndRotateFromCentre)
	{
		matrixOrigin.x = origin.x;
		matrixOrigin.y = matrixOrigin.y;
	}	
	
	//Advanced render
	_mtx.identity();
	_mtx.translate(-matrixOrigin.x, -matrixOrigin.y);
	_mtx.scale(scale.x,scale.y);
	if(angle != 0) _mtx.rotate((float) (Math.PI * 2 * (angle / 360)));
	_mtx.translate(_p.x + matrixOrigin.x, _p.y + matrixOrigin.y);
	FlxG.buffer.draw(_framePixels,_mtx,null,blend,null,antialiasing);
}

//@desc		Checks to see if a point in 2D space overlaps this FlxCore object
//@param	X			The X coordinate of the point
//@param	Y			The Y coordinate of the point
//@param	PerPixel	Whether or not to use per pixel collision checking
//@return	Whether or not the point overlaps this object
public boolean overlapsPoint(float X, float Y, boolean PerPixel)
{
	float tx = x;
	float ty = y;
	if((scrollFactor.x != 1) || (scrollFactor.y != 1))
	{
		tx -= Math.floor(FlxG.scroll.x*(1-scrollFactor.x));
		ty -= Math.floor(FlxG.scroll.y*(1-scrollFactor.y));
	}
	if(PerPixel)
		return _framePixels.hitTest(new Point(0,0),0xFF,new Point(X-tx,Y-ty));
	else if((X <= tx) || (X >= tx+width) || (Y <= ty) || (Y >= ty+height))
		return false;
	return true;
}

public boolean overlapsPoint(float X,float Y) {return overlapsPoint(X, Y, false);}

//@desc		Called when this object collides with a FlxBlock on one of its sides
//@return	Whether you wish the FlxBlock to collide with it or not
public boolean hitWall(FlxCore Contact) { velocity.x = 0; return true; }

//@desc		Called when this object collides with the top of a FlxBlock
//@return	Whether you wish the FlxBlock to collide with it or not
public boolean hitFloor(FlxCore Contact) { velocity.y = 0; return true; }

//@desc		Called when this object collides with the bottom of a FlxBlock
//@return	Whether you wish the FlxBlock to collide with it or not
public boolean hitCeiling(FlxCore Contact) { velocity.y = 0; return true; }

//@desc		Call this function to "damage" (or give health bonus) to this sprite
//@param	Damage		How much health to take away (use a negative number to give a health bonus)
public void hurt(float Damage)
{
	if((health -= Damage) <= 0)
		kill();
}

//@desc		Called if/when this sprite is launched by a FlxEmitter
public void onEmit() { }

//@desc		Adds a new animation to the sprite
//@param	Name		What this animation should be called (e.g. "run")
//@param	Frames		An array of numbers indicating what frames to play in what order (e.g. 1, 2, 3)
//@param	FrameRate	The speed in frames per second that the animation should play at (e.g. 40 fps)
//@param	Looped		Whether or not the animation is looped or just plays once
public void addAnimation(String Name, ArrayList<Integer> Frames, float FrameRate, boolean Looped)
{
	_animations.add(new FlxAnim(Name,Frames,FrameRate,Looped));
}
public void addAnimation(String Name, ArrayList<Integer> Frames, float FrameRate) {addAnimation(Name, Frames, FrameRate, true);}
public void addAnimation(String Name, ArrayList<Integer> Frames) {addAnimation(Name, Frames, 0, true);}

//@desc		Pass in a function to be called whenever this sprite's animation changes
//@param	AnimationCallback		A function that has 3 parameters: a string name, a int frame number, and a int frame index
public void addAnimationCallback(FlxAnimationListener AnimationCallback)
{
	_callback = AnimationCallback;
}

//@desc		Plays an existing animation (e.g. "run") - if you call an animation that is already playing it will be ignored
//@param	AnimName	The string name of the animation you want to play
//@param	Force		Whether to force the animation to restart
public void play(String AnimName,boolean Force)
{
	if(!Force && (_curAnim != null) && (AnimName == _curAnim.name)) return;
	_curFrame = 0;
	_caf = 0;
	_frameTimer = 0;
	int al = _animations.size();
	for(int i = 0; i < al; i++)
	{
		if(((FlxAnim)_animations.get(i)).name.equals(AnimName))
		{
			finished = false;
			_curAnim = (FlxAnim) _animations.get(i);
			Integer frame = _curAnim.frames.get(_curFrame);
			_caf = frame.intValue();
			calcFrame();
			return;
		}
	}
}

public void play(String AnimName) {play(AnimName, false);}

//@desc		Tell the sprite which way to face (you can just set 'facing' but this function also updates the animation instantly)
//@param	Direction		See static const members RIGHT, LEFT, UP, and DOWN	
public void setFacing(int Direction)
{
	boolean c = _facing != Direction;
	_facing = Direction;
	if(c) calcFrame();
}

//@desc		Get the direction the sprite is facing
//@return	True means facing right, False means facing left (see static const members RIGHT and LEFT)
public int getFacing()
{
	return _facing;
}

//@desc		Tell the sprite to change to a random frame of animation (useful for instantiating particles or other weird things)
public void randomFrame()
{
	_curAnim = null;
	_caf = (int) (FlxG.random()*(_pixels.width/_bw));
	calcFrame();
}

//@desc		Tell the sprite to change to a specific frame of animation (useful for instantiating particles)
//@param	Frame	The frame you want to display
public void specificFrame(int Frame)
{
	_curAnim = null;
	_caf = Frame;
	calcFrame();
}

//@desc		Call this function to figure out the post-scrolling "screen" position of the object
//@param	P	Takes a Flash Point object and assigns the post-scrolled X and Y values of this object to it
public void getScreenXY(Point P)
{
	P.x = (float) (Math.floor(x-offset.x)+Math.floor(FlxG.scroll.x*scrollFactor.x));
	P.y = (float) (Math.floor(y-offset.y)+Math.floor(FlxG.scroll.y*scrollFactor.y));
}

//@desc		Internal function to update the current animation frame
protected void calcFrame()
{
	int rx = _caf*_bw;
	int ry = 0;
	//Handle sprite sheets
	int w = _pixels.width;
	if(rx >= w)
	{
		ry = (int)(rx/w)*_bh;
		rx %= w;
	}
	
	//handle reversed sprites
	boolean useReverse = _flipped && (_facing == LEFT);
	BitmapData source = useReverse?_pixelsReverse:_pixels;
	
	if(useReverse)
		rx = (source.width)-rx-_bw;
	
	//Update display bitmap
	_r.setX(rx);
	_r.setY(ry);
	_framePixels.clearBitmap();
	_framePixels.copyPixels(source, _r, _pZero);
	_r.setX(0); _r.setY(0);
	if(_ct != null) _framePixels.colorTransform(_r,_ct);
	if(_callback != null) _callback.animationChanged(_curAnim.name,_curFrame,_caf);
}

//@desc		The setter for alpha
//@param	Alpha	The new opacity value of the sprite (between 0 and 1)
public void setAlpha(float Alpha)
{
	if(Alpha > 1) Alpha = 1;
	if(Alpha < 0) Alpha = 0;
	if(Alpha == _alpha) return;
	_alpha = Alpha;
	if((_alpha != 1) || (_color != 0x00ffffff)) _ct = new ColorTransform((float)(_color>>16)/255,(float)(_color>>8&0xff)/255,(float)(_color&0xff)/255,_alpha);
	else _ct = null;
	calcFrame();
}

//@desc		The getter for alpha
//@return	The value of this sprite's opacity
public float getAlpha()
{
	return _alpha;
}

//@desc		The setter for color - tints the whole sprite this color (similar to Photoshop multiply)
//@param	Color	The new color value of the sprite (0xRRGGBB) - ignores alpha
public void setColor(int Color)
{
	Color &= 0x00ffffff;
	if(_color == Color) return;
	_color = Color;
	if((_alpha != 1) || (_color != 0x00ffffff)) _ct = new ColorTransform((float)(_color>>16)/255,(float)(_color>>8&0xff)/255,(float)(_color&0xff)/255,_alpha);
	else _ct = null;
	calcFrame();
}

//@desc		The getter for color - tints the whole sprite this color (similar to Photoshop multiply)
//@return	The color value of the sprite (0xRRGGBB) - ignores alpha
public int getColor()
{
	return _color;
}

//@desc		This function draws or stamps one FlxSprite onto another (not intended to replace render()!)
//@param	Brush		The image you want to use as a brush or stamp or pen or whatever
//@param	X			The X coordinate of the brush's top left corner on this sprite
//@param	Y			They Y coordinate of the brush's top left corner on this sprite
public void draw(FlxSprite Brush,int X,int Y)
{
	BitmapData b = Brush._framePixels;
	
	//Simple draw
	if((Brush.angle == 0) && (Brush.scale.x == 1) && (Brush.scale.y == 1) && (Brush.blend == null))
	{
		_p.x = X;
		_p.y = Y;
		_r2.setWidth(b.width);
		_r2.setHeight(b.height);
		_pixels.copyPixels(b,_r2,_p,null,null,true);
		_r2.setWidth(_pixels.width);
		_r2.setHeight(_pixels.height);
		calcFrame();
		return;
	}
	//Advanced draw
	_mtx.identity();
	_mtx.translate(-Brush.origin.x,-Brush.origin.y);
	_mtx.scale(Brush.scale.x,Brush.scale.y);
	if(Brush.angle != 0) _mtx.rotate((float) (Math.PI * 2 * (Brush.angle / 360)));
	_mtx.translate(X+Brush.origin.x,Y+Brush.origin.y);
	_pixels.draw(b,_mtx,null,Brush.blend,null,Brush.antialiasing);
	calcFrame();
}

public void draw(FlxSprite Brush) {draw(Brush, 0, 0);}

//@desc		Fills this sprite's graphic with a specific color
//@param	Color		The color with which to fill the graphic
public void fill(int Color)
{
	_pixels.fillRect(_r2,Color);
	calcFrame();
}

//@desc		Internal function, currently only used to quickly update FlxState.screen for post-processing
//@param	Pixels		The flash BitmapData object you want to point at
	void unsafeBind(BitmapData Pixels)
	{
		_pixels = _framePixels = Pixels;
	}
}
