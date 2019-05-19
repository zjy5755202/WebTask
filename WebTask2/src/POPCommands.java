import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class POPCommands {
    BufferedReader in;
    BufferedWriter out;

    public POPCommands(BufferedReader in, BufferedWriter out) {
        this.in = in;
        this.out = out;
    }

    public String getReturn() throws IOException {
        String line="";
        try {
            line=in.readLine();
        }catch (Exception e){
            e.printStackTrace();
        }
        return line;
    }

    public String getResult(String line) {
        StringTokenizer st = new StringTokenizer(line, " ");
        return st.nextToken();
    }

    public String sendServer(String str) throws IOException {
        out.write(str);
        out.newLine();
        out.flush();
        return getReturn();
    }

    //传递用户名
    public String user(String username) throws IOException {
        return sendServer("user "+username);
    }
    //传递密码
    public String pass(String password) throws IOException {
        return sendServer("pass "+password);
    }
    //统计邮件数量以及内存大小
    public String stat() throws IOException {
        return sendServer("stat");
    }

    //列出邮件中的所有邮件信息
    public String list() throws IOException {
        return sendServer("list");
    }

    //列出邮件中具体某件邮件的信息
    public String list(int mailNum) throws IOException {
        return sendServer("list "+mailNum);
    }

    //给某封邮件设置删除标记
    public void dele(int mailNum) throws IOException {
        sendServer("dele "+mailNum);
    }

    //收取某封邮件
    public void retr(int mailNum) throws IOException {
        String msg = sendServer("retr " + mailNum);
        String result = getResult(msg);
        if (!"+OK".equals(result)) {
            System.out.println(msg);
        }else {
            System.out.println(getMessageDetail());
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String getMessageDetail() throws UnsupportedEncodingException {
        String message = "";
        String line = null;

        try {
            line = in.readLine().toString();
            while (!".".equalsIgnoreCase(line)) {
                message = message + line + "\n";
                line = in.readLine().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    //结束邮件接收过程
    public String quit() throws IOException {
        return sendServer("QUIT");
    }
}
