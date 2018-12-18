package de.tacticalteam;

import com.wmi.windows.WmiHelper;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;

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
		final File file = new File(Objects.requireNonNull(classLoader.getResource("TTT.json.template")).getFile());
		final Context context = new VelocityContext();
		context.put("mic", askForAudioDevice("dein Mikro"));
		context.put("desktop", askForAudioDevice("deinen Spielsound"));
		if (askForOption("L\u00e4uft dein TS auf \u00fcber ein anderes Audioger\u00e4t als der Spielsound?"))
		{
			context.put("TS", askForAudioDevice("dein TS"));
		}
		context.put("name", askFor("Gib deinen Namen im Spiel ein"));
		context.put("overlay_path", askFor("Gib den Pfad zum Overlay ein"));
		context.put("stream_bg", askFor("Gib den Pfad zum Stream-Hintergrund ein"));
		final String resultFileName = askFor("Wo soll die fertige Import-Datei erstellt werden");
		final File outFile = new File(resultFileName);
		Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new NullLogChute());
		try(final FileReader reader = new FileReader(file);
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
		return reader.next();
	}

	private boolean askForOption(final String text)
	{
		final Scanner reader = new Scanner(System.in);
		String option;
		do
		{
			System.out.print(text + " (y/n): ");
			option = reader.next();
		}
		while (!"y".equals(option.toLowerCase(Locale.ENGLISH)) && !"n".equals(option.toLowerCase(Locale.ENGLISH)));
		System.out.println();
		return "y".equals(option.toLowerCase(Locale.ENGLISH));
	}
}
