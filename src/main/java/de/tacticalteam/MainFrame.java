package de.tacticalteam;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public MainFrame()
	{
		setTitle("OBS Scene Creator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add(new AudioSettingPanel());
		tabbedPane.add(new ContentSettingPanel());
		tabbedPane.add(new JsonCreationPanel());
		add(tabbedPane);
		pack();
		setVisible(true);
	}

	static GroupLayout addGroupLayoutToPanel(final JPanel panel)
	{
		final GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		return layout;
	}
}
