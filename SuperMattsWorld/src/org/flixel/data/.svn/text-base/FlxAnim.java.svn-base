package org.flixel.data;

import java.util.ArrayList;

	//@desc		Just a helper structure for the FlxSprite animation system
	public class FlxAnim
	{
		public String name;
		public float delay;
		public ArrayList<Integer> frames;
		public boolean looped;
		
		//@desc		Constructor
		//@param	Name		What this animation should be called (e.g. "run")
		//@param	Frames		An array of numbers indicating what frames to play in what order (e.g. 1, 2, 3)
		//@param	FrameRate	The speed in frames per second that the animation should play at (e.g. 40 fps)
		//@param	Looped		Whether or not the animation is looped or just plays once
		public FlxAnim(String Name, ArrayList<Integer> Frames, float FrameRate, boolean Looped)
		{
			constructor(Name, Frames, FrameRate, Looped);
		}
		
		public FlxAnim(String Name, ArrayList<Integer> Frames, float FrameRate)
		{
			constructor(Name, Frames, FrameRate, true);
		}
		
		public FlxAnim(String Name, ArrayList<Integer> Frames)
		{
			constructor(Name, Frames, 0, true);
		}
		
		protected void constructor(String Name, ArrayList<Integer> Frames, float FrameRate, boolean Looped)
		{
			name = Name;
			delay = 1.0f/FrameRate;
			frames = Frames;
			looped = Looped;
		}
	}

