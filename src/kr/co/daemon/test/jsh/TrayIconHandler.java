package kr.co.daemon.test.jsh;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrayIconHandler {
	
	private static Logger LOGGER = LoggerFactory.getLogger(TrayIconHandler.class);
	
	private static TrayIcon trayIcon;
//	= new TrayIcon(createImage("images/bulb.gif", "innerIcon"));;
	
	private static PopupMenu getPopupMenu() {
		PopupMenu popupMenu = trayIcon.getPopupMenu();

		if (popupMenu == null) {
			popupMenu = new PopupMenu();
		}

		return popupMenu;
	}
	
	private static void add(MenuItem item) {
		if (!isRegistered()) {
			return;
		}

		PopupMenu popupMenu = getPopupMenu();
		popupMenu.add(item);

		trayIcon.setPopupMenu(popupMenu);
	}
	
	public static boolean isRegistered() {
		return (trayIcon != null && getPopupMenu() != null) ? true : false;
	}
	
	public static void registerTrayIcon(Image image, String toolTip,
			ActionListener action) {
		if (SystemTray.isSupported()) {
			if (trayIcon != null) {
				trayIcon = new TrayIcon(createImage("img/blue_icon.png", "innerIcon"));;
			}

			trayIcon = new TrayIcon(image);
			trayIcon.setImageAutoSize(true);

			if (toolTip != null) {
				trayIcon.setToolTip(toolTip);
			}

			if (action != null) {
				trayIcon.addActionListener(action);
			}

			try {
				for (TrayIcon registeredTrayIcon : SystemTray.getSystemTray()
						.getTrayIcons()) {
					SystemTray.getSystemTray().remove(registeredTrayIcon);
				}

				SystemTray.getSystemTray().add(trayIcon);
			} catch (AWTException e) {
				LOGGER.error("I got catch an error during add system tray !", e);
			}
		} else {
			LOGGER.error("System tray is not supported !");
		}
	}
	
	public static void setToolTip(String toolTip) {
		if (!isRegistered()) {
			return;
		}

		trayIcon.setToolTip(toolTip);
	}
	
	public static void setImage(Image image) {
		if (!isRegistered()) {
			return;
		}

		trayIcon.setImage(image);
	}
	
	public static void addMenu(String menu) {
		add(new Menu(menu));
	}
	
	public static void addItem(String label, ActionListener action) {
		MenuItem menuItem = new MenuItem(label);
		menuItem.addActionListener(action);

		add(menuItem);
	}
	
	public static void displayMessage(String caption, String text, MessageType messageType) {
		if (isRegistered()) {
			return;
		}

		trayIcon.displayMessage(caption, text, messageType);
	}
	
	protected static Image createImage(String path, String description){
		URL imageURL = TrayIconHandler.class.getResource(path);
		
		System.out.println("imageURL : "+imageURL.toString());
		
		if(imageURL.equals(null)){
			System.err.println("해당 경로에서 찾을 수 없습니다" + path);
			return null;
		}else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}
	
}
