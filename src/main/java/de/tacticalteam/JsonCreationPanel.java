package de.tacticalteam;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import org.apache.commons.lang3.StringUtils;

public class JsonCreationPanel extends JPanel implements Observer
{
	private static final long serialVersionUID = 1L;

	private final JFileChooser fileChooser;
	private final JButton generateJsonButton;

	public JsonCreationPanel()
	{
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(new File("TTTStreamen_2020.json"));
		final FileNameExtensionFilter filter = new FileNameExtensionFilter("OBS Szenensammlung", "json");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setFileFilter(filter);

		final GroupLayout layout = MainFrame.addGroupLayoutToPanel(this);
		final JLabel nameLabel = new JLabel("Dein Name im Spiel");
		add(nameLabel);
		final JTextField nameField = new JTextField();
		nameField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(final DocumentEvent e)
			{
				try
				{
					Main.getInstance().setPlayerName(e.getDocument().getText(0, e.getDocument().getLength()));
				}
				catch (final BadLocationException ex)
				{}
			}

			@Override
			public void removeUpdate(final DocumentEvent e)
			{
				try
				{
					Main.getInstance().setPlayerName(e.getDocument().getText(0, e.getDocument().getLength()));
				}
				catch (final BadLocationException ex)
				{}
			}

			@Override
			public void changedUpdate(final DocumentEvent e)
			{
				try
				{
					Main.getInstance().setPlayerName(e.getDocument().getText(0, e.getDocument().getLength()));
				}
				catch (final BadLocationException ex)
				{}
			}
		});
		add(nameField);
		final JLabel textLabel = new JLabel("Wo soll die OBS-Importdatei erzeugt werden?");
		add(textLabel);
		final JTextField fileNameTextField = new JTextField();
		fileNameTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
		add(fileNameTextField);
		final JButton locateButton = new JButton("Speicherort ausw\u00e4hlen");
		locateButton.addActionListener(e -> {
			if (fileChooser.showSaveDialog(JsonCreationPanel.this) == JFileChooser.APPROVE_OPTION)
			{
				final File file = fileChooser.getSelectedFile();
				if (file != null)
				{
					fileNameTextField.setText(file.getAbsolutePath());
					Main.getInstance().setPathToJson(file.getAbsolutePath());
				}
			}
		});
		add(locateButton);
		final CheckListPanel checkListPanel = new CheckListPanel();
		add(checkListPanel);
		generateJsonButton = new JButton("OBS-Importdatei erzeugen");
		generateJsonButton.setEnabled(isJsonGenerationEnabled());
		generateJsonButton.addActionListener((e) -> {
			try
			{
				Main.getInstance().createJson();
				JOptionPane.showMessageDialog(generateJsonButton, "Die OBS-Importdatei wurde erfolgreich generiert.", "Aktion erfolgreich", JOptionPane.INFORMATION_MESSAGE);
			}
			catch (final IOException ex)
			{
				JOptionPane.showMessageDialog(generateJsonButton, "Die Datei konnte nicht gespeichert werden. " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
			}
		});
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(nameLabel).addComponent(nameField).addComponent(textLabel).addComponent(fileNameTextField).addComponent(locateButton).addComponent(checkListPanel).addComponent(generateJsonButton));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(nameLabel).addComponent(nameField).addComponent(textLabel).addComponent(fileNameTextField).addComponent(locateButton).addComponent(checkListPanel).addComponent(generateJsonButton));
		setName("OBS-Importdatei erzeugen");
		Main.getInstance().addObserver(this);
	}

	@Override
	public void update(final Observable observable, final Object arg)
	{
		generateJsonButton.setEnabled(isJsonGenerationEnabled());
	}

	private boolean isJsonGenerationEnabled()
	{
		final Main main = Main.getInstance();
		return main.getSelectedDesktopDevice() != null &&
			main.getSelectedMicroDevice() != null &&
			(main.getSelectedTSDevice() != null || main.isSameSourceLikeDesktop()) &&
			StringUtils.isNotBlank(main.getPathToOverlay()) &&
			StringUtils.isNotBlank(main.getPathToWaitingScreen()) &&
			StringUtils.isNotBlank(main.getPathToArmaExe()) &&
			StringUtils.isNotBlank(main.getPathToJson());
	}
}
