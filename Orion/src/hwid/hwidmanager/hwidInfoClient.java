package hwid.hwidmanager;

public class hwidInfoClient
{
	private String _playerName;
	private String _loginName;
	private int _playerId;
	private String _hwid;
	private int _revision;
	
	public hwidInfoClient()
	{
		_playerName = "";
		_loginName = "";
		_playerId = 0;
		_hwid = "";
		_revision = 0;
	}
	
	public final String getPlayerName()
	{
		return _playerName;
	}
	
	public void setPlayerName(final String name)
	{
		_playerName = name;
	}
	
	public void setPlayerId(final int plId)
	{
		_playerId = plId;
	}
	
	public int getPlayerId()
	{
		return _playerId;
	}
	
	public final String getHWID()
	{
		return _hwid;
	}
	
	public void setHWID(final String hwid)
	{
		_hwid = hwid;
	}
	
	public void setRevision(final int revision)
	{
		_revision = revision;
	}
	
	public int getRevision()
	{
		return _revision;
	}
	
	public final String getLoginName()
	{
		return _loginName;
	}
	
	public void setLoginName(final String name)
	{
		_loginName = name;
	}
}