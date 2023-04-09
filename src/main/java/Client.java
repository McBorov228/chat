import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Класс Client предназначен для подключения к серверу и обмена с ним сообщениями.
 *
 * @autor Петров Даниил Денисович
 */
public class Client {

    /** Порт */
    private static final int SERVER_PORT = 5006;

    /** Локальный IP-адрес */
    private static final String LOCALHOST = "127.0.0.1";

    public static void main(String[] ar)
    {
        Socket socket = null;
        try{
            try {
                System.out.println("Добро пожаловать на сторону клиента\n" +
                        "Подключение к серверу\n\t" +
                        "(IP address " + LOCALHOST +
                        ", port " + SERVER_PORT + ")");
                InetAddress ipAddress;
                ipAddress = InetAddress.getByName(LOCALHOST);
                socket = new Socket(ipAddress, SERVER_PORT);
                System.out.println(
                        "Соединение установлено.");
                System.out.println(
                        "\tLocalPort = " +
                                socket.getLocalPort() +
                                "\n\tInetAddress.HostAddress = " +
                                socket.getInetAddress()
                                        .getHostAddress() +
                                "\n\tReceiveBufferSize (SO_RCVBUF) = "
                                + socket.getReceiveBufferSize());

                InputStream  sin  = socket.getInputStream();
                OutputStream sout = socket.getOutputStream();

                DataInputStream  in ;
                DataOutputStream out;
                in  = new DataInputStream (sin );
                out = new DataOutputStream(sout);

                InputStreamReader isr;
                isr = new InputStreamReader(System.in);
                BufferedReader keyboard;
                keyboard = new BufferedReader(isr);
                String line = in.readUTF();
                if (!"".equals(line)) {
                    System.out.println("История сообщений чата:\n" + line);
                }
                System.out.println("Введите что-нибудь:");
                while (true) {
                    line = keyboard.readLine();
                    out.writeUTF(line);
                    out.flush();
                    line = in.readUTF();
                    if (line.endsWith("quit")) {
                        break;
                    } else {
                        System.out.println("Сервер прислал мне эту строку:\n" + line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
