package Client;

import java.io.*;
import java.util.*;
import java.rmi.RemoteException;
import Server.IServer;
import Server.IServer.*;

public class CommandProcessing {

    private static final int ECHO_PARAMETERS_LENGTH = 2;
    private static final int LOGIN_PARAMETERS_LENGTH = 3;
    private static final int MESSAGE_PARAMETERS_LENGTH = 3;
    private static final int FILE_PARAMETERS_LENGTH = 3;

    private final IServer proxy;

    public CommandProcessing(IServer proxy) {
        this.proxy = proxy;
    }

    public void ping() throws RemoteException {
    	System.out.println("Перевірка з'єднання з сервером");
    	proxy.ping();
        System.out.println("Сервер доступний");
    }

    public void echo(String[] comandMas) throws RemoteException {
        if (isNormalLength(comandMas, ECHO_PARAMETERS_LENGTH)) {
            System.out.println(proxy.echo(comandMas[1]));
        }
    }

    private final Timer timer = new Timer();
    private static String myLogin = null;
    private static String sessionId = null;

    public void login(String[] comandMas) throws RemoteException {
        if (!isNormalLength(comandMas, LOGIN_PARAMETERS_LENGTH)) {
            return;
        }
        String login = comandMas[1];
        String password = comandMas[2];

        if (login.equals(myLogin)) {
            System.out.println("Авторизація пройшла успішно");
            return;
        }
        if (sessionId != null) {
        	proxy.exit(sessionId);
        }
        sessionId = proxy.login(login, password);
        if (myLogin == null) {
            timer.schedule(receive, 0, 1500);
        }
        myLogin = login;
    }

    public void list() throws RemoteException {
        if (isLogged(sessionId)) {
            String[] onlineUser = proxy.listUsers(sessionId);
            if (onlineUser != null) {
                Arrays.toString(onlineUser);
            }
        }
    }

    public void file(String[] comandMas) throws IOException {
//        if (!isLogged(sessionId) || !isNormalLength(comandMas, FILE_PARAMETERS_LENGTH) || !isItUser(comandMas[1])) {
//            return;
//        }
        File file = new File(comandMas[1]);
        if (!file.exists()) {
            System.out.println("Файлова помилка");
            return;
        }
        byte [] b = new byte [(int) file.length()];
        try {
        	b = proxy.sendFile(file);
        	System.out.println("Файл надіслано");
        	System.out.println("file: " + "\n" + file + "\n" + "b: " + "\n" + b);
        	
        } catch (Exception e) {
        	System.out.println("Не вдалося надіслати файл");
        	System.exit(1);
        }
    }

    public void receiveFile() throws RemoteException {
        if (!isLogged(sessionId)) {
            return;
        }
        FileInfo file = proxy.receiveFile(sessionId);
        if (file == null) {
            if (flug) {
                System.out.println("Немає файлу");
            }
            return;
        }
        System.out.println(file.toString());
        try (FileOutputStream fos = new FileOutputStream(
                new File(file.getSender() + "_" + file.getFilename()))) {
            fos.write(file.getFileContent());
        } catch (Exception ex) {
            System.out.println("Проблеми з файлом");
        }
    }

    public void exit() throws RemoteException {
        timer.cancel();
        proxy.exit(sessionId);
        System.out.println("Вихід із сервера");
        Client.flug = false;
    }

    private boolean isNormalLength(String[] comandMas, int validLength) {
        return comandMas.length == validLength ? true : badArgument();
    }

    private boolean badArgument() {
        System.out.println("Невірні аргументи");
        return false;
    }

    private boolean isLogged(String sessionId) {
        return sessionId != null ? true : noLoged();
    }

    private boolean noLoged() {
        System.out.println("Зареєструйтеся");
        return true;
    }

    private boolean isItUser(String user) {
        return this.userStorage.contains(user) ? true : noUser();
    }

    private boolean noUser() {
        System.out.println("Такого користувача немає");
        return false;
    }

    private boolean flug;
    TimerTask receive = new TimerTask() {

        @Override
        public void run() {
            try {
                flug = false;
                receiveFile();
                activeUser();
                flug = true;
            } catch (RemoteException ex) {
                System.out.println("Проблеми з таймером");
            }
        }
    };

    private final List<String> userStorage = new LinkedList<>();

    private void activeUser() throws RemoteException {
        if (sessionId != null) {
            List<String> onlineUser = Arrays.asList(proxy.listUsers(sessionId));

            if (onlineUser == null) {
                return;
            }
            for (String user : onlineUser) {
                if (!userStorage.contains(user)) {
                    userStorage.add(user);
                    System.out.println("Користувач " + user + " увійшов");
                }
            }
            for (String user : userStorage) {
                if (!onlineUser.contains(user)) {
                    System.out.println("Користувач " +user + " вийшов");
                    userStorage.remove(user);
                }
            }
        }
    }
}