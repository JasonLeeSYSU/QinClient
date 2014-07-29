package qin.controller.handelThread.basicOperation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import qin.controller.QinUIController;
import qin.model.*;

public class MessagePacketSender {
	public static QinMessagePacket sendPacket(QinMessagePacket packetToSend) throws IOException, ClassNotFoundException {
		QinMessagePacket packetReceived = new QinMessagePacket(null);
		try {
			Socket socket = new Socket(QinUIController.getInstance().getServerIP(), Resource.ServerPort);
			
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(packetToSend);
			out.flush();
			
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			packetReceived = (QinMessagePacket)in.readObject();
			
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packetReceived;
	}
}
