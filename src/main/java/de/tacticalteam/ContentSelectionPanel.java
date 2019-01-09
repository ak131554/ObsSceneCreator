package de.tacticalteam;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.function.Consumer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.lang3.StringUtils;

public class ContentSelectionPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public ContentSelectionPanel(final ContentSettingPanel contentSettingPanel, final String text, final Consumer<String> consumer, final FileNameExtensionFilter fileFilter, final String downloadUrl)
	{
		final GroupLayout layout = MainFrame.addGroupLayoutToPanel(this);
		final JLabel textLabel = new JLabel(text);
		add(textLabel);
		final JTextField fileNameTextField = new JTextField();
		fileNameTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
		add(fileNameTextField);
		final JButton locateButton = new JButton("Quelle lokalisieren");
		locateButton.addActionListener(e -> {
			final JFileChooser fileChooser = contentSettingPanel.getFileChooser();
			fileChooser.addChoosableFileFilter(fileFilter);
			final FileFilter oldFilter = fileChooser.getFileFilter();
			fileChooser.setFileFilter(fileFilter);
			if (fileChooser.showDialog(ContentSelectionPanel.this, "Datei benutzen") == JFileChooser.APPROVE_OPTION)
			{
				final File file = fileChooser.getSelectedFile();
				if (file != null)
				{
					fileNameTextField.setText(file.getAbsolutePath());
					consumer.accept(file.getAbsolutePath());
				}
			}
			fileChooser.setFileFilter(oldFilter);
			fileChooser.removeChoosableFileFilter(fileFilter);
			fileChooser.setSelectedFile(new File(""));
		});
		add(locateButton);
		if (StringUtils.isNotBlank(downloadUrl))
		{
			final JButton downloadButton = new JButton("Quelle herunterladen");
			downloadButton.addActionListener(e -> {
				final Download download;
				try
				{
					download = new Download(new URL(downloadUrl));
				}
				catch (final MalformedURLException ex)
				{
					JOptionPane.showMessageDialog(ContentSelectionPanel.this, "Ein unerwarteter Fehler ist beim Herunterladen aufgetreten. Lade dir die Datei von https://cloud.monclefu.de/index.php/s/F8eoCe2oN62WQex selbst herunter und w\u00e4hle hier Quelle lokalisieren.", "Fehler beim Herunterladen", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
					return;
				}
				final JFileChooser fileChooser = contentSettingPanel.getFileChooser();
				final FileNameExtensionFilter downloadFilter = new FileNameExtensionFilter(fileFilter.getDescription(), downloadUrl.substring(downloadUrl.lastIndexOf(".") + 1));
				fileChooser.addChoosableFileFilter(downloadFilter);
				final FileFilter oldFilter = fileChooser.getFileFilter();
				fileChooser.setFileFilter(downloadFilter);
				try
				{
					fileChooser.setSelectedFile(new File(URLDecoder.decode(downloadUrl.substring(downloadUrl.lastIndexOf("=") + 1), "UTF-8")));
				}
				catch (final UnsupportedEncodingException ex)
				{
					// Do nothing
				}
				if (fileChooser.showSaveDialog(ContentSelectionPanel.this) == JFileChooser.APPROVE_OPTION)
				{
					final String fileName = fileChooser.getSelectedFile().getAbsolutePath();
					download.setFileName(fileName);
					download.start();
					final JDialog downloadWindow = new JDialog((JFrame)SwingUtilities.getWindowAncestor(ContentSelectionPanel.this), fileChooser.getSelectedFile().getName() + " - Downloading", true);
					final JPanel panel = new JPanel();
					final GroupLayout downloadPanelLayout = MainFrame.addGroupLayoutToPanel(panel);
					final JProgressBar progressBar = new JProgressBar(0, 100);
					progressBar.setVisible(true);
					progressBar.setStringPainted(true);
					panel.add(progressBar);

					final double speed = download.getSpeed();
					final JLabel speedLabel = new JLabel(speed < 1024.0 ? MessageFormat.format("Geschwindigkeit: {0} kB/s", Double.valueOf(speed)) : MessageFormat.format("Geschwindigkeit: {0} MB/s", Double.valueOf(speed / 1024d)));
					panel.add(speedLabel);
					downloadWindow.add(panel);
					downloadPanelLayout.setHorizontalGroup(downloadPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(progressBar).addComponent(speedLabel));
					downloadPanelLayout.setVerticalGroup(downloadPanelLayout.createSequentialGroup().addComponent(progressBar).addComponent(speedLabel));

					final Timer timer = new Timer(200, (timerActionListener) -> {
						progressBar.setValue(download.getProgress());
						final double currentSpeed = download.getSpeed();
						speedLabel.setText(currentSpeed < 1024.0 ? MessageFormat.format("Geschwindigkeit: {0} kB/s", Double.valueOf(currentSpeed)) : MessageFormat.format("Geschwindigkeit: {0} MB/s", Double.valueOf(currentSpeed / 1024d)));
					});
					timer.start();
					downloadWindow.setSize(240,100);
					downloadWindow.setLocationRelativeTo(ContentSelectionPanel.this);
					downloadWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					downloadWindow.setVisible(true);
					downloadWindow.addWindowListener(new WindowAdapter()
					{
						@Override
						public void windowClosed(final WindowEvent e)
						{
							super.windowClosed(e);
							fileNameTextField.setText(fileName);
							consumer.accept(fileName);
						}
					});
				}
				fileChooser.setFileFilter(oldFilter);
				fileChooser.removeChoosableFileFilter(downloadFilter);
				fileChooser.setSelectedFile(new File(""));
			});
			add(downloadButton);
			layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(textLabel).addComponent(fileNameTextField).addGroup(layout.createSequentialGroup().addComponent(locateButton).addComponent(downloadButton)));
			layout.setVerticalGroup(layout.createSequentialGroup().addComponent(textLabel).addComponent(fileNameTextField).addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(locateButton).addComponent(downloadButton)));
		}
		else
		{
			layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(textLabel).addComponent(fileNameTextField).addComponent(locateButton));
			layout.setVerticalGroup(layout.createSequentialGroup().addComponent(textLabel).addComponent(fileNameTextField).addComponent(locateButton));
		}
	}
}
