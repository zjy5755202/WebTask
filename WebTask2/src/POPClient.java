import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class POPClient {

    public static void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        POPCommands popCommands;
        BufferedReader in = null;
        BufferedWriter out = null;
        String popServer;
        int Port;
        Socket socket = null;


        System.out.println("Welcome to user zhuzhu's pop3 server");
        System.out.println("Please input your popServer'url and port");
        System.out.println("Usage:pop.163.com 110");
        //conn
        while (true) {
            String[] conn = scanner.nextLine().split(" ");
            if (conn.length != 2) {
                System.out.println("Usage:pop.163.com 110");
                continue;
            }
            popServer = conn[0];
            Port = Integer.parseInt(conn[1]);

            try {
                socket = new Socket(popServer, Port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Create Socket fail!");
                System.out.println("Please check your popServer'url and port");
                continue;
            }

            popCommands = new POPCommands(in, out);
            String connResult = popCommands.getReturn();
            if (!connResult.startsWith("+OK")) {
                System.out.println("Cannot connect to mail server");
                continue;
            }
            break;
        }


        //conn success
        //Login
        System.out.println("Success connect to popServer");
        System.out.println("Login");
        System.out.println("Please Input your username");
        System.out.println("Usage:user username");
        while (true) {
            String[] userParam = scanner.nextLine().split(" ");
            if(userParam.length!=2){
                System.out.println("Usage:user username");
                continue;
            }
            String username = userParam[1];
            String userresult = popCommands.user(username);
            if (!userresult.startsWith("+OK")) {
                System.out.println("WRONG USER!PLEASE CHECK YOUR USERNAME");
                continue;
            }
            System.out.println("Please Input your password");
            System.out.println("Usage:pass password");
            String[] passParam = scanner.nextLine().split(" ");
            if(passParam.length!=2){
                System.out.println("Usage:pass password");
                continue;
            }
            String password = passParam[1];
            String passresult = popCommands.pass(password);
            if (!passresult.startsWith("+OK")) {
                System.out.println("WRONG PASS!PLEASE CHECK YOUR PASSWORD");
                continue;
            }
            break;
        }


        System.out.println("Success Login");
        System.out.println("Now you can use command like:stat,list,dele,retr,quit");
        //use commands
        while (true) {
            String[] passParam = scanner.nextLine().split(" ");
            switch (passParam[0]) {
                case "stat":
                    if (passParam.length != 1) {
                        System.out.println("Usage:stat");
                        continue;
                    }
                    System.out.println(popCommands.stat());
                    break;
                case "list":
                    if (passParam.length > 2) {
                        System.out.println("Usage:list");
                        System.out.println("Usage:list mailNum");
                        continue;
                    }
                    if (passParam.length == 1) {
                        System.out.println(popCommands.list());
                    } else {
                        System.out.println(popCommands.list(Integer.parseInt(passParam[1])));
                    }
                    break;
                case "dele":
                    if (passParam.length != 2) {
                        System.out.println("Usage:dele mailNum");
                        continue;
                    }
                    popCommands.dele(Integer.parseInt(passParam[1]));
                    break;
                case "retr":
                    if (passParam.length != 2) {
                        System.out.println("Usage:retr mailNum");
                        continue;
                    }
                    popCommands.retr(Integer.parseInt(passParam[1]));
                    break;
                case "quit":
                    if (passParam.length != 1) {
                        System.out.println("Usage:quit");
                        continue;
                    }
                    System.out.println(popCommands.quit());
                    return;

            }


        }


    }


    public static void main(String[] args) throws IOException {
        POPClient.run();
    }
}
