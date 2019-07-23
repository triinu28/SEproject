package presentation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.BLFacade;

public class ApplicationLauncher {
	
	public static void main(String[] args) throws MalformedURLException {
		
		URL url = new URL("http://localhost:8080/ws?wsdl");
		QName qname = new QName("http://businessLogic/","BLFacadeImplementationService"); 
		Service service = Service.create(url, qname);
		
		BLFacade bl = service.getPort(BLFacade.class);

		Login a = new Login(bl);
		a.setVisible(true);
	}

}
