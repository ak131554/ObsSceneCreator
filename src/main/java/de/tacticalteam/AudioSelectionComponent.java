package de.tacticalteam;

import com.wmi.windows.AudioDevice;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AudioSelectionComponent extends JPanel
{
	private static final long serialVersionUID = 1L;

	public AudioSelectionComponent(final String text, final boolean onlyInput, final ActionListener actionListener)
	{
		this(text, onlyInput, actionListener, null, null, true);
	}

	public AudioSelectionComponent(final String text, final boolean onlyInput, final ActionListener actionListener, final String optionalText, final BiConsumer<JComboBox<AudioDevice>, ActionEvent> optionalActionListener,
		final boolean optionalInitiallySelected)
	{
		final GroupLayout layout = MainFrame.addGroupLayoutToPanel(this);
		final JLabel textLabel = new JLabel(text);
		add(textLabel);
		final JComboBox<AudioDevice> comboBox = new JComboBox<>(
			Stream.concat(Stream.of(AudioDevice.DEFAULT_OUTPUT), Main.getInstance().getAudioDevices().stream().filter(audioDevice -> audioDevice.isAudioInputDevice() == onlyInput)).toArray(AudioDevice[]::new));
		comboBox.addActionListener(actionListener);
		comboBox.setMaximumSize(new Dimension(300,20));
		add(comboBox);
		if (optionalActionListener != null)
		{
			comboBox.setEnabled(!optionalInitiallySelected);
			final JCheckBox checkBox = new JCheckBox(optionalText, optionalInitiallySelected);
			checkBox.addActionListener(e -> optionalActionListener.accept(comboBox, e));
			add(checkBox);
			layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(textLabel).addGroup(layout.createSequentialGroup().addComponent(comboBox).addComponent(checkBox)));
			layout.setVerticalGroup(layout.createSequentialGroup().addComponent(textLabel).addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(comboBox).addComponent(checkBox)));
		}
		else
		{
			layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(textLabel).addComponent(comboBox));
			layout.setVerticalGroup(layout.createSequentialGroup().addComponent(textLabel).addComponent(comboBox));
		}
	}
}
