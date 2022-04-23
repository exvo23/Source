package hwid.crypt;

import hwid.crypt.impl.L2Client;
import hwid.crypt.impl.L2Server;
import hwid.crypt.impl.VMPC;
import l2jorion.Config;

public class GameCrypt
{
	private ProtectionCrypt _client;
	private ProtectionCrypt _server;
	private boolean _isEnabled;
	private boolean _isProtected;
	
	public GameCrypt()
	{
		_isEnabled = false;
		_isProtected = false;
	}
	
	public void setProtected(final boolean state)
	{
		_isProtected = state;
	}
	
	public void setKey(final byte[] key)
	{
		if (_isProtected)
		{
			(_client = new VMPC()).setup(key, Config.GUARD_CLIENT_CRYPT);
			(_server = new L2Server()).setup(key, null);
			(_server = new VMPC()).setup(key, Config.GUARD_SERVER_CRYPT);
		}
		else
		{
			(_client = new L2Client()).setup(key, null);
			(_server = new L2Server()).setup(key, null);
		}
	}
	
	public void decrypt(final byte[] raw, final int offset, final int size)
	{
		if (_isEnabled)
		{
			_client.crypt(raw, offset, size);
		}
	}
	
	public void encrypt(final byte[] raw, final int offset, final int size)
	{
		if (_isEnabled)
		{
			_server.crypt(raw, offset, size);
		}
		else
		{
			_isEnabled = true;
		}
	}
}