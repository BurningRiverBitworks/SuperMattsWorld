package org.flixel;

import java.util.ArrayList;

	//@desc		This is an organizational class that can update and render a bunch of FlxCore objects
	public class FlxLayer extends FlxCore
	{
		protected ArrayList<FlxCore> _children;
		//@desc		Constructor		
		public FlxLayer()
		{
			_children = new ArrayList<FlxCore>();
		}
		
		//@desc		Adds a new FlxCore subclass (FlxSprite, FlxBlock, etc) to the list of children
		//@param	Core			The object you want to add
		//@param	ShareScroll		Whether or not this FlxCore should sync up with this layer's scrollFactor
		public <T extends FlxCore> T add(T Core, boolean ShareScroll)
		{
			_children.add(Core);
			Core.x += x;
			Core.y += y;
			if(ShareScroll)
				Core.scrollFactor = scrollFactor;
			return Core;
		}
		
		public <T extends FlxCore> T add(T Core)
		{
			return add(Core, false);
		}
		
		//@desc		Automatically goes through and calls update on everything you added, override this function to handle custom input and perform collisions
		public void update()
		{
			super.update();
			float mx = 0;
			float my = 0;
			boolean moved = false;
			if((x != last.x) || (y != last.y))
			{
				moved = true;
				mx = x - last.x;
				my = y - last.y;
			}
			FlxCore c;
			int cl = _children.size();
			for(int i = 0; i < cl; i++)
			{
				c = (FlxCore)_children.get(i);
				if((c != null) && c.exists)
				{
					if(moved)
					{
						c.x += mx;
						c.y += my;
					}
					if(c.active)
						c.update();
				}
			}
		}
		
		//@desc		Automatically goes through and calls render on everything you added, override this loop to do crazy graphical stuffs I guess?
		public void render()
		{
			super.render();
			FlxCore c;
			int cl = _children.size();
			for(int i = 0; i < cl; i++)
			{
				c = (FlxCore)_children.get(i);
				if((c != null) && c.exists && c.visible) c.render();
			}
		}
		
		//@desc		Override this function to handle any deleting or "shutdown" type operations you might need (such as removing traditional Flash children like Sprite objects)
		public void destroy()
		{
			super.destroy();
			int cl = _children.size();
			for(int i = 0; i < cl; i++)
				((FlxCore)_children.get(i)).destroy();
			_children.clear();
		}
		
		//@desc		Returns the array of children
		public ArrayList<FlxCore> children() { return _children; }
	}
