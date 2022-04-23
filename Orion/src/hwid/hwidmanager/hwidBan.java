package hwid.hwidmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import l2jorion.game.network.L2GameClient;
import l2jorion.logger.Logger;
import l2jorion.logger.LoggerFactory;
import l2jorion.util.database.L2DatabaseFactory;

public class hwidBan
{
	protected static final Logger LOG = LoggerFactory.getLogger(hwidBan.class.getName());
	private static hwidBan INSTANCE;
	private static Map<Integer, hwidBanList> _lists = new HashMap<>();
	
	public hwidBan()
	{
		load();
		LOG.info("Loaded " + hwidBan._lists.size() + " banned(s) HWID(s)");
	}
	
	public static hwidBan getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new hwidBan();
		}
		return INSTANCE;
	}
	
	private static void load()
	{
		String HWID = "";
		int counterHWIDBan = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM hwid_bans");
			ResultSet rset = statement.executeQuery())
		{
			while (rset.next())
			{
				HWID = rset.getString("HWID");
				final hwidBanList hb = new hwidBanList(counterHWIDBan);
				hb.setHWIDBan(HWID);
				_lists.put(counterHWIDBan, hb);
				++counterHWIDBan;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void reload()
	{
		INSTANCE = new hwidBan();
	}
	
	public boolean checkFullHWIDBanned(final L2GameClient client)
	{
		if (_lists.size() == 0)
		{
			return false;
		}
		for (int i = 0; i < _lists.size(); ++i)
		{
			if (_lists.get(i).getHWID().equals(client.getHWID()))
			{
				return true;
			}
		}
		return false;
	}
	
	public static int getCountHWIDBan()
	{
		return _lists.size();
	}
	
	public static void addHWIDBan(final L2GameClient client)
	{
		final String HWID = client.getHWID();
		final int counterHwidBan = _lists.size();
		final hwidBanList hb = new hwidBanList(counterHwidBan);
		hb.setHWIDBan(HWID);
		_lists.put(counterHwidBan, hb);
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO hwid_bans SET HWID=?"))
		{
			statement.setString(1, HWID);
			statement.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}