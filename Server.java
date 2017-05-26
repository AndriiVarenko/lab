package Server;

import java.io.IOException;

public class Server {

	public static void main(String[] args) throws IOException {
		System.out.println("Ласкаво просимо на сервер. Натисніть ENTER для вимкнення серверу.");

		try (RmiServer server = new RmiServer(args)) {
			server.run();
			System.in.read();
		}
		
		System.out.println("Сервер вимкнено.");
	}
}
