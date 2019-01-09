package de.tacticalteam;

import com.wmi.windows.AudioDevice;
import com.wmi.windows.WmiHelper;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;

public class Main extends Observable implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final Main INSTANCE = new Main();

	public static Main getInstance()
	{
		return INSTANCE;
	}

	private final List<AudioDevice> audioDevices;

	public enum ObserverArgument
	{
		SELECTED_MICRO,
		SELECTED_DESKTOP,
		SELECTED_TS,
		SAME_SOURCE_LIKE_DESKTOP,
		PATH_OVERLAY,
		PATH_WAITING_SCREEN,
		PATH_ARMA,
		PATH_JSON,
		PLAYER_NAME
	}

	private AudioDevice selectedMicroDevice = AudioDevice.DEFAULT_INPUT;
	private AudioDevice selectedDesktopDevice = AudioDevice.DEFAULT_OUTPUT;
	private AudioDevice selectedTSDevice = AudioDevice.DEFAULT_OUTPUT;
	private boolean sameSourceLikeDesktop = true;

	private String pathToOverlay = null;
	private String pathToWaitingScreen = null;
	private String pathToArmaExe = null;
	private String pathToJson = null;

	private String playerName = null;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(MainFrame::new);
	}

	public Main()
	{
		audioDevices = new WmiHelper().queryAudioDevices();
		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);

	}

	List<AudioDevice> getAudioDevices()
	{
		return audioDevices;
	}

	public AudioDevice getSelectedMicroDevice()
	{
		return selectedMicroDevice;
	}

	public void setSelectedMicroDevice(final AudioDevice selectedMicroDevice)
	{
		this.selectedMicroDevice = selectedMicroDevice;
		setChanged();
		notifyObservers(ObserverArgument.SELECTED_MICRO);
	}

	public AudioDevice getSelectedDesktopDevice()
	{
		return selectedDesktopDevice;
	}

	public void setSelectedDesktopDevice(final AudioDevice selectedDesktopDevice)
	{
		this.selectedDesktopDevice = selectedDesktopDevice;
		setChanged();
		notifyObservers(ObserverArgument.SELECTED_DESKTOP);
	}

	public AudioDevice getSelectedTSDevice()
	{
		return selectedTSDevice;
	}

	public void setSelectedTSDevice(final AudioDevice selectedTSDevice)
	{
		this.selectedTSDevice = selectedTSDevice;
		setChanged();
		notifyObservers(ObserverArgument.SELECTED_TS);
	}

	public boolean isSameSourceLikeDesktop()
	{
		return sameSourceLikeDesktop;
	}

	public void setSameSourceLikeDesktop(final boolean sameSourceLikeDesktop)
	{
		this.sameSourceLikeDesktop = sameSourceLikeDesktop;
		setChanged();
		notifyObservers(ObserverArgument.SAME_SOURCE_LIKE_DESKTOP);
	}

	public String getPathToOverlay()
	{
		return pathToOverlay;
	}

	public void setPathToOverlay(final String pathToOverlay)
	{
		this.pathToOverlay = pathToOverlay;
		setChanged();
		notifyObservers(ObserverArgument.PATH_OVERLAY);
	}

	public String getPathToWaitingScreen()
	{
		return pathToWaitingScreen;
	}

	public void setPathToWaitingScreen(final String pathToWaitingScreen)
	{
		this.pathToWaitingScreen = pathToWaitingScreen;
		setChanged();
		notifyObservers(ObserverArgument.PATH_WAITING_SCREEN);
	}

	public String getPathToArmaExe()
	{
		return pathToArmaExe;
	}

	public void setPathToArmaExe(final String pathToArmaExe)
	{
		this.pathToArmaExe = pathToArmaExe;
		setChanged();
		notifyObservers(ObserverArgument.PATH_ARMA);
	}

	public String getPathToJson()
	{
		return pathToJson;
	}

	public void setPathToJson(final String pathToJson)
	{
		this.pathToJson = pathToJson;
		setChanged();
		notifyObservers(ObserverArgument.PATH_JSON);
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(final String playerName)
	{
		this.playerName = playerName;
		setChanged();
		notifyObservers(ObserverArgument.PLAYER_NAME);
	}

	void createJson() throws IOException
	{
		final ClassLoader classLoader = getClass().getClassLoader();
		final InputStream inputStream = classLoader.getResourceAsStream("TTT.json.template");
		final Context context = new VelocityContext();
		context.put("mic", getSelectedMicroDevice().getDeviceId());
		context.put("desktop", getSelectedDesktopDevice().getDeviceId());
		if (!isSameSourceLikeDesktop())
		{
			context.put("TS", getSelectedTSDevice().getDeviceId());
		}
		context.put("name", getPlayerName());
		context.put("overlay_path", getPathToOverlay().replaceAll("\\\\", "/"));
		context.put("waiting_screen", getPathToWaitingScreen().replaceAll("\\\\", "/"));
		context.put("arma_path", "Arma 3 \\\"" + getPathToArmaExe().replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "#3A") + "\\\" -beservice:Arma 3:arma3_x64.exe");
		final File outFile = new File(getPathToJson());
		Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new NullLogChute());
		try (final Reader reader = new InputStreamReader(inputStream);
			 final FileWriter writer = new FileWriter(outFile))
		{
			Velocity.evaluate(context, writer, "CreateJson", reader);
		}
	}
}
