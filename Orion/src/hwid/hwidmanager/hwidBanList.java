package hwid.hwidmanager;

public class hwidBanList
{
	private final int _id;
	private String _hwid;
	
	public hwidBanList(final int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getHWID()
	{
		return _hwid;
	}
	
	public void setHWIDBan(final String hwid)
	{
		_hwid = hwid;
	}
}
