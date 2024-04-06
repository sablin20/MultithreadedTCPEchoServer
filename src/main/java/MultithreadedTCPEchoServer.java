import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadedTCPEchoServer {
    private static final int PORT = 8080;

    // Создаем пул потоков для обработки соединений
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private static void handleClient(Socket clientSocket) {
        try {
            // Буфер для чтения данных
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
                // Отправляем данные обратно клиенту
                clientSocket.getOutputStream().write(buffer, 0, bytesRead);
            }
            clientSocket.close(); // Закрываем соединение
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        try (var serverSocket = new ServerSocket(PORT)) {
            while (true) {
                var clientSocket = serverSocket.accept(); // Ожидаем подключения клиента
                executorService.execute(() -> handleClient(clientSocket)); // Добавляем обработку клиента в пул потоков
            }
        } finally {
            executorService.shutdownNow(); // Закрываем пул потоков
        }
    }
}