import java.net.Socket;

/**
 * Класс SocketClient хранит в себе номер клиента и принадлежащий ему клиентский сокет.
 *
 * @autor Петров Даниил Денисович
 */
public class SocketClient {

    private Integer numberClient;
    private Socket socket;

    /**
     * Конструктор класса SocketClient
     *
     * @param numberClient - номер клиента
     * @param socket - клиентский сокет
     *
     */
    public SocketClient(Integer numberClient, Socket socket) {
        this.numberClient = numberClient;
        this.socket = socket;
    }

    public Integer getNumberClient() {
        return this.numberClient;
    }

    public Socket getSocket() {
        return this.socket;
    }

}
