package de.tacticalteam;

import com.wmi.windows.AudioDevice;
import java.util.stream.Stream;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_AUDIO_STRING = "Standard";
	private final Main main;

	public MainFrame(final Main main)
	{
		this.main = main;
		setTitle("OBS Scene Creator");
		setSize(640, 380);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		addAudioSettingsPanel();
		setVisible(true);
	}

	private void addAudioSettingsPanel()
	{
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createMicroAudioDropdown());
		panel.add(createDesktopAudioDropdown());
		panel.add(createTSAudioDropdown());
		add(panel);
	}

	private JComponent createDesktopAudioDropdown()
	{
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		final JLabel textLabel = new JLabel("W\u00e4hle die Audioquelle f\u00fcr den Spielsound aus:");
		panel.add(textLabel);
		final JComboBox<AudioDevice> comboBox = new JComboBox<>(
			Stream.concat(Stream.of(AudioDevice.DEFAULT_OUTPUT), main.getAudioDevices().stream().filter(audioDevice -> !audioDevice.isAudioInputDevice())).toArray(AudioDevice[]::new));
		comboBox.addActionListener(e -> {
			@SuppressWarnings("unchecked")
			final JComboBox<AudioDevice> aComboBox = (JComboBox<AudioDevice>)e.getSource();
			final AudioDevice selectedAudioDevice = (AudioDevice)aComboBox.getSelectedItem();
			main.setSelectedDesktopDevice(selectedAudioDevice);
		});
		panel.add(comboBox);
		return panel;
	}

	private JComponent createMicroAudioDropdown()
	{
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		final JLabel textLabel = new JLabel("W\u00e4hle dein Mikrofon aus:");
		panel.add(textLabel);
		final JComboBox<AudioDevice> comboBox = new JComboBox<>(Stream.concat(Stream.of(AudioDevice.DEFAULT_INPUT), main.getAudioDevices().stream().filter(AudioDevice::isAudioInputDevice)).toArray(AudioDevice[]::new));
		comboBox.addActionListener(e -> {
			@SuppressWarnings("unchecked")
			final JComboBox<AudioDevice> aComboBox = (JComboBox<AudioDevice>)e.getSource();
			final AudioDevice selectedAudioDevice = (AudioDevice)aComboBox.getSelectedItem();
			main.setSelectedMicroDevice(selectedAudioDevice);
		});
		panel.add(comboBox);
		return panel;
	}

	private JComponent createTSAudioDropdown()
	{
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		final JLabel textLabel = new JLabel("W\u00e4hle die Audioquelle f\u00fcr Teamspeak aus:");
		panel.add(textLabel);
		final JComboBox<AudioDevice> comboBox = new JComboBox<>(
			Stream.concat(Stream.of(AudioDevice.DEFAULT_OUTPUT), main.getAudioDevices().stream().filter(audioDevice -> !audioDevice.isAudioInputDevice())).toArray(AudioDevice[]::new));
		comboBox.setEnabled(!main.isSameSourceLikeDesktop());
		comboBox.addActionListener(e -> {
			@SuppressWarnings("unchecked")
			final JComboBox<AudioDevice> aComboBox = (JComboBox<AudioDevice>)e.getSource();
			final AudioDevice selectedAudioDevice = (AudioDevice)aComboBox.getSelectedItem();
			main.setSelectedTSDevice(selectedAudioDevice);
		});
		panel.add(comboBox);
		final JCheckBox checkBox = new JCheckBox("Selbe Audioquelle wie Spielsound", main.isSameSourceLikeDesktop());
		checkBox.addActionListener(e -> {
			final JCheckBox aCheckBox = (JCheckBox)e.getSource();
			final boolean selected = aCheckBox.isSelected();
			comboBox.setEnabled(!selected);
			main.setSameSourceLikeDesktop(selected);
		});
		panel.add(checkBox);
		return panel;
	}
}
