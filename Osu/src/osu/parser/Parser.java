package osu.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

public class Parser {
	
	// Resource Accessors
	private final AssetManager asset_manager;
	
	private InputStream input_stream;
	private BufferedReader reader;
	
	// Storage
	HashMap<Subsections, HashMap<String, String>> dict;
	
	
	// *** CONSTRUCTORS *** //
	public Parser(Activity act)
	{
		asset_manager = act.getAssets();
		
		input_stream = null;
		
		initDict();
	}
	
	
	// *** INIT METHODS *** //
	private void initDict()
	{
		dict = new HashMap<Subsections, HashMap<String, String>>();
		
		// Init Subsections
		dict.put(Subsections.GENERAL, new HashMap<String, String>());
		dict.put(Subsections.EDITOR, new HashMap<String, String>());
		dict.put(Subsections.METADATA, new HashMap<String, String>());
		dict.put(Subsections.DIFFICULTY, new HashMap<String, String>());
		
		HashMap<String, String> cur;
		
		// General //
		cur = dict.get(Subsections.GENERAL);
		cur.put("audiofilename", null);
		cur.put("audioleadin", null);
		cur.put("previewtime", null);
		cur.put("countdown", null);
		cur.put("sampleset", null);
		cur.put("stackleniency", null);
		cur.put("mode", null);
		cur.put("letterboxinbreaks", null);
		cur.put("skinpreference", null);
		cur.put("countdownoffset", null);
		cur.put("storyfireinfront", null);
		cur.put("epilepsywarning", null);
		
		// Editor //
		cur = dict.get(Subsections.EDITOR);
		cur.put("distancespacing", null);
		cur.put("beatdivisor", null);
		cur.put("gridsize", null);
		
		// Metadata //
		cur = dict.get(Subsections.METADATA);
		cur.put("title", null);
		cur.put("artist", null);
		cur.put("creator", null);
		cur.put("version", null);
		cur.put("source", null);
		cur.put("tags", null);
		
		// Difficulty //
		cur = dict.get(Subsections.DIFFICULTY);
		cur.put("hpdrainrate", null);
		cur.put("circlesize", null);
		cur.put("overalldifficulty", null);
		cur.put("approachrate", null);
		cur.put("slidermultiplier", null);
		cur.put("slidertickrate", null);
	}
	
	
	// *** ACTION *** //
	public void parse(String path) throws ParseException, IOException
	{
		/*
		 * TODO:
		 *  - Check for nulls in reader.readLine()
		 *  - Deal with all necessary subsections not being found
		 *  - List-Based
		 *  - Special Case
		 *  - Return HashMaps to game Objects
		 */
		
		try {
			// File IO
			input_stream = asset_manager.open(path, AssetManager.ACCESS_BUFFER);
			InputStreamReader isr = new InputStreamReader(input_stream);
			reader = new BufferedReader(isr);
			
			// Check file format header
			if (!confirmFileFormat())
				throw new ParseException("Invalid file format.");
			
			// Iterate through sub-sections and parse their attributes
			String output = getFirstHeader();
			
			while (output != null) // Loop until the end of the file (null)
				output = manageSubsection(output);
			
			// Close all used resources
			reader.close();
			reader = null;
			
			isr.close();
			isr = null;
			
			input_stream.close();
			input_stream = null;
		} catch (IOException ex) {
			// Null all values so the garbage collector doesn't get in the way post parse
			nullify();
			throw ex;
		}
	}
	
	
	/**
	 * Parses the subsection following the current given header (String in) and handles the attributes accordingly.
	 * @param in The current subsection header String read off from the BufferedReader.
	 * @return The first non-attribute String found. Returns null if the end of the current file has been reached.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private String manageSubsection(String in) throws IOException, ParseException
	{
		String section_name = in.substring(in.indexOf('[') + 1, in.lastIndexOf(']')).toUpperCase();
		
		try {
			Subsections section_enum = Subsections.valueOf(section_name);
			
			switch (section_enum)
			{
			
			// Attribute-Based
			case GENERAL:
			case EDITOR:
			case METADATA:
			case DIFFICULTY:
				return handleAttributeBased(section_enum);
				
			// List-Based
			case TIMINGPOINTS:
				return handleTimingPoints();
			case COLOURS:
				return handleColours();
			case HITOBJECTS:
				return handleHitObjects();
				
			// Special-Case
			case EVENTS:
				return handleEvents();
				
			default:
				throw new ParseException("Coder error in manageSubsection: missed a header title.");
			}
		} catch (IllegalArgumentException ex) {
			throw new ParseException("Unknown section header.");
		}
		
	}
	
	
	/**
	 * Ensures the file format header conforms to the osu! format.
	 * @return True if the header is vaild, false otherwise
	 * @throws IOException If the act of reading the file's input stream throws an Exception.
	 */
	private boolean confirmFileFormat() throws IOException
	{
		String output = reader.readLine();
		
		// Error checking
		if (output == null)
			return false;
		
		// Check for vaild version descriptor
		if (!output.substring(0, ParserConstants.HEADER_START.length()).equals(ParserConstants.HEADER_START))
			return false;
		
		// Check for valid version number
		if (!intArrContains(ParserConstants.ACCEPTABLE_FILE_VERSIONS, Integer.parseInt(output.substring(ParserConstants.HEADER_START.length()))))
			return false;
		
		return true;
	}
	
	
	private String getFirstHeader() throws IOException, ParseException
	{
		String output = "";
		while (!isHeader(output))
		{
			output = reader.readLine();
			
			if (output == null) // Error checking
				throw new ParseException("No subsections found.");
		}
		
		return output;
	}
	
	
	// *** HANDLE SUBSECTIONS *** //
	// Attribute-Based //
	private String handleAttributeBased(Subsections header) throws IOException
	{
		HashMap<String, String> subsect = dict.get(header); // Current subsection we are working in
		
		String line, attrib, val;
		while (true)
		{
			line = reader.readLine();
			
			if (line == null || isHeader(line)) // Return at end of file or next header
				break;
			if (line.length() == 0) // Skip over blank lines
				continue;
			
			line = line.toLowerCase(); // For easier checking against HashMap
			
			// Parse left-side of expression
			attrib = removeSpaces(line.substring(0, line.indexOf(':')));
			
			// Parse right-side of expression
			val = removeSpaces(line.substring(line.indexOf(':') + 1));
			
			// Set values in HashMap
			if (subsect.containsKey(attrib))
				subsect.put(attrib, val);
			else // Invalid attribute
				Log.e("Parser.handleAttributeBased", "Invalid Attribute, \"" + attrib + "\" in subsection, \"" + header.toString() + "\".");
		}
		
		return line;
	}
	
	
	// List-Based //
	private String handleTimingPoints()
	{
		// List-based
		return null;
	}
	
