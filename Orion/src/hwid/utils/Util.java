package hwid.utils;

public class Util
{
	public static void intToBytes(final int value, final byte[] array, int offset)
	{
		array[offset++] = (byte) (value & 0xFF);
		array[offset++] = (byte) (value >> 8 & 0xFF);
		array[offset++] = (byte) (value >> 16 & 0xFF);
		array[offset++] = (byte) (value >> 24 & 0xFF);
	}
	
	public static String LastErrorConvertion(final Integer LastError)
	{
		return LastError.toString();
	}
	
	public static final String asHex(final byte[] raw)
	{
		return asHex(raw, 0, raw.length);
	}
	
	public static final String asHex(final byte[] raw, final int offset, final int size)
	{
		final StringBuffer strbuf = new StringBuffer(raw.length * 2);
		for (int i = 0; i < size; ++i)
		{
			if ((raw[offset + i] & 0xFF) < 16)
			{
				strbuf.append("0");
			}
			
			strbuf.append(Long.toString(raw[offset + i] & 0xFF, 16));
		}
		return strbuf.toString();
	}
	
	public static int bytesToInt(final byte[] array, int offset)
	{
		return (array[offset++] & 0xFF) | (array[offset++] & 0xFF) << 8 | (array[offset++] & 0xFF) << 16 | (array[offset++] & 0xFF) << 24;
	}
	
	public static String asHwidString(final String hex)
	{
		final byte[] buf = asByteArray(hex);
		return asHex(buf);
	}
	
	public static byte[] asByteArray(final String hex)
	{
		final byte[] buf = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2)
		{
			final int j = Integer.parseInt(hex.substring(i, i + 2), 16);
			buf[i / 2] = (byte) (j & 0xFF);
		}
		return buf;
	}
	
	public static boolean verifyChecksum(final byte[] raw, final int offset, final int size)
	{
		if ((size & 0x3) == 0x0 && size > 4)
		{
			long chksum = 0L;
			final int count = size - 4;
			for (int i2 = offset; i2 < count; i2 += 4)
			{
				chksum ^= bytesToInt(raw, i2);
			}
			final long check = bytesToInt(raw, count);
			return check == chksum;
		}
		return false;
	}
}