import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class HTTPClientManager extends Thread {
    private Socket socket;
    private String url;
    private ArrayList<String> connections;

    HTTPClientManager(Socket clientSocket, String url, ArrayList<String> connections) {
        this.socket = clientSocket;
        this.url = url;
        this.connections = connections;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            String data = pingUrl(url);

            String request = input.readLine();

            if (request.startsWith("GET")) {
                //response header
                dataOutputStream.writeBytes("HTTP/1.0 200 OK\r\n");
                dataOutputStream.writeBytes("Content-Type: text/html\r\n");
                dataOutputStream.writeBytes("Content-Length: \r\n");
                dataOutputStream.writeBytes("\r\n");

                //response body
                dataOutputStream.writeBytes("<html>\r\n");
                dataOutputStream.writeBytes("<head><meta charset=\"utf-8\" /></head>\r\n");
                dataOutputStream.writeBytes("<body>\r\n");
                dataOutputStream.writeBytes("<H1>Strona testowa</H1>\r\n");
                dataOutputStream.writeBytes("<H2>Ping:" + data + "</H2>\r\n");
                log(dataOutputStream);
                dataOutputStream.writeBytes("</body>\r\n");
                dataOutputStream.writeBytes("</html>\r\n");
            } else {
                dataOutputStream.writeBytes("HTTP/1.1 501 Not supported.\r\n");
            }

            input.close();
            dataOutputStream.close();
            socket.close();
            connections.remove(url);
        } catch (Exception e) {
            System.out.println(String.valueOf(e));
        }
    }

    private void log(DataOutputStream outputStream) {
        ArrayList<String> log = new ArrayList<>();
        try {
            BufferedReader file = new BufferedReader(new FileReader("log.txt"));
            String line;

            while (file.ready()) {
                line = file.readLine();
                log.add(line);
            }

            file.close();
            for (int i = log.size() - 1; i >= 0; i--) {
                outputStream.writeBytes(log.get(i) + "\r\n" + "</br>");
            }
        } catch (Exception e) {
            System.out.println(String.valueOf(e));
        }
    }

    @Nullable
    private static String pingUrl(final String address) {
        try {
            String line;
            ArrayList<String> commands = new ArrayList<>();
            commands.add("ping");
            commands.add("-c 1");
            commands.add(address);
            ProcessBuilder processbuilder = new ProcessBuilder(commands);
            Process process = processbuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = stdInput.readLine()) != null) {
                if (line.contains("rtt")) {
                    return line.split("/")[5];
                }
            }

        } catch (Exception e) {
            System.out.println(String.valueOf(e));
        }
        return null;
    }
}

class HTTPPart {

    private SettingsProvider settingsProvider;

    HTTPPart(SettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
    }

    void Run() {
        new Thread(() -> incoming()).start();
    }

    private void incoming() {
        System.out.println("HTTP started");
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(settingsProvider.get("httpport")));
            ArrayList<String> connections = new ArrayList<>();

            while (true) {
                Socket socket = serverSocket.accept();
                String url = socket.getRemoteSocketAddress().toString();
                url = url.split(":")[0].split("/")[1];
                if (!connections.contains(url)) {
                    new HTTPClientManager(socket, url, connections).start();
                    connections.add(url);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in HTTPPart/incoming: "+String.valueOf(e));
        }
    }

}