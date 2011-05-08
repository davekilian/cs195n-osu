package osu.parser;

/**
 * The exception to be thrown when the Osu! parser does not run correctly.
 * This can be caused by an invalid file type and many other issues.
 * 
 * @author Michael Comella
 *
 */
public class ParseException extends Exception {

	// Note, I have no idea how Exceptions work so this could be complete suck!
	
	private static final long serialVersionUID = 1L; // I don't know what this does!
	
	// *** CONSTRUCTORS *** //
	/**
	 * Constructs a ParseException with a <code>null</code> message.
	 */
	public ParseException()
	{
		super();
	}
	
	
	/**
	 * Constructs a ParseException with a descriptor, <code>msg</code>.
	 * 
	 * @param msg The reason the exception was thrown.
	 */
	public ParseException(String msg)
	{
		super(msg);
	}

}
