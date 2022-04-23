package hwid.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import l2jorion.logger.Logger;
import l2jorion.logger.LoggerFactory;

public class HwidLog
{
	protected static final Logger LOG = LoggerFactory.getLogger(HwidLog.class.getName());
	
	public HwidLog()
	{
	}
	
	public static void auditGMAction(String gmName, String action, String params)
	{
		final File file = new File("log/hwid/" + gmName + ".txt");
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
			}
		}
		
		try (FileWriter save = new FileWriter(file, true))
		{
			save.write(formatDate(new Date(), "dd/MM/yyyy H:mm:ss") + " >>> HWID: [" + gmName + "] >>> Player: [" + action + "]\r\n");
		}
		catch (IOException e)
		{
			LOG.error("HwidLog for Player " + gmName + " could not be saved: ", e);
		}
	}
	
	public static void auditGMAction(String gmName, String action)
	{
		auditGMAction(gmName, action, "");
	}
	
	public static String formatDate(Date date, String format)
	{
		final DateFormat dateFormat = new SimpleDateFormat(format);
		if (date != null)
		{
			return dateFormat.format(date);
		}
		
		return null;
	}
}