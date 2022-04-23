package l2jorion.game.network.serverpackets;

public final class SpecialString extends L2GameServerPacket
{
	private int _strId;
	private int _fontSize;
	private int _x;
	private int _y;
	private int _color;
	private boolean _isDraw;
	private String _text;
	
	public SpecialString(int strId, boolean isDraw, int fontSize, int x, int y, int color, String text)
	{
		_strId = strId;
		_isDraw = isDraw;
		_fontSize = fontSize;
		_x = x;
		_y = y;
		_color = color;
		_text = text;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeC(176);
		writeC(_strId);
		writeC(_isDraw ? 1 : 0);
		writeC(_fontSize);
		writeD(_x);
		writeD(_y);
		writeD(_color);
		writeS(_text);
	}
	
	@Override
	public String getType()
	{
		return "[S] B0 SpecialString";
	}
}