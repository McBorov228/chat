import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Server создает сокет ServerSocket, открывает порт и ждет подключений клиента.
 * Далее выполняет заданные операции.
 *
 * @autor Петров Даниил Денисович
 */
public class Server extends Thread {

    private static final int PORT = 5006;
    private String templMsg = "Клиент №'%d' отправил сообщение: \n\t";
    private String templConn = "Клиент №'%d' отключился";

    private Socket socket;
    private int num;
    private static List<Story> stories;
    private static List<String> allActions;
    private static List<SocketClient> sockets;

    public void setSocket(int num, Socket socket) {
        this.num = num;
        this.socket = socket;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    @Override
    public void run() {
        try {
            InputStream  sin  = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            DataInputStream  dis = new DataInputStream (sin );
            DataOutputStream dos = new DataOutputStream(sout);
            dos.writeUTF(String.join(" ", allActions));
            String line = null;
            while(true) {
                line = dis.readUTF();
                System.out.println(String.format(templMsg, num) + line);
                allActions.add(LocalDate.now() + ": " + String.format(templMsg, num) + line + '\n');
                if ("story".equals(line)) {
                    getStoryLine(dos);
                } else if (line.equalsIgnoreCase("quit")) {
                    socket.close();
                    allActions.add(LocalDate.now() + ": " + String.format(templConn, num));
                    System.out.println(String.format(templConn, num));
                    break;
                } else {
                    addStoryLine(line);
                    allUserMessage(line, num);
                }
            }
            dos.flush();
        } catch(Exception e) {
            System.out.println("Ошибка 1: " + e);
        }
    }

    /**
     * Отправляем сообщение клиента всем клиентам присутствующим в чате, кроме самого клиента
     *
     * @param line - строка введенная клиентом
     * @param num - номер клиента
     *
     */
    private void allUserMessage(String line, Integer num) {
        for (SocketClient socketClient : sockets) {
            if (!socket.equals(socketClient.getSocket())) {
                try {
                    OutputStream soutClient = socketClient.getSocket().getOutputStream();
                    DataOutputStream dosClient = new DataOutputStream(soutClient);
                    dosClient.writeUTF("Клиент №" + num + ": " + line);
                    dosClient.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Отправляем клиенту его историю сообщений с датой
     *
     * @param dos - поток вывода
     *
     */
    private void getStoryLine(DataOutputStream dos) {
        for (Story story : stories) {
            if (story.getNumberClient().equals(num)) {
                try {
                    String listString = String.join(" ", story.getStory());
                    dos.writeUTF(listString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Добавление сообщения клиента в его историю чата
     *
     * @param line - сообщение клиента
     *
     */
    private void addStoryLine(String line) {
        for (Story story : stories) {
            if (story.getNumberClient().equals(num)) {
                story.addLines(line + '\n');
            }
        }
    }

    public static void main(String[] ar) {
        ServerSocket srvSocket = null;
        stories = new ArrayList<>();
        allActions = new ArrayList<>();
        sockets = new ArrayList<>();
        try {
            try {
                int i = 0;
                InetAddress ia = InetAddress.getByName("localhost");
                srvSocket = new ServerSocket(PORT, 0, ia);
                System.out.println("Сервер запущен\n\n");
                while(true) {
                    Socket socket = srvSocket.accept();
                    System.err.println("Клиент принят");
                    int num = i++;
                    sockets.add(new SocketClient(num, socket));
                    Story storyClient = new Story(num);
                    stories.add(storyClient);
                    new Server().setSocket(num, socket);
                }
            } catch(Exception e) {
                System.out.println("Ошибка 2: " + e);
            }
        } finally {
            try {
                if (srvSocket != null)
                    srvSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

}
