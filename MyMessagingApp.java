import java.io.*;
import java.net.*;

class App {
	public static void main(String args[]) {
		boolean clientMode = false;
		boolean serverMode = false;

		System.out.print("Is this being run as the client or the server? (c/s):");

		String input = System.console().readLine();
		if (input.equals("client") || input.equals("c")) {
			clientMode = true;
		} else if (input.equals("server") || input.equals("s")) {
			serverMode = true;
		} else {
			// Nonsense answer... figure out what to do with this later
			return;
		}

		if (clientMode) {
			// Do client stuff here...
			System.out.print("Enter the server's address: ");
			String serverAddress = System.console().readLine();

			System.out.print("Enter the port number: ");
			// TODO: error handling for bad input here
			int portNumber = Integer.parseInt(System.console().readLine());

			try {
				BufferedReader fromUser = new BufferedReader (new InputStreamReader(System.in));
				Socket clientSocket = new Socket(serverAddress, portNumber);
				DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				Boolean someCondition = false;

				System.out.println("Connection established! Now accepting input.");
				while (!someCondition) {
					String sentence = fromUser.readLine();

					// TODO: SET SECURITY SCHEME DYNAMICALLY
					String encryptedMessage = RotorMachine.encryptMessage(sentence);

					toServer.writeBytes(encryptedMessage + '\n');
					if (sentence.equals("terminate")) {
						someCondition = true;
					}
					String encryptedIncomingMessage = fromServer.readLine();
					String decryptedIncomingMessage = RotorMachine.decryptMessage(encryptedIncomingMessage);
					System.out.println("From server: " + decryptedIncomingMessage);
				}
				clientSocket.close();
			} catch (ConnectException e) {
				System.out.println("Connection refused. Perhaps the server is currently unreachable?");
			} catch (UnknownHostException e) {
				System.out.println("Error: IP Address of hostname \"" + serverAddress + "\" could not be determined");
			} catch (IOException e) {
				e.printStackTrace();
			}
			// END OF CLIENT STUFF
		} else if (serverMode) {
			// Do server stuff here
		} 
		return;
	}

}