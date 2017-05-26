package Client;
public class Parser {
    
    public String[] parsForComand(String line) {
        String[] parsMas = line.split(" ", 2);

        switch (parsMas[0]) {
            case "echo":
                return line.split(" ", 2); // comand _ anyText                  
                
            default:
                return line.split(" ");
        }
    }    
}