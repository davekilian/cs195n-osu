package dkilian.andy;

/**
 * Interface for a message that can be sent over a network channel
 * @author dkilian
 */
public interface NetworkMessage
{
	/** Gets a number unique to this NetworkMessage implementation's type (but is shared across all instances of the type) */
	public int getTypeID();
	
	/** Gets the network clock time this message was sent, in partial seconds */
	public float getTimestamp();

	/**
	 * Serializes the payload of this message. Note that, since only one of each type of message is ever allocated by the system
	 * and only one message is ever processed at a time, it is OK for message implementations to allocate the buffer returned by
	 * this function once and reuse it for each call to improve GC performance. 
	 */
	public byte[] pack();
	
	/**
	 * Deserializes this message's payload and updates its timestamp
	 * @param buffer A buffer containing the payload of this message
	 * @param offset The offset into the buffer at which the payload begins
	 * @param len The number of bytes in the payload
	 * @param timestamp The timestamp at which the message being unpacked was sent (must update this object's getTimestamp() value)
	 */
	public void unpack(byte[] buffer, int offset, int len, float timestamp);
}
