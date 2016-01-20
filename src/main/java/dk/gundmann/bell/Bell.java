package dk.gundmann.bell;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public class Bell {

	@WebMethod
	public String ring() {
		return "OK";
	}
	
}
