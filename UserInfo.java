package Server;

import java.util.ArrayList;
import java.util.List;

import Server.IServer.FileInfo;

public class UserInfo {
	private static final int MAX_PENDING_MESSAGES = 100;

	private final Object syncRoot = new Object();
	private final String login;
	private final String password;
	private final List<FileInfo> files = new ArrayList<>();

	public UserInfo(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return this.login;
	}

	public boolean canLogin(String login, String password) {
		return this.login.equals(login) && this.password.equals(password);
	}

	public boolean addFile(FileInfo file) {
		synchronized (syncRoot) {
			if (this.files.size() >= MAX_PENDING_MESSAGES)
				return false;

			this.files.add(file);
			return true;
		}
	}

	public FileInfo popFile() {
		synchronized (syncRoot) {
			if (this.files.size() == 0)
				return null;

			return this.files.remove(0);
		}
	}
}
