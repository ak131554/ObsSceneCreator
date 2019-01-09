/*
 * Icons used in this panel: Roundicons from www.flaticon.com is licensed by CC 3.0 BY
 */
package de.tacticalteam;

import de.tacticalteam.Main.ObserverArgument;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CheckListPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public CheckListPanel()
	{
		final GroupLayout layout = MainFrame.addGroupLayoutToPanel(this);
		final Main main = Main.getInstance();
		final Group horizontalGroup = layout.createParallelGroup(Alignment.LEADING);
		final Group verticalGroup = layout.createSequentialGroup();
		add(createLabel("Audioquelle f\u00fcr Spielsound: ", main::getSelectedDesktopDevice, horizontalGroup, verticalGroup, ObserverArgument.SELECTED_DESKTOP));
		add(createLabel("Audioquelle f\u00fcr Mikrofon: ", main::getSelectedMicroDevice, horizontalGroup, verticalGroup, ObserverArgument.SELECTED_MICRO));
		add(createLabel("Audioquelle f\u00fcr TeamSpeak: ", () -> main.isSameSourceLikeDesktop() ? main.getSelectedDesktopDevice() : main.getSelectedTSDevice(), horizontalGroup, verticalGroup, ObserverArgument.SELECTED_TS,
			ObserverArgument.SAME_SOURCE_LIKE_DESKTOP));
		add(createLabel("Pfad zum Overlay: ", main::getPathToOverlay, horizontalGroup, verticalGroup, ObserverArgument.PATH_OVERLAY));
		add(createLabel("Pfad zum Waiting-Screen: ", main::getPathToWaitingScreen, horizontalGroup, verticalGroup, ObserverArgument.PATH_WAITING_SCREEN));
		add(createLabel("Pfad zur Arma3-Exe: ", main::getPathToArmaExe, horizontalGroup, verticalGroup, ObserverArgument.PATH_ARMA));
		add(createLabel("Spielername: ", main::getPlayerName, horizontalGroup, verticalGroup, ObserverArgument.PLAYER_NAME));
		add(createLabel("Ziel f\u00fcr OBS-Importdatei: ", main::getPathToJson, horizontalGroup, verticalGroup, ObserverArgument.PATH_JSON));
		layout.setHorizontalGroup(horizontalGroup);
		layout.setVerticalGroup(verticalGroup);
	}

	private ObservingJLabel createLabel(final String text, final SerializableSupplier<Object> supplier, final Group horizontalGroup, final Group verticalGroup, final ObserverArgument... observerArgument)
	{
		final ObservingJLabel label = new ObservingJLabel(text, supplier.get())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void update(final Observable observable, final Object argument)
			{
				if (Arrays.asList(observerArgument).contains(argument))
				{
					updateValue(supplier.get());
				}
			}
		};
		horizontalGroup.addComponent(label);
		verticalGroup.addComponent(label);
		Main.getInstance().addObserver(label);
		return label;
	}

	private static abstract class ObservingJLabel extends JLabel implements Observer
	{
		private static final long serialVersionUID = 1L;

		private ImageIcon CHECKED = null;
		private ImageIcon CANCEL = null;

		private final String text;

		public ObservingJLabel(final String text, final Object value)
		{
			super(text + (value != null ? value.toString() : "n/a"), JLabel.LEADING);
			this.text = text;
			setIcon(value != null ? getChecked() : getCancel());
		}

		public void updateValue(final Object value)
		{
			setText(text + (value != null ? value.toString() : "n/a"));
			setIcon(value != null ? getChecked() : getCancel());
		}

		private ImageIcon getChecked()
		{
			if (CHECKED == null)
			{
				try
				{
					BufferedImage image = ImageIO.read(getClass().getResource("checked.png"));
					CHECKED = new ImageIcon(image);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			return CHECKED;
		}

		private ImageIcon getCancel()
		{
			if (CANCEL == null)
			{
				try
				{
					BufferedImage image = ImageIO.read(getClass().getResource("cancel.png"));
					CANCEL = new ImageIcon(image);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			return CANCEL;
		}
	}

	private interface SerializableSupplier<T> extends Supplier<T>, Serializable
	{

	}
}
