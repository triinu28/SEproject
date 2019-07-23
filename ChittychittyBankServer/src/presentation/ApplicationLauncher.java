package presentation;

import java.net.MalformedURLException;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;

public class ApplicationLauncher {
	
	public static void main(String[] args) throws MalformedURLException {
		BLFacade bl = new BLFacadeImplementation();
		Login a = new Login(bl);
		a.setVisible(true);
	}

}
