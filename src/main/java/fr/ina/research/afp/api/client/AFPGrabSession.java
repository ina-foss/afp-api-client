package fr.ina.research.afp.api.client;

import java.io.File;
import java.util.List;

import fr.ina.research.afp.api.model.extended.FullNewsDocument;

public class AFPGrabSession {
	private final String authenticatedAs;
	private List<FullNewsDocument> allDocuments;
	private File dir;
	private List<FullNewsDocument> newDocuments;

	public AFPGrabSession(String authenticatedAs) {
		super();
		this.authenticatedAs = authenticatedAs;
	}

	public List<FullNewsDocument> getAllDocuments() {
		return allDocuments;
	}

	public File getDir() {
		return dir;
	}

	public List<FullNewsDocument> getNewDocuments() {
		return newDocuments;
	}

	public AFPGrabSession setAllDocuments(List<FullNewsDocument> allDocuments) {
		this.allDocuments = allDocuments;
		return this;
	}

	public AFPGrabSession setDir(File dir) {
		this.dir = dir;
		return this;
	}

	public AFPGrabSession setNewDocuments(List<FullNewsDocument> newDocuments) {
		this.newDocuments = newDocuments;
		return this;
	}

	public String getAuthenticatedAs() {
		return authenticatedAs;
	}
}
