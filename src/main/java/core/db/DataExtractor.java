package core.db;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

public class DataExtractor 
{
	public static String convertToString(ByteBuffer data) 
	{
		return com.mezocliq.qrypt.helper.serializer.StringSerializer.getInstance().deSerialize(data);
	}
	
	public static ByteBuffer convertToByteBuffer(String data) 
	{
		return com.mezocliq.qrypt.helper.serializer.StringSerializer.getInstance().serialize(data);
	}
	
	public static boolean isEmpty(String data) 
	{
		return data == null || data.isEmpty();
	}

	public static boolean isEmpty(Collection value)
	{
		return value == null || value.isEmpty();
	}

    public static boolean isEmpty(Map value)
    {
        return value == null || value.isEmpty();
    }
}