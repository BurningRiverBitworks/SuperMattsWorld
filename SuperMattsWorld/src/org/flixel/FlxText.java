package org.flixel;

	import flash.display.BitmapData;
	import flash.geom.Rectangle;
	import android.graphics.Paint.*;
	import android.graphics.*;
	
	//@desc		A basic text display class, can do some fun stuff though like flicker and rotate
	public class FlxText extends FlxSprite
	{
		protected static final int TEXT_BOUNDRY = 2;
		protected String _text;
		protected int _width; 
		protected boolean _regen;
		protected int _color;
		protected int _textSize;
		protected Align _align;
		protected Typeface _font;
		
		//@desc		Constructor
		//@param	X		The X position of the text
		//@param	Y		The Y position of the text
		//@param	Width	The width of the text object (height is determined automatically)
		//@param	Text	The actual text you would like to display initially
		public FlxText(int X, int Y, int Width, String Text)
		{
			super(X, Y);
			constructor(X, Y, Width, Text);
		}
		
		public FlxText(int X, int Y, int Width)
		{
			super(X, Y);
			constructor(X, Y, Width, null);
		}
		
		protected void constructor(int X, int Y, int Width, String Text)
		{
			if(Text == null)
				Text = "";
			_text = Text;
			_color = 0xFFFFFFFF;
			_textSize = 10;
			_align = Align.LEFT;
			_regen = true;
			_font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
			createGraphic(Width, 1);
			calcFrame();
		}
		
		//@desc		You can use this if you have a lot of text parameters to set instead of the individual properties
		//@param	Font		The name of the font face for the text display
		//@param	Size		The size of the font (in pixels essentially)
		//@param	Color		The color of the text in traditional flash 0xAARRGGBB format
		//@param	Alignment	A string representing the desired alignment ("left,"right" or "center")
		//@return	This FlxText instance (nice for chaining stuff together, if you're into that)
		public FlxText setFormat(Typeface Font, int Size, int Color, Align Alignment)
		{
			_font = Font;
			_color = 0xFFFFFFFF;
			_textSize = Size;
			_align = Alignment;
			_regen = true;
			calcFrame();
			return this;
		}
		
		//@desc		Changes the text being displayed
		//@param	Text	The new string you want to display
		public void setText(String Text)
		{
			_text = Text;
			_regen = true;
			calcFrame();
		}
		
		//@desc		Getter to retrieve the text being displayed
		//@return	The text string being displayed
		public String getText()
		{
			return _text;
		}
		
		//@desc		Changes the size of the text being displayed
		//@param	Size	The new font size you want to use
		public void setSize(int Size)
		{
			_textSize = Size;
			_regen = true;
			calcFrame();
		}
		
		//@desc		Getter to retrieve the size of the text
		//@return	The size of the font
		public int getSize()
		{
			return _textSize;
		}
		
		//@desc		Sets the font face being used
		//@param	Font	The name of the font face
		public void setFont(Typeface Font)
		{
			_font = Font;
			_regen = true;
			calcFrame();
		}
		
		//@desc		Gets the name of the font face being used
		//@return	The name of the font face
		public Typeface getFont()
		{
			return _font;
		}
		
		//@desc		Sets the alignment of the text being displayed
		//@param	A string indicating the desired alignment - acceptable values are "left", "right" and "center"
		public void setAlignment(Align Alignment)
		{
			_align = Alignment;
			calcFrame();
		}
		
		//@desc		Gets the alignment of the text being displayed
		//@return	A string indicating the current alignment
		public Align getAlignment()
		{
			return _align;
		}
		
		//@desc		Internal function to update the current animation frame
		protected void calcFrame()
		{
			Paint paint = new Paint();
			paint.setTextAlign(_align);
			paint.setColor(_color);
			paint.setTextSize(_textSize);
			paint.setTypeface(_font);
			FontMetricsInt fm = paint.getFontMetricsInt();
			_framePixels.fillRect(_r,0);
			
			//Just leave if there's no text to render
			if(_text.length() <= 0)				
				return;
			
			if(_regen)
			{
				//Need to generate a new buffer to store the text graphic
				int nl = _text.split("\n").length;
				height = 0;
				for(int i = 0; i < nl; i++)				
					height += fm.bottom - fm.top;
				height += TEXT_BOUNDRY * 2; //account for 2px gutter on top and bottom
				_framePixels = new BitmapData(width,height,true,0);
				_bh = height;
				_r = new Rectangle(0,0,width,height);
				_framePixels.fillRect(_r,0);
				_regen = false;
			}
						
			if (_align == Align.CENTER)
				_framePixels.getCanvas().drawText(_text, (width - TEXT_BOUNDRY * 2) / 2, height - TEXT_BOUNDRY, paint);
			else if (_align == Align.LEFT)
				_framePixels.getCanvas().drawText(_text, TEXT_BOUNDRY, height - TEXT_BOUNDRY, paint);
			else
				_framePixels.getCanvas().drawText(_text, width - TEXT_BOUNDRY, height - TEXT_BOUNDRY, paint);
		}

	}
