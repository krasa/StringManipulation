package osmedile.intellij.stringmanip;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Donate {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(Donate.class);

	public static JComponent newDonateButton(JPanel donatePanel) {
		JButton donate = new JButton();
		donate.setBorder(null);
		donate.setIcon(IconLoader.getIcon("donate.gif", Donate.class));
		donate.setContentAreaFilled(false);
		donate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BrowserUtil.browse("https://www.paypal.me/VojtechKrasa");
			}
		});
		donate.putClientProperty("JButton.backgroundColor", donatePanel.getBackground());
		return donate;
	}
}
