package hwid.hwidmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2jorion.Config;
import l2jorion.game.model.actor.instance.L2PcInstance;
import l2jorion.game.network.L2GameClient;
import l2jorion.game.network.serverpackets.NpcHtmlMessage;
import l2jorion.logger.Logger;
import l2jorion.logger.LoggerFactory;

public class hwidManager
{
	protected static final Logger LOG = LoggerFactory.getLogger(hwidManager.class.getName());
	
	public hwidManager()
	{
	}
	
	private static boolean multiboxKickTask(final L2PcInstance activeChar, final Integer numberBox, final Collection<L2PcInstance> world)
	{
		final Map<String, List<L2PcInstance>> hwidMap = new HashMap<>();
		for (final L2PcInstance player : world)
		{
			if (player.getClient() != null)
			{
				if (player.getClient().isDetached())
				{
					continue;
				}
				
				final String hwid = activeChar.getHWID();
				final String playerHwid = player.getHWID();
				if (!hwid.equals(playerHwid))
				{
					continue;
				}
				
				if (hwidMap.get(hwid) == null)
				{
					hwidMap.put(hwid, new ArrayList<L2PcInstance>());
				}
				
				hwidMap.get(hwid).add(player);
				if (hwidMap.get(hwid).size() >= numberBox)
				{
					return true;
				}
				
				continue;
			}
		}
		return false;
	}
	
	public boolean validBox(final L2PcInstance activeChar, final Integer numberBox, final Collection<L2PcInstance> world, final Boolean forcedLogOut)
	{
		if (multiboxKickTask(activeChar, numberBox, world))
		{
			if (forcedLogOut)
			{
				final L2GameClient client = activeChar.getClient();
				LOG.warn("Dualbox Protection: " + client.getHWID() + " was trying to use over " + numberBox + " clients!");
				activeChar.sendMessage("SYS: You have exceeded the PC connection limit = " + Config.PROTECT_WINDOWS_COUNT + " box per PC.");
				activeChar.sendMessage("SYS: You will be disconnected in 30 seconds.");
				activeChar.setIsImobilised(true);
				activeChar.setIsInvul(true);
				activeChar.startAbnormalEffect(0x000200);
				activeChar.disableAllSkills();
				showChatWindow(activeChar, 0);
				waitSecs(30);
				activeChar.getClient().closeNow();
			}
			return true;
		}
		return false;
	}
	
	public void showChatWindow(final L2PcInstance player, final int val)
	{
		final NpcHtmlMessage msg = new NpcHtmlMessage(5);
		msg.setHtml(ExcedLimit(player));
		player.sendPacket(msg);
	}
	
	private static String ExcedLimit(final L2PcInstance player)
	{
		final StringBuilder tb = new StringBuilder();
		tb.append("<html><body><center>");
		tb.append("<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32>HWID<font color=LEVEL> Dual Box </font>'- Manager");
		tb.append("<br><table><tr><td height=7><img src=\"L2UI.SquareGray\" width=220 height=1></td></tr></table>");
		tb.append("<img src=\"L2UI.SquareGray\" width=295 height=1><table width=295 border=0 bgcolor=000000><tr><td align=center>");
		tb.append("<br>You have exceeded the PC connection limit.<br1>Server have limit to <font color=LEVEL>" + Config.PROTECT_WINDOWS_COUNT + "</font> per PC.<br><br>You will be disconnected in '<font color=LEVEL>30 seconds</font>'.<br1>" + player.getName()
			+ ", Thanks for following the server rules.<br1>Thanks.<br>");
		tb.append("<br><img src=\"l2ui.squarewhite\" width=\"150\" height=\"1\"><br>");
		tb.append("<br></td></tr></table><img src=\"L2UI.SquareGray\" width=295 height=1>");
		tb.append("<table><tr><td height=7><img src=\"L2UI.SquareGray\" width=220 height=1></td></tr></table><br>");
		tb.append("<br><br><font color=333333>Respect the rules</font>");
		return tb.toString();
	}
	
	public static void waitSecs(final int i)
	{
		try
		{
			Thread.sleep(i * 1000);
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}
	
	public static final hwidManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final hwidManager INSTANCE = new hwidManager();
	}
}