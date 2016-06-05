package com.sshtools.forker.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class OS {

	public enum Desktop {
		GNOME, KDE, CINNAMON, XFCE, GNOME3, WINDOWS, MAC_OSX, MAC, OTHER, UNITY, LXDE, CONSOLE, NONE
	}

	public static boolean isRunningOnDesktop() {
		return !getDesktopEnvironment().equals(Desktop.CONSOLE);
	}

	public static String getAdministratorUsername() {
		if (SystemUtils.IS_OS_WINDOWS) {
			return System.getProperty("forker.administratorUsername",
					System.getProperty("vm.rootUser", "Administrator"));
		}
		if (SystemUtils.IS_OS_UNIX) {
			return System.getProperty("forker.administratorUsername", System.getProperty("vm.rootUser", "root"));
		}
		throw new UnsupportedOperationException();
	}

	public static boolean isAdministrator() {
		if (SystemUtils.IS_OS_WINDOWS) {
			try {
				String programFiles = System.getenv("ProgramFiles");
				if (programFiles == null) {
					programFiles = "C:\\Program Files";
				}
				File temp = new File(programFiles, "foo.txt");
				temp.deleteOnExit();
				if (temp.createNewFile()) {
					temp.delete();
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				return false;
			}
		}
		if (SystemUtils.IS_OS_UNIX) {
			return System.getProperty("forker.administratorUsername", System.getProperty("vm.rootUser", "root"))
					.equals(System.getProperty("user.name"));
		}
		throw new UnsupportedOperationException();
	}

	public static Desktop getDesktopEnvironment() {
		// TODO more to do - see the following links for lots of info

		// http://unix.stackexchange.com/questions/116539/how-to-detect-the-desktop-environment-in-a-bash-script
		// http://askubuntu.com/questions/72549/how-to-determine-which-window-manager-is-running/227669#227669

		String desktopSession = System.getenv("XDG_CURRENT_DESKTOP");
		String gdmSession = System.getenv("GDMSESSION");
		if (SystemUtils.IS_OS_WINDOWS) {
			return Desktop.WINDOWS;
		}
		if (SystemUtils.IS_OS_MAC_OSX) {
			return Desktop.MAC_OSX;
		}
		if (SystemUtils.IS_OS_MAC) {
			return Desktop.MAC;
		}
		if (SystemUtils.IS_OS_LINUX && StringUtils.isBlank(System.getenv("DISPLAY"))) {
			return Desktop.CONSOLE;
		}

		if ("X-Cinnamon".equalsIgnoreCase(desktopSession)) {
			return Desktop.CINNAMON;
		}
		if ("LXDE".equalsIgnoreCase(desktopSession)) {
			return Desktop.LXDE;
		}
		if ("XFCE".equalsIgnoreCase(desktopSession)) {
			return Desktop.XFCE;
		}
		if ("KDE".equalsIgnoreCase(desktopSession)
				|| (StringUtils.isBlank(desktopSession) && "kde-plasma".equals(gdmSession))) {
			return Desktop.KDE;
		}
		if ("UNITY".equalsIgnoreCase(desktopSession)) {
			return Desktop.UNITY;
		}
		if ("GNOME".equalsIgnoreCase(desktopSession)) {
			if ("gnome-shell".equals(gdmSession)) {
				return Desktop.GNOME3;
			}
			return Desktop.GNOME;
		}
		return Desktop.OTHER;
	}

	public static String getJavaPath() {
		String javaExe = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		if (SystemUtils.IS_OS_WINDOWS)
			javaExe += ".exe";
		return javaExe;
	}
}
