package hwid.crypt.impl;

import hwid.crypt.ProtectionCrypt;

public class VMPC implements ProtectionCrypt
{
	private byte _n;
	private byte[] _P;
	private byte _s;
	
	public VMPC()
	{
		_n = 0;
		_P = new byte[256];
		_s = 0;
	}
	
	@Override
	public void setup(final byte[] key, final byte[] iv)
	{
		_s = 0;
		for (int i = 0; i < 256; ++i)
		{
			_P[i] = (byte) (i & 0xFF);
		}
		for (int m = 0; m < 768; ++m)
		{
			_s = _P[_s + _P[m & 0xFF] + key[m % 64] & 0xFF];
			final byte temp = _P[m & 0xFF];
			_P[m & 0xFF] = _P[_s & 0xFF];
			_P[_s & 0xFF] = temp;
		}
		for (int m = 0; m < 768; ++m)
		{
			_s = _P[_s + _P[m & 0xFF] + iv[m % 64] & 0xFF];
			final byte temp = _P[m & 0xFF];
			_P[m & 0xFF] = _P[_s & 0xFF];
			_P[_s & 0xFF] = temp;
		}
		for (int m = 0; m < 768; ++m)
		{
			_s = _P[_s + _P[m & 0xFF] + key[m % 64] & 0xFF];
			final byte temp = _P[m & 0xFF];
			_P[m & 0xFF] = _P[_s & 0xFF];
			_P[_s & 0xFF] = temp;
		}
		_n = 0;
	}
	
	@Override
	public void crypt(final byte[] raw, final int offset, final int size)
	{
		for (int i = 0; i < size; ++i)
		{
			_s = _P[_s + _P[_n & 0xFF] & 0xFF];
			final byte z = _P[_P[_P[_s & 0xFF] & 0xFF] + 1 & 0xFF];
			final byte temp = _P[_n & 0xFF];
			_P[_n & 0xFF] = _P[_s & 0xFF];
			_P[_s & 0xFF] = temp;
			_n = (byte) (_n + 1 & 0xFF);
			raw[offset + i] ^= z;
		}
	}
}