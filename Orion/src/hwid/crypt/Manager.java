package hwid.crypt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import hwid.hwidmanager.hwidPlayer;
import l2jorion.Config;
import l2jorion.game.network.L2GameClient;
import l2jorion.logger.Logger;
import l2jorion.logger.LoggerFactory;

public final class Manager
{
	protected static final Logger LOG = LoggerFactory.getLogger(Manager.class.getName());
	protected static String _logFile = "Manager";
	protected static String _logMainFile = "hwid_logs";
	protected static Manager INSTANCE;
	protected static ScheduledFuture<?> _GGTask = null;
	protected static ConcurrentHashMap<String, InfoSet> _objects = new ConcurrentHashMap<>();
	
	public static Manager getInstance()
	{
		if (INSTANCE == null)
		{
			LOG.info("Loaded HWID KEY L2Brasil Project");
			LOG.info("Loaded HWID IP " + Config.GAMESERVER_HOSTNAME);
			LOG.info("Loaded Anti Leech key...assigned ");
			LOG.info("Loaded Restriction Leech...assigned ");
			LOG.info("Loaded Licensed to Max Players : " + Config.MAXIMUM_ONLINE_USERS);
			INSTANCE = new Manager();
		}
		return INSTANCE;
	}
	
	public void addPlayer(final L2GameClient client)
	{
		hwidPlayer.updateHWIDInfo(client);
		_objects.put(client.getPlayerName(), new InfoSet(client.getPlayerName(), client.getHWID()));
	}
	
	public static void removePlayer(final String name)
	{
		if (_objects.containsKey(name))
		{
			_objects.remove(name);
		}
	}
	
	public static int getCountByHWID(final String HWID)
	{
		int result = 0;
		for (final InfoSet object : _objects.values())
		{
			if (object._hwid.equals(HWID))
			{
				++result;
			}
		}
		return result;
	}
	
	public class InfoSet
	{
		public String _playerName;
		public long _lastGGSendTime;
		public long _lastGGRecvTime;
		public int _attempts;
		public String _hwid;
		
		public InfoSet(final String name, final String HWID)
		{
			_playerName = "";
			_hwid = "";
			_playerName = name;
			_lastGGSendTime = System.currentTimeMillis();
			_lastGGRecvTime = _lastGGSendTime;
			_attempts = 0;
			_hwid = HWID;
		}
	}
}