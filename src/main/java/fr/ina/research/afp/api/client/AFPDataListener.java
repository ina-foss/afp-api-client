package fr.ina.research.afp.api.client;

import fr.ina.research.afp.api.model.extended.FullNewsDocument;
import fr.ina.research.afp.api.model.extended.LangEnum;

public interface AFPDataListener {
	void newDocument(LangEnum lng, FullNewsDocument nd, AFPGrabSession gs);
	void newAPICall(LangEnum lng, int lastMinutes);
}
