package de.tacticalteam;

import com.wmi.windows.AudioDevice;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class AudioSettingPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public AudioSettingPanel()
	{
		final GroupLayout layout = MainFrame.addGroupLayoutToPanel(this);
		final JComponent desktopAudioDropdown = new AudioSelectionComponent("W\u00e4hle dein Mikrofon aus:", true, e -> {
			@SuppressWarnings("unchecked") final JComboBox<AudioDevice> aComboBox = (JComboBox<AudioDevice>) e.getSource();
			final AudioDevice selectedAudioDevice = (AudioDevice) aComboBox.getSelectedItem();
			Main.getInstance().setSelectedMicroDevice(selectedAudioDevice);
		});
		add(desktopAudioDropdown);
		final JComponent microAudioDropdown = new AudioSelectionComponent("W\u00e4hle die Audioquelle f\u00fcr den Spielsound aus:", false, e -> {
			@SuppressWarnings("unchecked") final JComboBox<AudioDevice> aComboBox = (JComboBox<AudioDevice>) e.getSource();
			final AudioDevice selectedAudioDevice = (AudioDevice) aComboBox.getSelectedItem();
			Main.getInstance().setSelectedDesktopDevice(selectedAudioDevice);
		});
		add(microAudioDropdown);
		final JComponent tsAudioDropdown = new AudioSelectionComponent("W\u00e4hle die Audioquelle f\u00fcr Teamspeak aus:", false, e -> {
			@SuppressWarnings("unchecked") final JComboBox<AudioDevice> aComboBox = (JComboBox<AudioDevice>) e.getSource();
			final AudioDevice selectedAudioDevice = (AudioDevice) aComboBox.getSelectedItem();
			Main.getInstance().setSelectedTSDevice(selectedAudioDevice);
		}, "Selbe Audioquelle wie Spielsound", (comboBox, e) -> {
			final JCheckBox aCheckBox = (JCheckBox) e.getSource();
			final boolean selected = aCheckBox.isSelected();
			comboBox.setEnabled(!selected);
			Main.getInstance().setSameSourceLikeDesktop(selected);
		}, Main.getInstance().isSameSourceLikeDesktop());
		add(tsAudioDropdown);
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(microAudioDropdown).addComponent(desktopAudioDropdown).addComponent(tsAudioDropdown));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(microAudioDropdown).addComponent(desktopAudioDropdown).addComponent(tsAudioDropdown));
		setName("Audioquellen einstellen");
	}
}
