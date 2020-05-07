package com.wmi.windows;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WmiHelper
{
	public List<AudioDevice> queryAudioDevices()
	{
		ActiveXComponent wmi = new ActiveXComponent("WbemScripting.SWbemLocator");
		// no connection parameters means to connect to the local machine
		Variant conRet = wmi.invoke("ConnectServer");
		// the author liked the ActiveXComponent api style over the Dispatch
		// style
		ActiveXComponent wmiconnect = new ActiveXComponent(conRet.toDispatch());

		// the WMI supports a query language.
		String query = "SELECT * "
			+ "FROM Win32_PnPEntity "
			+ "WHERE PNPDeviceID LIKE 'SWD\\\\MMDEVAPI\\\\%'";
		Variant vCollection = wmiconnect
			.invoke("ExecQuery", new Variant(query));

		EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
		List<AudioDevice> result = new ArrayList<>();
		while (enumVariant.hasMoreElements())
		{
			final Dispatch item = enumVariant.nextElement().toDispatch();
			result.add(new AudioDevice(Dispatch.call(item, "Caption").toString(), Dispatch.call(item, "PNPDeviceID").toString().replaceAll("SWD\\\\MMDEVAPI\\\\", "").toLowerCase(Locale.ENGLISH)));
		}
		return result;
	}
}