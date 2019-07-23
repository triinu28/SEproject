package businessLogic;

import javax.xml.ws.Endpoint;

public class Publisher {
	public static void main (String args[]) { 
		Endpoint.publish("http://0.0.0.0:8080/ws", new BLFacadeImplementation()); 
		System.out.println("The web service has been launched");
	} 
}