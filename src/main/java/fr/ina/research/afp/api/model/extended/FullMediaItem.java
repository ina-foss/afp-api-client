package fr.ina.research.afp.api.model.extended;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FullMediaItem {
	public static class MediaFile {
		private String role;
		private String type;
		private String href;
		private File localFile;
		private int size;

		public String getHref() {
			return href;
		}

		public File getLocalFile() {
			return localFile;
		}

		public String getRole() {
			return role;
		}

		public int getSize() {
			return size;
		}

		public String getType() {
			return type;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public void setLocalFile(File localFile) {
			this.localFile = localFile;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public void setType(String type) {
			this.type = type;
		}
		
		public boolean isImage() {
			return "Photo".equalsIgnoreCase(getType()) || "Graphic".equalsIgnoreCase(getType());
		}
	}

	private String creator;
	private String provider;
	private String uno;
	private String caption;
	private List<MediaFile> files;

	public FullMediaItem() {
		super();
		files = new ArrayList<>();
	}

	public boolean addMediaFile(MediaFile e) {
		return files.add(e);
	}

	public String getCaption() {
		return caption;
	}

	public String getCreator() {
		return creator;
	}

	public List<MediaFile> getFiles() {
		return files;
	}
	
	public List<MediaFile> getFilesToIndex() {
		List<MediaFile> res = getVideos();
		MediaFile big = getBiggestImage();
		if (big != null) {
			res.add(big);
		}
		return res;
	}
	
	public List<MediaFile> getVideos() {
		ArrayList<MediaFile> videos = new ArrayList<>();
		for (MediaFile f : getFiles()) {
			if (!f.isImage()) {
				videos.add(f);
			}
		}
		return videos;
	}
	
	public MediaFile getBiggestImage() {
		MediaFile result = null;
		int max = -1;
		
		for (MediaFile f : getFiles()) {
			if (f.isImage() && f.getSize() > max) {
				max = f.getSize();
				result = f;
			}
		}
		
		return result;
	}

	public String getProvider() {
		return provider;
	}

	public String getUno() {
		return uno;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void setUno(String uno) {
		this.uno = uno;
	}
}
