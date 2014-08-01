package org.flixel;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.*;
import android.view.*;
import org.flixel.data.*;

import flash.display.BitmapData;
import flash.geom.Rectangle;

//@desc		FlxGame is the heart of all flixel games, and contains a bunch of basic game loops and things.  It is a long and sloppy file that you shouldn't have to worry about too much!
public class FlxGame extends FlxCore
{
	//[Embed(source="data/nokiafc22.ttf",fontFamily="system")] protected String junk;
	//[Embed(source="data/poweredby.png")] protected Class ImgPoweredBy;
	//[Embed(source="data/beep.mp3")] protected Class SndBeep;
	//[Embed(source="data/flixel.mp3")] protected Class SndFlixel;
	static final float MAX_ELAPSED = 0.0333f;
	
	static Class<? extends FlxState> initialGameState = null;
	
	//@desc		Whether or not to display the flixel logo on startup
	protected boolean showLogo;
	//@desc		Sets 0, -, and + to global volume and P to pause (off by default)
	protected boolean useDefaultHotKeys;
	//@desc		Displayed whenever the game is paused - can be overridden with whatever!
	protected FlxLayer pause;
	
	protected boolean gameRunning = true;
	
	//startup
	Class<? extends FlxState> _iState;
	boolean _created;
	
	//basic display stuff
	/*internal var _buffer:Sprite;
	internal var _bmpBack:Bitmap;
	internal var _bmpFront:Bitmap;*/
	BitmapData _buffer;
	Rectangle _r;
	boolean _flipped;
	int _zoom;
	int _gameXOffset;
	int _gameYOffset;
	String _frame;
	FlxState _curState;
	//Bitmap _cursor;
	
	//basic update stuff
	float _elapsed;
	long _total;
	boolean _paused;
	
	//Pause screen, sound tray, support panel, dev console, and special effects objects
	ArrayList<String> _helpStrings;
	//Sprite _soundTray;
	float _soundTrayTimer;
	//ArrayList _soundTrayBars;
	//FlxPanel _panel;
	//FlxConsole _console;
	FlxQuake _quake;
	FlxFlash _flash;
	FlxFade _fade;
	
