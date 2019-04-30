package yang.lin35.networking_service_demo.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSocketServer {

	private static final int PORT = 9999;

	public static void main(String[] args) {

		Thread dispatch = new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(PORT);
				while(true) {
					//阻塞直到有请求
					Socket socket = serverSocket.accept();
					//启动一个新线程处理请求
					Thread worker = new Thread(()-> {
						BufferedReader reader;
						try {
							reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
							String line = null;
							while ((line = reader.readLine()) != null) {
								System.out.println(socket.getRemoteSocketAddress().toString()+": "+line);
								writer.write("来自服务器的回复"+line+"\n");
								writer.flush();
							}
							reader.close();
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					});  
					worker.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		dispatch.start();
	}

}
