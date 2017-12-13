package fr.ina.research.afp;

import java.io.File;
import java.net.Proxy;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ina.research.afp.api.client.AFPDataGrabber;
import fr.ina.research.afp.api.client.AFPDataGrabberCache;
import fr.ina.research.afp.api.client.AFPGrabSession;
import fr.ina.research.afp.api.model.Parameters.LangEnum;
import fr.ina.research.afp.api.model.extended.FullNewsDocument;

public class GetSomeAFPData {
	private final static File dataDir = new File("/tmp/afp");

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(GetSomeAFPData.class);

		Proxy proxy = null;
		// If you are behind a proxy, use the following line with the correct parameters :
		// proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("firewall.company.com", 80));

		Map<String, String> authentication = null;
		// If you want to use your credentials on api.afp.com, provide the following informations :
		// authentication = new HashMap<String, String>();
		// authentication.put(AFPAuthenticationManager.KEY_CLIENT, "client");
		// authentication.put(AFPAuthenticationManager.KEY_SECRET, "secret");
		// authentication.put(AFPAuthenticationManager.KEY_USERNAME, "username");
		// authentication.put(AFPAuthenticationManager.KEY_PASSWORD, "password");

		AFPDataGrabber afp = new AFPDataGrabber(LangEnum.EN, authentication, logger, dataDir,
				AFPDataGrabberCache.noCache(), proxy);

		AFPGrabSession gs = afp.grabSearchMax(false, 10);
		logger.info("Grabbed " + gs.getAllDocuments().size() + " documents as [" + gs.getAuthenticatedAs() + "] in "
				+ gs.getDir());
		for (FullNewsDocument nd : gs.getAllDocuments()) {
			logger.info("    - " + nd.getUno() + " : " + nd.getFullTitle());
		}
	}

}
