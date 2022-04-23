package hwid.crypt;

import l2jorion.util.random.Rnd;

public class ProtectionPackets
{
	public static int readB(final byte[] raw, int offset, final byte[] data, final int size)
	{
		for (int i = 0; i < size; ++i)
		{
			data[i] = (byte) (raw[offset] ^ raw[0]);
			offset += (raw[offset + 1] & 0xFF);
		}
		return offset;
	}
	
	public static int readS(final byte[] raw, int offset, final byte[] data, final int size)
	{
		for (int i = 0; i < size; ++i)
		{
			data[i] = (byte) (raw[offset] ^ raw[0]);
			offset += (raw[offset + 1] & 0xFF);
			if (data[i] == 0)
			{
				break;
			}
		}
		return offset;
	}
	
	public static int writeB(final byte[] raw, int offset, final byte[] data, final int size)
	{
		for (int i = 0; i < size; ++i)
		{
			raw[offset] = (byte) (data[i] ^ raw[0]);
			raw[offset + 1] = (byte) (2 + Rnd.nextInt(10));
			offset += (raw[offset + 1] & 0xFF);
		}
		return offset;
	}
	
	public static byte ck(final byte[] raw, final int offset, final int size)
	{
		byte c = -1;
		for (int i = 0; i < size; ++i)
		{
			c ^= raw[offset + i];
		}
		return c;
	}
}