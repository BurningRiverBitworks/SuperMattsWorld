package org.flixel;

//@desc		This is the basic game "state" object - e.g. in a simple game you might have a menu state and a play state
public class FlxState extends FlxCore
{
	//@desc		This variable holds the screen buffer, so you can draw to it directly if you want
	static public FlxSprite screen;
	//@desc		This variable indicates the "clear color" or default background color of the game
	static public int bgColor;
	protected FlxLayer _layer;
	
	//@desc		Constructor		
	public FlxState()
	{
		super();
		_layer = new FlxLayer();
		FlxG.state = this;
		if(screen == null)
		{
			screen = new FlxSprite();
			screen.createGraphic(FlxG.width,FlxG.height,0,true);
			screen.origin.x = screen.origin.y = 0;
			screen.antialiasing = true;
		}
	}
	
	//@desc		Adds a new FlxCore subclass (FlxSprite, FlxBlock, etc) to the game loop
	//@param	Core	The object you want to add to the game loop
	public <T extends FlxCore> T add(T Core)
	{
		return _layer.add(Core);
	}
	
	//@desc		Override this function to do special pre-processing FX on FlxG.buffer (like motion blur)
	public void preProcess()
	{
		screen.fill(bgColor);	//Default behavior - just overwrite buffer with background color
	}
	
	//@desc		Automatically goes through and calls update on everything you added to the game loop, override this function to handle custom input and perform collisions
	public void update()
	{
		_layer.update();
	}
	
	//@desc		Automatically goes through and calls render on everything you added to the game loop, override this loop to do crazy graphical stuffs I guess?
	public void render()
	{
		_layer.render();
	}
	
	//@desc		Override this function and use the 'screen' variable to do special post-processing FX (like light bloom)
	public void postProcess() { }
	
	//@desc		Override this function to handle any deleting or "shutdown" type operations you might need (such as removing traditional Flash children like Sprite objects)
	public void destroy() { _layer.destroy(); }
}

