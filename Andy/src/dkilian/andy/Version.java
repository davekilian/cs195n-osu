package dkilian.andy;

/**
 * Stores information about this version of Andy
 * @author dkilian
 */
public class Version 
{
	/** The major version number of this Andy build (i.e. the X in vX.Y) */
	public static final int VersionMajor = 0;

	/** The minor version number of this Andy build (i.e. the Y in vX.Y) */
	public static final int VersionMinor = 1;
	
	/** The codename for this version of Andy */
	public static final String Codename = "Mandy";
	
	/** The version string unique to this release of Andy */
	public static final String VersionString = "0.1-m";
	
	/** A short human-readable version string */
	public static final String HumanString = "0.1 Mandy";
	
	/** A long human-readable version string */
	public static final String HumanLongString = "\"Mandy\" Andy 0.1-m";
}
