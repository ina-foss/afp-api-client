package fr.ina.research.afp.api.model.extended;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ina.research.afp.api.model.NewsDocument;

public class FullNewsDocument {
	public enum EntityType {
		ORGANISATION("organisation"), FUNCTION("function"), MEDIA("media"), KEYWORD("keyword"), LOCATION("location"), PERSON("person"), COMPANY("company");

		private final String suffix;

		private EntityType(String suffix) {
			this.suffix = suffix;
		}

		public String getSuffix() {
			return suffix;
		}

	};

	private NewsDocument internal;
	private File localFile;
	// private Map<String, File> files;
	// private Set<String> retrieved;
	private ZonedDateTime retrievedDate;
	private List<FullMediaItem> mediaItems;

	public FullNewsDocument(NewsDocument internal, long retrievedDate) {
		super();
		// files = new HashMap<>();
		// retrieved = new HashSet<>();
		setInternal(internal);
		mediaItems = new ArrayList<>();
		this.retrievedDate = ZonedDateTime.ofInstant(new Date(retrievedDate).toInstant(), ZoneId.systemDefault());
	}

	public boolean addMediaItem(FullMediaItem e) {
		return mediaItems.add(e);
	}

	private String concatenate(List<String> strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			if (sb.length() > 0) {
				sb.append("\n");
			}
			sb.append(s);
		}
		return sb.toString();
	}

	public List<String> getBody() {
		return getStringProperty("news");
	}

	// public File getFile(String key) {
	// return files.get(key);
	// }
	//
	// public Set<String> getFiles() {
	// return files.keySet();
	// }

	public List<String> getEntities(EntityType type) {
		return getStringProperty("entity_" + type.getSuffix());
	}

	public String getFullBoby() {
		return concatenate(getBody());
	}

	public String getFullTitle() {
		return concatenate(getTitle());
	}

	public String getFullUno() {
		return getUno() + "---" + getRevision();
	}

	public String getHref() {
		return (String) internal.get("href");
	}

	public NewsDocument getInternal() {
		return internal;
	}

	public List<String> getIPTC() {
		return getStringProperty("iptc");
	}

	public String getLang() {
		return (String) internal.get("lang");
	}

	public File getLocalFile() {
		return localFile;
	}

	public List<FullMediaItem> getMediaItems() {
		return mediaItems;
	}

	public String getProduct() {
		return (String) internal.get("product");
	}

	public String getPublicIdentifier() {
		return (String) internal.get("publicIdentifier");
	}

	public String getPublished() {
		return (String) internal.get("published");
	}

	public ZonedDateTime getPublishedDate() {
		return ZonedDateTime.parse(getPublished());
	}

	public ZonedDateTime getRetrievedDate() {
		return retrievedDate;
	}

	public int getRevision() {
		return (int) ((double) internal.get("revision"));
	}

	public List<String> getSlug() {
		return getStringProperty("slug");
	}

	private List<String> getStringProperty(String prop) {
		Object o = internal.get(prop);
		if (o == null) {
			return new ArrayList<>();
		}
		if (o instanceof List) {
			return (List<String>) o;
		}
		List<String> l = new ArrayList<>();
		l.add((String) o);
		return l;
	}

	public List<String> getTitle() {
		return getStringProperty("title");
	}

	// public boolean isRetrieved(String o) {
	// return retrieved.contains(o);
	// }
	//
	// public void markAsRetrieved(String e) {
	// retrieved.add(e);
	// }

	public String getUno() {
		return (String) internal.get("uno");
	}

	// public void putFile(String key, File value) {
	// files.put(key, value);
	// }

	public boolean mayHaveMedia() {
		String p = getProduct();
		return "multimedia".equalsIgnoreCase(p) || "photo".equalsIgnoreCase(p) || "infographie".equalsIgnoreCase(p);
	}

	private void setInternal(NewsDocument internal) {
		this.internal = internal;
	}

	public void setLocalFile(File localFile) {
		this.localFile = localFile;
	}

	@Override
	public String toString() {
		return "FullNewsDocument [" + getFullUno() + "]";
	}
}
