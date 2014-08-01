package org.flixel.data;

import java.util.ArrayList;
import org.flixel.*;

//@desc		This automates the color-rotation effect on the 'f' logo during game launch, not used in actual game code
public class FlxLogoPixel extends FlxLayer
{
	private ArrayList<FlxSprite> _layers;
	private int _curLayer;
	
	public FlxLogoPixel(int xPos,int yPos,int pixelSize,int index,int finalColor, int Size)
	{
		super();
		
		//Build up the color layers
		_layers = new ArrayList<FlxSprite>();
		int[] colors = new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFF00FFFF };
				
		FlxSprite background = new FlxSprite(xPos, yPos, null, false, Size, Size, finalColor);
		_layers.add(add(background));
		
		
		for(int i = 0; i < colors.length; i++)
		{
			FlxSprite coloredBlock = new FlxSprite(xPos, yPos, null, false, Size, Size, colors[index]);
			_layers.add(add(coloredBlock));
			
			if(++index >= colors.length) index = 0;
		}
		
		_curLayer = _layers.size()-1;
	}
	
	public void update()
	{
		if(_curLayer == 0)
			return;
		if(_layers.get(_curLayer).getAlpha() >= 0.1)
			_layers.get(_curLayer).setAlpha(_layers.get(_curLayer).getAlpha() - 0.1f);
		else
		{
			_layers.get(_curLayer).setAlpha(0);
			_curLayer--;
		}		
	}
}