	//@desc		Game object constructor - sets up the basic properties of your game
	//@param	GameSizeX		The width of your game in pixels (e.g. 320)
	//@param	GameSizeY		The height of your game in pixels (e.g. 240)
	//@param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState)
	//@param	Zoom			The level of zoom (e.g. 2 means all pixels are now rendered twice as big)
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, Context context, Class<? extends Object> Resource, int Zoom)
	{
		constructor(GameSizeX, GameSizeY, InitialState, context, Resource, Zoom);
	}
	
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, Context context, Class<? extends Object> Resource)
	{
		constructor(GameSizeX, GameSizeY, InitialState, context, Resource, 1);
	}
	
	protected void constructor(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, Context context, Class<? extends Object> Resource, int Zoom)
	{
		FlxResourceManager.context = context;
		FlxResourceManager.R = Resource;
		
		//flash.ui.Mouse.hide();
		
		_zoom = Zoom;
		FlxState.bgColor = 0xff000000;
		FlxG.setGameData(this,GameSizeX,GameSizeY);
		_elapsed = 0;
		_total = 0;
		pause = new FlxPause();
		_quake = new FlxQuake(_zoom);
		_flash = new FlxFlash();
		_fade = new FlxFade();
		
		_curState = null;
		_iState = InitialState;
		
		_helpStrings = new ArrayList<String>();
		_helpStrings.add("Button 1");
		_helpStrings.add("Button 2");
		_helpStrings.add("Mouse");
		_helpStrings.add("Move");
		//_panel = new FlxPanel();
		
		useDefaultHotKeys = true;
		
		_frame = null;
		_gameXOffset = 0;
		_gameYOffset = 0;
		
		_paused = false;
		_created = false;
		
		_buffer = new BitmapData(FlxG.width, FlxG.height);
		FlxG.buffer = _buffer;
		_created = true;		
		
		initialGameState = InitialState;
		
		FlxG.switchState(FlxLogoState.class);
	}
	
	//@desc		Adds a frame around your game for presentation purposes (see Canabalt, Gravity Hook)
	//@param	Frame			If you want you can add a little graphical frame to the outside edges of your game
	//@param	ScreenOffsetX	If you use a frame, you're probably going to want to scoot your game down to fit properly inside it
	//@param	ScreenOffsetY	These variables do exactly that :)
	//@return	This FlxGame instance (nice for chaining stuff together, if you're into that)
	protected FlxGame addFrame(String Frame,int ScreenOffsetX,int ScreenOffsetY)
	{
		_frame = Frame;
		_gameXOffset = ScreenOffsetX;
		_gameYOffset = ScreenOffsetY;
		return this;
	}
	
	public void shutdown(boolean forceSoundShutdown)
	{
		//_panel.hide();
		FlxG.unfollow();
		FlxG.keys.reset();
		FlxG.hideCursor();
		FlxG.destroySounds(forceSoundShutdown);
		_flash.restart(0,0);
		_fade.restart(0,0);
		_quake.reset(0);
		
		if(_curState != null)
		{
			_curState.destroy();
			_curState = null;
		}
	}
	
	public void shutdown() {shutdown(false);}
	
	//@desc		Switch from one FlxState to another
	//@param	State		The class name of the state you want (e.g. PlayState)
	void switchState(Class<? extends FlxState> State)
	{ 
		shutdown();
		
		FlxState newState = null;
		
		if (State != null)
		{
			try 
			{
				newState = (FlxState) State.newInstance();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			} 
			catch (InstantiationException e) 
			{
				e.printStackTrace();
			}
		}

		_curState = newState;
	}
	
	//@desc		Sets up the strings that are displayed on the left side of the pause game popup
	//@param	X		What to display next to the X button
	//@param	C		What to display next to the C button
	//@param	Mouse	What to display next to the mouse icon
	//@param	Arrows	What to display next to the arrows icon
	protected void help(String X,String C,String Mouse,String Arrows)
	{
		if(X != null)
			_helpStrings.set(0, X);
		if(C != null)
			_helpStrings.set(1, C);
		if(Mouse != null)
			_helpStrings.set(2, Mouse);
		if(Arrows != null)
			_helpStrings.set(3, Arrows);
	}
	
	protected void help() {help(null, null, null, null);}
	
	//@desc		This function is only used by the FlxGame class to do important management stuff
	public void onKeyUp(int Key)
	{
		if(Key == KeyEvent.KEYCODE_AT)
		{
			//_console.toggle();
			return;
		}
		if(useDefaultHotKeys)
		{
			switch(Key)
			{
				case KeyEvent.KEYCODE_0:
					FlxG.setMute(!FlxG.getMute());
					showSoundTray();
					return;
				case KeyEvent.KEYCODE_I:
					FlxG.setMute(false);
		    		FlxG.setVolume(FlxG.getVolume() - 0.1f);
		    		showSoundTray();
		    		return;
				case KeyEvent.KEYCODE_O:
					FlxG.setMute(false);
		    		FlxG.setVolume(FlxG.getVolume() + 0.1f);
		    		showSoundTray();
		    		return;
				case KeyEvent.KEYCODE_P:
					FlxG.setPause(!FlxG.getPause());
					return;
				case KeyEvent.KEYCODE_Q:
					gameRunning = false;
					return;
				default: break;
			}
		}
		FlxG.keys.handleKeyUp(Key);
	}
	
	//@desc		This function is only used by the FlxGame class to do important management stuff
	public void onKeyDown(int Key)
	{
		FlxG.keys.handleKeyDown(Key);
	}
	
	//@desc		This function is only used by the FlxGame class to do important management stuff
	/*protected void onMouseUp(MouseEvent event)
	{
		FlxG.mouse.handleMouseUp(event);
	}
	
	//@desc		This function is only used by the FlxGame class to do important management stuff
	protected void onMouseDown(MouseEvent event)
	{
		FlxG.mouse.handleMouseDown(event);
	}*/
	
	//@desc		This function is only used by the FlxGame class to do important management stuff
	public void onFocus()
	{
		//if(FlxG.getPause())
			//FlxG.setPause(false);
	}
	
	//@desc		This function is only used by the FlxGame class to do important management stuff
	public void onFocusLost()
	{
		if(!(FlxG.state instanceof FlxLogoState))
			FlxG.setPause(true);
	}
	
	//@desc		function to help with basic pause game functionality
	void unpauseGame()
	{
		//if(!_panel.visible) flash.ui.Mouse.hide();
		FlxG.resetInput();
		_paused = false;
		//stage.frameRate = 90;
	}
	
	//@desc		function to help with basic pause game functionality
	void pauseGame()
	{
		if((x != 0) || (y != 0))
		{
			x = 0;
			y = 0;
		}
		_paused = true;
	}
	
	public boolean onEnterFrame(Canvas canvas)
	{
		long now = System.currentTimeMillis();
		_elapsed = (now - _total) / 1000.0f;
		_total = now;
		FlxG.elapsed = _elapsed;
		if(FlxG.elapsed > MAX_ELAPSED)
			FlxG.elapsed = MAX_ELAPSED;
		
		FlxG.updateSounds();
		
		if (_paused)
		{
			pause.update();
			canvas.drawBitmap(
					FlxG.buffer.getBitmap(), 
					FlxG.buffer.getBitmapRect(), 
					canvas.getClipBounds(),
					null);		
			pause.render();
		}
		else
		{
			FlxG.buffer.fillRect(
				new Rectangle(0, 0, FlxG.buffer.getBitmap().getWidth(), FlxG.buffer.getBitmap().getHeight()), 
				0xFF000000);
			
			_curState.preProcess();
			
			//Update the camera and game state
			FlxG.doFollow();
			_curState.update();
			
			_flash.update();
			_fade.update();
			_quake.update();
			
			//Render game content, special fx, and overlays
			_curState.render();
			
			//_flash.render();
			_fade.render();
			
			//Post-processing hook
			_curState.postProcess();
			
			canvas.drawBitmap(
					FlxG.buffer.getBitmap(), 
					FlxG.buffer.getBitmapRect(), 
					canvas.getClipBounds(),
					null);				
		}
		
		return gameRunning;
	}
	
	//@desc		This is the main game loop, but only once creation and logo playback is finished
	/*protected void onEnterFrame(Event event)
	{
		int i;
		
		//Frame timing
		int t = getTimer();
		_elapsed = (t-_total)/1000;
		_total = t;
		FlxG.elapsed = _elapsed;
		if(FlxG.elapsed > MAX_ELAPSED)
			FlxG.elapsed = MAX_ELAPSED;
		
		if(_logoComplete)
		{
			//Animate flixel HUD elements
			_panel.update();
			_console.update();
			if(_soundTrayTimer > 0)
				_soundTrayTimer -= _elapsed;
			else if(_soundTray.y > -_soundTray.height)
			{
				_soundTray.y -= _elapsed*FlxG.height*2;
				if(_soundTray.y < -_soundTray.height)
					_soundTray.visible = false;
			}
			
			//State updating
			FlxG.updateInput();
			if(_cursor != null)
			{
				_cursor.x = FlxG.mouse.x+FlxG.scroll.x;
				_cursor.y = FlxG.mouse.y+FlxG.scroll.y;
			}
			FlxG.updateSounds();
			if(_paused)
			{
				pause.update();
				if(_flipped)
					FlxG.buffer.copyPixels(_bmpFront.bitmapData,_r,new Point(0,0));
				else
					FlxG.buffer.copyPixels(_bmpBack.bitmapData,_r,new Point(0,0));
				canvas.drawBitmap(FlxG.buffer.getBitmap(), 0, 0, null);
				pause.render();
			}
			else
			{
				//Clear video buffer
				if(_flipped)
					FlxG.buffer = _bmpFront.bitmapData;
				else
					FlxG.buffer = _bmpBack.bitmapData;
				FlxState.screen.unsafeBind(FlxG.buffer);
				
				_curState.preProcess();
				
				//Update the camera and game state
				FlxG.doFollow();
				_curState.update();
				
				//Update the various special effects
				_flash.update();
				_fade.update();
				_quake.update();
				_buffer.x = _quake.x;
				_buffer.y = _quake.y;
				
				//Render game content, special fx, and overlays
				_curState.render();
				_flash.render();
				_fade.render();
				_panel.render();
				
				//Post-processing hook
				_curState.postProcess();
				
				//Swap video buffers
				_bmpBack.visible = !(_bmpFront.visible = _flipped);
				_flipped = !_flipped;
			}
		}
		else if(_created)
		{
			if(!showLogo)
			{
				_logoComplete = true;
				FlxG.switchState(_iState);
			}
			else
			{
				if(_f == null)
				{
					Bitmap tmp;
					_f = new ArrayList();
					int scale = 1;
					if(FlxG.height > 200)
						scale = 2;
					int pixelSize = 32*scale;
					int top = FlxG.height*_zoom/2-pixelSize*2;
					int left = FlxG.width*_zoom/2-pixelSize;
					
					_f.push(addChild(new FlxLogoPixel(left+pixelSize,top,pixelSize,0,_fc)) as FlxLogoPixel);
					_f.push(addChild(new FlxLogoPixel(left,top+pixelSize,pixelSize,1,_fc)) as FlxLogoPixel);
					_f.push(addChild(new FlxLogoPixel(left,top+pixelSize*2,pixelSize,2,_fc)) as FlxLogoPixel);
					_f.push(addChild(new FlxLogoPixel(left+pixelSize,top+pixelSize*2,pixelSize,3,_fc)) as FlxLogoPixel);
					_f.push(addChild(new FlxLogoPixel(left,top+pixelSize*3,pixelSize,4,_fc)) as FlxLogoPixel);
					
					_poweredBy = new ImgPoweredBy;
					_poweredBy.scaleX = scale;
					_poweredBy.scaleY = scale;
					_poweredBy.x = FlxG.width*_zoom/2-_poweredBy.width/2;
					_poweredBy.y = top+pixelSize*4+16;
					ColorTransform ct = new ColorTransform();
					ct.color = _fc;
					_poweredBy.bitmapData.colorTransform(new Rectangle(0,0,_poweredBy.width,_poweredBy.height),ct);
					addChild(_poweredBy);
					
					_logoFade = addChild(new Bitmap(new BitmapData(FlxG.width*_zoom,FlxG.height*_zoom,true,0xFF000000))) as Bitmap;
					_logoFade.x = _gameXOffset*_zoom;
					_logoFade.y = _gameYOffset*_zoom;
					
					if(_fSound != null)
						FlxG.play(_fSound,0.35);
				}
				
				_logoTimer += _elapsed;
				for(i = 0; i < _f.length; i++)
					_f.get(i).update();
				if(_logoFade.alpha > 0)
					_logoFade.alpha -= _elapsed*0.5;
					
				if(_logoTimer > 2)
				{
					removeChild(_poweredBy);
					for(i = 0; i < _f.length; i++)
						removeChild(_f.get(i));
					_f.length = 0;
					removeChild(_logoFade);
					FlxG.switchState(_iState);
					_logoComplete = true;
				}
			}
		}
		else if(root != null)
		{
		
			//Set up the view window and double buffering
			stage.scaleMode = StageScaleMode.NO_SCALE;
            stage.align = StageAlign.TOP_LEFT;
            stage.frameRate = 90;
            _buffer = new Sprite();
            _buffer.scaleX = _zoom;
            _buffer.scaleY = _zoom;
            addChild(_buffer);
			_bmpBack = new Bitmap(new BitmapData(FlxG.width,FlxG.height,true,FlxState.bgColor));
			_bmpBack.x = _gameXOffset;
			_bmpBack.y = _gameYOffset;
			_buffer.addChild(_bmpBack);
			_bmpFront = new Bitmap(new BitmapData(_bmpBack.width,_bmpBack.height,true,FlxState.bgColor));
			_bmpFront.x = _bmpBack.x;
			_bmpFront.y = _bmpBack.y;
			_buffer.addChild(_bmpFront);
			_flipped = false;
			_r = new Rectangle(0,0,_bmpFront.width,_bmpFront.height);
			
			//Initialize game console
			_console = new FlxConsole(_gameXOffset,_gameYOffset,_zoom);
			addChild(_console);
			String vstring = FlxG.LIBRARY_NAME+" v"+FlxG.LIBRARY_MAJOR_VERSION+"."+FlxG.LIBRARY_MINOR_VERSION;
			if(FlxG.debug)
				vstring += " .get(debug)";
			else
				vstring += " .get(release)";
			String underline = "";
			for(i = 0; i < vstring.length+32; i++)
				underline += "-";
			FlxG.log(vstring);
			FlxG.log(underline);
			
			//Add basic input even listeners
			stage.addEventListener(KeyboardEvent.KEY_DOWN, onKeyDown);
			stage.addEventListener(KeyboardEvent.KEY_UP, onKeyUp);
			stage.addEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
			stage.addEventListener(MouseEvent.MOUSE_UP, onMouseUp);
			
			//Initialize the pause screen
			stage.addEventListener(Event.DEACTIVATE, onFocusLost);
			stage.addEventListener(Event.ACTIVATE, onFocus);
			
			//Sound Tray popup
			_soundTray = new Sprite();
			_soundTray.visible = false;
			_soundTray.scaleX = 2;
			_soundTray.scaleY = 2;
			tmp = new Bitmap(new BitmapData(80,30,true,0x7F000000));
			_soundTray.x = (_gameXOffset+FlxG.width/2)*_zoom-(tmp.width/2)*_soundTray.scaleX;
			_soundTray.addChild(tmp);
			
			TextField text = new TextField();
			text.width = tmp.width;
			text.height = tmp.height;
			text.multiline = true;
			text.wordWrap = true;
			text.selectable = false;
			text.embedFonts = true;
			text.antiAliasType = AntiAliasType.NORMAL;
			text.gridFitType = GridFitType.PIXEL;
			text.defaultTextFormat = new TextFormat("system",8,0xffffff,null,null,null,null,null,"center");;
			_soundTray.addChild(text);
			text.text = "VOLUME";
			text.y = 16;
			
			int bx = 10;
			int by = 14;
			_soundTrayBars = new ArrayList();
			for(i = 0; i < 10; i++)
			{
				tmp = new Bitmap(new BitmapData(4,i+1,false,0xffffff));
				tmp.x = bx;
				tmp.y = by;
				_soundTrayBars.push(_soundTray.addChild(tmp));
				bx += 6;
				by--;
			}
			addChild(_soundTray);
			//Initialize the decorative frame (optional)
			if(_frame != null)
			{
				Bitmap bmp = new _frame;
				bmp.scaleX = _zoom;
				bmp.scaleY = _zoom;
				addChild(bmp);
			}
			
			//All set!
			_created = true;
			_logoTimer = 0;
		}
	}*/
	
	//@desc		Makes the little volume tray slide out
	public void showSoundTray()
	{
		/*FlxG.play(SndBeep);
		_soundTrayTimer = 1;
		_soundTray.y = _gameYOffset*_zoom;
		_soundTray.visible = true;
		int gv = Math.round(FlxG.volume*10);
		if(FlxG.mute)
			gv = 0;
		for (int i = 0; i < _soundTrayBars.length; i++)
		{
			if(i < gv) _soundTrayBars.get(i).alpha = 1;
			else _soundTrayBars.get(i).alpha = 0.5;
		}*/
	}
	
	//@desc		Set up the support panel thingy with donation and aggregation info
	//@param	PayPalID		Your paypal username, usually your email address (leave it blank to disable donations)
	//@param	PayPalAmount	The default amount of the donation
	//@param	GameTitle		The text that you would like to appear in the aggregation services (usually just the name of your game)
	//@param	GameURL			The URL you would like people to use when trying to find your game
	protected void setupSupportPanel(String PayPalID,float PayPalAmount,String GameTitle,String GameURL,String Caption)
	{
		//_panel.init(PayPalID,PayPalAmount,GameTitle,GameURL,Caption);
	}
}