	private String handleColours()
	{
		// List-Based
		return null;
	}
	
	private String handleHitObjects()
	{
		// List-based
		return null;
	}
	
	
	// Special-Case //
	private String handleEvents()
	{
		// Special case
		return null;
	}
	
	
	// *** HELPER METHODS *** //
	/**
	 * Returns true if the given int array contains the given int, false otherwise.
	 */
	private boolean intArrContains(int[] arr, int i)
	{
		int s = arr.length;
		for (int x = 0; x < s; ++x)
		{
			if (arr[x] == i)
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * Returns true if the specified string is a section header.
	 * Does not check if the given String is null beforehand.
	 * 
	 * @param s The String to check.
	 * @return True if s is a section header, false otherwise.
	 */
	private boolean isHeader(String s)
	{
		if (s.charAt(0) == '[' && s.contains("]"))
			return true;
		
		return false;
	}
	
	
	/**
	 * Removes all spaces in the input String.
	 * @param s The String to remove the spaces from.
	 * @return The given String with no space characters.
	 */
	private String removeSpaces(String s)
	{	
		int space_ind = s.indexOf(' ');
		while (space_ind >= 0)
		{
			if (space_ind != (s.length() - 1)) // Not out of bounds
				s = s.substring(0, space_ind).concat(s.substring(space_ind + 1));
			else // Out of bounds
				s = s.substring(0, space_ind);
			
			space_ind = s.indexOf(' ');
		}
		
		return s;
	}
	
	
	// *** MISCELLANEOUS *** //
	public void nullify()
	{
		input_stream = null;
		reader = null;
		
		dict = null;
	}

}
