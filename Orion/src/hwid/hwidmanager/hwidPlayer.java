package hwid.hwidmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import hwid.hwidmanager.hwidInfoList.LockType;
import l2jorion.game.network.L2GameClient;
import l2jorion.logger.Logger;
import l2jorion.logger.LoggerFactory;
import l2jorion.util.database.L2DatabaseFactory;

public class hwidPlayer
{
	protected static final Logger LOG = LoggerFactory.getLogger(hwidPlayer.class.getName());
	private static hwidPlayer INSTANCE;
	private static Map<Integer, hwidInfoList> _list = new HashMap<>();
	private static Map<Integer, Integer> _sessions = new HashMap<>();
	
	public hwidPlayer()
	{
		load();
		LOG.info("Loaded " + _list.size() + " player(s) HWID(s)");
	}
	
	public static hwidPlayer getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new hwidPlayer();
		}
		return INSTANCE;
	}
	
	private static void load()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM hwid_info");
			ResultSet rset = statement.executeQuery())
		{
			int counterHWIDInfo = 0;
			while (rset.next())
			{
				final hwidInfoList hInfo = new hwidInfoList(counterHWIDInfo);
				hInfo.setHWID(rset.getString("HWID"));
				hInfo.setLogin(rset.getString("Account"));
				hInfo.setPlayerID(rset.getInt("PlayerID"));
				hInfo.setLockType(LockType.valueOf(rset.getString("LockType")));
				_list.put(counterHWIDInfo, hInfo);
				++counterHWIDInfo;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void reload()
	{
		INSTANCE = new hwidPlayer();
	}
	
	public static int startSession(int WindowsCount)
	{
		synchronized (_list)
		{
			if (_sessions.get(WindowsCount) == null)
			{
				_sessions.put(WindowsCount, 0);
			}
			_sessions.put(WindowsCount, _sessions.get(WindowsCount) + 1);
		}
		return _sessions.get(WindowsCount);
	}
	
	public static void updateHWIDInfo(L2GameClient client)
	{
		updateHWIDInfo(client, LockType.NONE);
	}
	
	public static void updateHWIDInfo(final L2GameClient client, final LockType lockType)
	{
		int counterHwidInfo = _list.size();
		boolean isFound = false;
		for (int i = 0; i < _list.size(); ++i)
		{
			if (_list.get(i).getHWID().equals(client.getHWID()))
			{
				isFound = true;
				counterHwidInfo = i;
				break;
			}
		}
		final hwidInfoList hInfo = new hwidInfoList(counterHwidInfo);
		hInfo.setHWID(client.getHWID());
		hInfo.setLogin(client.getAccountName());
		hInfo.setPlayerID(client.getPlayerId());
		hInfo.setLockType(lockType);
		_list.put(counterHwidInfo, hInfo);
		if (isFound)
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("UPDATE hwid_info SET Account=?,PlayerID=?,LockType=? WHERE HWID=?"))
			{
				statement.setString(1, client.getAccountName());
				statement.setInt(2, client.getPlayerId());
				statement.setString(3, lockType.toString());
				statement.setString(4, client.getHWID());
				statement.execute();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("INSERT INTO hwid_info (HWID, Account, PlayerID, LockType) values (?,?,?,?)"))
			{
				statement.setString(1, client.getHWID());
				statement.setString(2, client.getAccountName());
				statement.setInt(3, client.getPlayerId());
				statement.setString(4, lockType.toString());
				statement.execute();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static boolean checkLockedHWID(final L2GameClient client)
	{
		if (_list.size() == 0)
		{
			return false;
		}
		
		boolean result = false;
		for (int i = 0; i < _list.size(); ++i)
		{
			switch (_list.get(i).getLockType().ordinal())
			{
				case 2:
				{
					if (client.getPlayerId() == 0)
					{
						break;
					}
					
					if (_list.get(i).getPlayerID() != client.getPlayerId())
					{
						break;
					}
					
					if (_list.get(i).getHWID().equals(client.getHWID()))
					{
						return false;
					}
					
					result = true;
					break;
				}
				case 3:
				{
					if (!_list.get(i).getLogin().equals(client.getLoginName()))
					{
						break;
					}
					
					if (_list.get(i).getHWID().equals(client.getHWID()))
					{
						return false;
					}
					
					result = true;
					break;
				}
			}
		}
		return result;
	}
	
	public static int getAllowedWindowsCount(final L2GameClient client)
	{
		if (_list.size() == 0)
		{
			return -1;
		}
		
		int i = 0;
		while (i < _list.size())
		{
			if (!_list.get(i).getHWID().equals(client.getHWID()))
			{
				++i;
			}
			else
			{
				if (_list.get(i).getHWID().equals(""))
				{
					return -1;
				}
				
				return _list.get(i).getCount();
			}
		}
		return -1;
	}
	
	public static int getCountHwidInfo()
	{
		return _list.size();
	}
}