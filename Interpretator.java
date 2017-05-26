package Client;

import Server.IServer;

public class Interpretator {
    private final CommandProcessing comP;
    private final Parser parser = new Parser(); 

    public Interpretator(IServer ob) {
        comP = new CommandProcessing(ob);        
    }
    
    public void interpretator(String inLine){
        String[] comandMas = parser.parsForComand(inLine);
        try {
            switch (comandMas[0]) {
                case "ping":
                    comP.ping();
                    break;
                case "echo":
                    comP.echo(comandMas);
                    break;
                case "login":
                    comP.login(comandMas);
                    break;
                case "list":
                    comP.list();
                    break;
                case "file":
                    comP.file(comandMas);
                    break;
                case "receivefile":
                    comP.receiveFile();
                    break;
                case "exit":
                    comP.exit();
                    break;
                default:
                    System.out.println("Такої команди не існує");
                    break;
            }
        } catch (Exception ex) {
            System.out.println("Помилка інтерпретатора");
        }
    }
}