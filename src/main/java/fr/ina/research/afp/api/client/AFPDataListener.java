package fr.ina.research.afp.api.client;

import fr.ina.research.afp.api.model.Parameters.LangEnum;
import fr.ina.research.afp.api.model.extended.FullNewsDocument;

public interface AFPDataListener {
	void newDocument(LangEnum lng, FullNewsDocument nd, AFPGrabSession gs);
	void newAPICall(LangEnum lng, int lastMinutes);
}
