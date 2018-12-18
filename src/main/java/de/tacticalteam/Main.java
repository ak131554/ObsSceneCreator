package de.tacticalteam;

import com.wmi.windows.WmiHelper;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.tuple.Pair;

public class Main
{
	public static void main(String[] args)
	{
		final List<Pair<String, String>> audioDevices = new WmiHelper().queryAudioDevices();
		System.out.println("W\u00e4hle deine Audioquelle");
		System.out.println("-----------------------");
		for (int i = 0; i < audioDevices.size(); i++)
		{
			System.out.println(Integer.toString(i) + ". " + audioDevices.get(i).getKey());
		}
		final Scanner reader = new Scanner(System.in);
		final int index = reader.nextInt();
		final String deviceId = audioDevices.get(index).getValue();
		System.out.println("Die DeviceId deiner Audioquelle lautet:");
		System.out.println(deviceId);
	}
}
