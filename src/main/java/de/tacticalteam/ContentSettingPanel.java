package de.tacticalteam;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ContentSettingPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JFileChooser fileChooser;

	public ContentSettingPanel()
	{
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		final GroupLayout layout = MainFrame.addGroupLayoutToPanel(this);
		final JComponent overlaySelector = new ContentSelectionPanel(this, "Pfad zum Overlay", path -> Main.getInstance().setPathToOverlay(path), new FileNameExtensionFilter("Bilder", "jpg", "jpeg", "png"), "https://files.tacticalteam.de/index.php/s/FyxaKacPppfPMMt/download?path=%2F&files=OverLay.png");
		add(overlaySelector);
		final JComponent waitingScreenSelector = new ContentSelectionPanel(this, "Pfad zum Waiting-Screen", path -> Main.getInstance().setPathToWaitingScreen(path), new FileNameExtensionFilter("Videos", "mp4", "m4v"), "https://files.tacticalteam.de/index.php/s/FyxaKacPppfPMMt/download?path=%2FWaitingscreen&files=Stream%20BG%20-%202020.m4v");
		add(waitingScreenSelector);
		final JComponent armaExeSelector = new ContentSelectionPanel(this, "Pfad zur ArmA3-Exe", path -> Main.getInstance().setPathToArmaExe(path), new FileNameExtensionFilter("Ausf\u00fchrbare Dateien", "exe"), null);
		add(armaExeSelector);
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(overlaySelector).addComponent(waitingScreenSelector).addComponent(armaExeSelector));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(overlaySelector).addComponent(waitingScreenSelector).addComponent(armaExeSelector));
		setName("Overlay und Waitingscreen einstellen");
	}

	public JFileChooser getFileChooser()
	{
		return fileChooser;
	}
}
