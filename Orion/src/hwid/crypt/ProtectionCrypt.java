package hwid.crypt;

public interface ProtectionCrypt
{
	void setup(final byte[] p0, final byte[] p1);
	
	void crypt(final byte[] p0, final int p1, final int p2);
}
