package hwid.hwidmanager;

public class hwidInfoList
{
	private final int _id;
	private String _hwid;
	private int _count;
	private int _playerID;
	private String _login;
	private LockType _lockType;
	
	public hwidInfoList(final int id)
	{
		_id = id;
	}
	
	public int get_id()
	{
		return _id;
	}
	
	public int getCount()
	{
		return _count;
	}
	
	public void setCount(final int count)
	{
		_count = count;
	}
	
	public int getPlayerID()
	{
		return _playerID;
	}
	
	public void setPlayerID(final int playerID)
	{
		_playerID = playerID;
	}
	
	public String getHWID()
	{
		return _hwid;
	}
	
	public void setHWID(final String hwid)
	{
		_hwid = hwid;
	}
	
	public String getLogin()
	{
		return _login;
	}
	
	public void setLogin(final String login)
	{
		_login = login;
	}
	
	public LockType getLockType()
	{
		return _lockType;
	}
	
	public void setLockType(final LockType lockType)
	{
		_lockType = lockType;
	}
	
	public enum LockType
	{
		PLAYER_LOCK,
		ACCOUNT_LOCK,
		NONE;
	}
}