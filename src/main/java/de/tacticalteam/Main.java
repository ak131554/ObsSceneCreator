package de.tacticalteam;

import com.wmi.windows.WmiHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main
{
	private final List<Pair<String, String>> audioDevices;

	public static void main(String[] args)
	{
		new Main().createJson();
	}

	public Main()
	{
		audioDevices = new WmiHelper().queryAudioDevices();
	}

	private void createJson()
	{
		final ClassLoader classLoader = getClass().getClassLoader();
		final InputStream inputStream = classLoader.getResourceAsStream("TTT.json.template");
		final Context context = new VelocityContext();
		context.put("mic", askForAudioDevice("dein Mikro"));
		context.put("desktop", askForAudioDevice("deinen Spielsound"));
		if (askForOption("L\u00e4uft dein TS \u00fcber ein anderes Audioger\u00e4t als der Spielsound?"))
		{
			context.put("TS", askForAudioDevice("dein TS"));
		}
		context.put("name", askFor("Gib deinen Namen im Spiel ein"));
		final String overlayPath = askFor("Gib den Pfad zum Overlay ein");
		context.put("overlay_path", overlayPath.replaceAll("\\\\", "/"));
		final String backgroundPath = askFor("Gib den Pfad zum TTT-Waiting Screen ein");
		context.put("waiting_screen", backgroundPath.replaceAll("\\\\", "/"));
		final String armaPath = askFor("Gib den Pfad zu deiner arma3_x64.exe ein");
		context.put("arma_path", "Arma 3 \\\"" + armaPath.replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "#3A") + "\\\" -beservice:Arma 3:arma3_x64.exe");
		final String resultFileName = askFor("Wo soll die fertige Import-Datei erstellt werden");
		final File outFile = new File(resultFileName + "\\TTT_Streamen_2019.json");
		Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new NullLogChute());
		try(final Reader reader = new InputStreamReader(inputStream);
			final FileWriter writer = new FileWriter(outFile))
		{
			Velocity.evaluate(context, writer, "CreateJson", reader);
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private String askForAudioDevice(final String text)
	{
		final String string = "W\u00e4hle die Audioquelle f\u00fcr " + text;
		System.out.println(string);
		//noinspection ReplaceAllDot
		System.out.println(string.replaceAll(".", "-"));
		for (int i = 0; i < audioDevices.size(); i++)
		{
			System.out.println(Integer.toString(i) + ". " + audioDevices.get(i).getKey());
		}
		System.out.print("Geben Sie die Nummer der Audioquelle ein: ");
		final Scanner reader = new Scanner(System.in);
		final int index = reader.nextInt();
		System.out.println();
		return audioDevices.get(index).getValue();
	}

	private String askFor(final String text)
	{
		final Scanner reader = new Scanner(System.in);
		System.out.print(text + ": ");
		return reader.nextLine();
	}

	private boolean askForOption(final String text)
	{
		final Scanner reader = new Scanner(System.in);
		String option;
		do
		{
			System.out.print(text + " (y/n): ");
			option = reader.nextLine();
		}
		while (!"y".equals(option.toLowerCase(Locale.ENGLISH)) && !"n".equals(option.toLowerCase(Locale.ENGLISH)));
		System.out.println();
		return "y".equals(option.toLowerCase(Locale.ENGLISH));
	}
}
