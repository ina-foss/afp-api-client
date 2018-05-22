package fr.ina.research.afp.api.client;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import fr.ina.research.afp.ApiException;
import fr.ina.research.afp.api.SearchApi;
import fr.ina.research.afp.api.model.DateRange;
import fr.ina.research.afp.api.model.NewsDocument;
import fr.ina.research.afp.api.model.Parameters;
import fr.ina.research.afp.api.model.Parameters.LangEnum;
import fr.ina.research.afp.api.model.Result;
import fr.ina.research.afp.api.model.extended.FullMediaItem;
import fr.ina.research.afp.api.model.extended.FullMediaItem.MediaFile;
import fr.ina.research.afp.api.model.extended.FullNewsDocument;
import fr.ina.research.afp.api.model.extended.FullParameter;
import fr.ina.research.afp.api.model.extended.FullResult;

public class AFPDataGrabber {
	private final static ObjectMapper jsonMapper;

	static {
		jsonMapper = new ObjectMapper();
		jsonMapper.registerModule(new JaxbAnnotationModule());
		jsonMapper.registerModule(new JavaTimeModule());

		jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
		jsonMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		jsonMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	public static <T> T deserializeFromString(String content, Class<T> deserializedObjectType) throws IOException {
		return jsonMapper.readValue(content, deserializedObjectType);
	}

	public static <T> String serializeToString(T object) throws IOException {
		try {
			return jsonMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IOException(e);
		}
	}

	private static List<FullNewsDocument> asFullDocuments(File dir, FullResult r) {
		List<FullNewsDocument> full = new ArrayList<>();
		for (NewsDocument nd : r.getInternal().getResponse().getDocs()) {
			FullNewsDocument fd = new FullNewsDocument(nd, r.getRetrievedDate());
			// logger.debug(fd.getPublished() + " - " + fd.getProduct() + " - "
			// + fd.getLang() + " - " + fd.getTitle() + " - " +
			// fd.getFullUno());
			fd.setLocalFile(new File(dir, fd.getFullUno() + ".xml"));
			if (fd.mayHaveMedia()) {
				List<Object> bag = (List<Object>) nd.get("bagItem");
				if (bag != null) {
					for (Object bagObject : bag) {
						Map bagMap = (Map) bagObject;
						FullMediaItem fmi = new FullMediaItem();
						String uno = (String) bagMap.get("uno");
						fmi.setUno(uno);
						fmi.setCreator((String) bagMap.get("creator"));
						fmi.setCaption((String) bagMap.get("caption"));
						fmi.setProvider((String) bagMap.get("provider"));

						List<Map> medias = (List<Map>) bagMap.get("medias");
						for (Map media : medias) {
							String existingExt = "";
							if (uno.toUpperCase().endsWith(".JPG") || uno.toUpperCase().endsWith(".JPEG")) {
								existingExt = ".jpg";
								uno = uno.substring(0, uno.lastIndexOf("."));
							} else if (uno.toUpperCase().endsWith(".PNG")) {
								existingExt = ".png";
								uno = uno.substring(0, uno.lastIndexOf("."));
							} else if (uno.toUpperCase().endsWith(".MP4") || uno.toUpperCase().endsWith(".MPG4")
									|| uno.toUpperCase().endsWith(".MPEG4") || uno.toUpperCase().endsWith(".MPEG")) {
								existingExt = ".mp4";
								uno = uno.substring(0, uno.lastIndexOf("."));
							} else if (uno.toUpperCase().endsWith(".FLV")) {
								existingExt = ".flv";
								uno = uno.substring(0, uno.lastIndexOf("."));
							}

							String role = (String) media.get("role");
							String type = (String) media.get("type");
							String ext = "Video".equalsIgnoreCase(type) ? ".mp4" : ".jpg";
							String mediaHref = (String) media.get("href");
							Double width = (Double) media.get("width");
							Double height = (Double) media.get("height");
							int size = 0;
							if ((width != null) && (height != null)) {
								size = width.intValue() * height.intValue();
							}
							File localFile = new File(dir, uno + "-" + role + ext);
							FullMediaItem.MediaFile f = new FullMediaItem.MediaFile();
							f.setHref(mediaHref);
							f.setRole(role);
							f.setType(type);
							f.setLocalFile(localFile);
							f.setSize(size);
							fmi.addMediaFile(f);

							// fd.putFile(mediaHref, localFile);
						}

						fd.addMediaItem(fmi);
					}
				}
			}
			full.add(fd);
		}
		return full;
	}

	public static File getDocsFile(File dir) {
		return new File(dir, "api_docs.json");
	}

	public static LangEnum getLangEnum(String lang) {
		switch (lang) {
		case "fr":
			return LangEnum.FR;
		case "en":
			return LangEnum.EN;
		case "ar":
			return LangEnum.AR;
		case "de":
			return LangEnum.DE;
		case "es":
			return LangEnum.ES;
		case "pt":
			return LangEnum.PT;
		default:
			return null;
		}
	}

	public static File getSearchFile(File dir) {
		return new File(dir, "api_search.json");
	}

	public static Collection<File> getValidReplayDirectories(File root) {
		Collection<File> res = new ArrayList<>();
		if (isValidReplayDirectory(root)) {
			res.add(root);
		} else {
			if (root.exists() && root.isDirectory()) {
				for (File sub : root.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						return pathname.isDirectory();
					}
				})) {
					res.addAll(getValidReplayDirectories(sub));
				}
			}
		}
		return res;
	}

	public static boolean isValidReplayDirectory(File dir) {
		if (!dir.exists() || !dir.isDirectory()) {
			return false;
		}

		File docs = getDocsFile(dir);
		if (!docs.exists() || !docs.isFile()) {
			return false;
		}

		File search = getSearchFile(dir);
		if (!search.exists() || !search.isFile()) {
			return false;
		}

		return true;
	}

	public static AFPGrabSession replay(File dir) throws IOException {
		File docsFile = getDocsFile(dir);
		if (docsFile.exists()) {
			FullResult res = deserializeFromString(FileUtils.readFileToString(docsFile, "UTF-8"), FullResult.class);
			return new AFPGrabSession("replay").setDir(dir).setAllDocuments(asFullDocuments(dir, res));
		}
		return null;
	}

	private final DateFormat directoryDF;
	private final DateFormat fileDF;
	private final DateFormat dateDF;

	private final Proxy proxy;
	private final String endpoint;

	private AFPAuthenticationManager aam;

	private final Logger logger;

	private final File baseDir;

	private final LangEnum lng;

	private final AFPDataGrabberCache cache;

	public AFPDataGrabber(LangEnum lang, Map<String, String> authenticationProperties, Logger logger, File baseDir,
			AFPDataGrabberCache cache, String endpoint, Proxy proxy) {
		super();
		aam = new AFPAuthenticationManager(authenticationProperties, endpoint, proxy, logger);
		this.logger = logger;
		this.baseDir = baseDir;
		this.proxy = proxy;
		this.endpoint = endpoint;
		this.cache = cache;
		lng = lang;
		directoryDF = new SimpleDateFormat("yyyy/MM/dd/", Locale.FRANCE);
		directoryDF.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		fileDF = new SimpleDateFormat("HHmmssSSS", Locale.FRANCE);
		fileDF.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		dateDF = new SimpleDateFormat("yyyyMMdd", Locale.FRANCE);
		dateDF.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
	}

	private File getDayDirectory(Date d) {
		return new File(baseDir, directoryDF.format(d));
	}

	private void getFile(String href, File file, OkHttpClient httpClient) throws IOException {
		Response resp = null;
		boolean sucess = false;
		int nbTry = 1;
		Exception lastException = null;

		while (!sucess && (nbTry < 4)) {
			try {
				lastException = null;
				logger.debug("[" + nbTry + "] " + file.getName() + " <- " + href);
				Call call = httpClient.newCall(new Request.Builder().url(href).build());
				resp = call.execute();

				if (resp.isSuccessful()) {
					FileOutputStream fos = new FileOutputStream(file);
					IOUtils.copy(resp.body().byteStream(), fos);
					fos.close();
					// fd.markAsRetrieved(href);
					sucess = true;
				} else {
					throw new IOException("Unable to get " + href + " : " + resp.code() + " - " + resp.message());
				}
			} catch (Exception e) {
				lastException = e;
			} finally {
				nbTry++;
				if ((resp != null) && (resp.body() != null)) {
					resp.body().close();
				}
				logger.debug("Sleeping ...");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}

		if (lastException != null) {
			throw new IOException(lastException);
		}
	}

	private File getSessionDirectory(String type) {
		Date d = new Date();
		File dir = new File(getDayDirectory(d), fileDF.format(d) + "-" + type);
		dir.mkdirs();
		return dir;
	}

	private AFPGrabSession grabSearch(boolean downloadItems, Parameters p, String postfix) {
		try {
			SearchApi api = new SearchApi();
			String token = aam.getToken();
			File dir = getSessionDirectory(p.getLang() + "-" + postfix);
			logger.debug("AFP - grabSearch - " + token + " - " + dir.getAbsolutePath());
			api.getApiClient().setAccessToken(token);
			api.getApiClient().setBasePath(endpoint);
			if (proxy != null) {
				api.getApiClient().getHttpClient().setProxy(proxy);
			}
			File search = getSearchFile(dir);
			FileWriter w = new FileWriter(search);
			w.write(serializeToString(p));
			w.close();

			logger.debug(p.toString().replace("    ", "").replace("\n", ", "));

			Result r = api.searchUsingPOST1(p, "xml");

			return processResponse(downloadItems, new FullResult(r, System.currentTimeMillis()), dir,
					api.getApiClient().getHttpClient());
		} catch (ApiException apie) {
			logger.error(apie.toString());
			logger.error("API error", apie);
			if (apie.getCode() == java.net.HttpURLConnection.HTTP_UNAUTHORIZED) {
				aam.cleanToken();
			}
			return null;
		} catch (Exception e) {
			logger.error("Global error", e);
			return null;
		}
	}

	public AFPGrabSession grabSearchDay(boolean downloadItems, Date date, int maxdocs) {
		return grabSearch(downloadItems, queryDay(date, maxdocs), "date-" + dateDF.format(date) + "-" + maxdocs);
	}

	public AFPGrabSession grabSearchLastMinutes(boolean downloadItems, int lastMinutes, int maxdocs) {
		return grabSearch(downloadItems, query(lastMinutes, maxdocs), "lastmin-" + lastMinutes + "-" + maxdocs);
	}

	public AFPGrabSession grabSearchMax(boolean downloadItems, int maxdocs) {
		return grabSearch(downloadItems, query(maxdocs), "maxdocs-" + maxdocs);
	}

	public AFPGrabSession grabSearchMaxProduct(boolean downloadItems, String product, int maxdocs) {
		return grabSearch(downloadItems, queryProduct(query(maxdocs), product), product + "-maxdocs-" + maxdocs);
	}

	private AFPGrabSession processResponse(boolean downloadItems, FullResult r, File dir, OkHttpClient httpClient)
			throws IOException {
		FileWriter w = new FileWriter(getDocsFile(dir));
		w.write(serializeToString(r));
		w.close();

		AFPGrabSession gs = new AFPGrabSession(aam.getAuthenticatedAs());
		gs.setDir(dir);

		List<FullNewsDocument> allDocuments = asFullDocuments(dir, r);
		gs.setAllDocuments(allDocuments);

		if (downloadItems) {
			List<FullNewsDocument> newDocuments = new ArrayList<>();
			for (FullNewsDocument fd : allDocuments) {
				if (!cache.isInCache(fd.getFullUno())) {
					logger.debug("Not in cache : " + fd.getFullUno());
					try {
						getFile(fd.getHref(), fd.getLocalFile(), httpClient);
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
					for (FullMediaItem fmi : fd.getMediaItems()) {
						for (MediaFile mf : fmi.getFiles()) {
							try {
								getFile(mf.getHref(), mf.getLocalFile(), httpClient);
							} catch (IOException e) {
								logger.error(e.getMessage());
							}
						}
					}
					newDocuments.add(fd);
					cache.putInCache(fd.getFullUno());
				} else {
					logger.debug("In cache : " + fd.getFullUno() + ", files are not grabbed again");
				}
			}
			gs.setNewDocuments(newDocuments);
		}

		return gs;
	}

	private Parameters query(int maxdocs) {
		Parameters p = new Parameters();
		p.setLang(lng);
		p.setMaxRows(maxdocs);
		p.setSortField("published");
		p.setSortOrder("desc");
		return p;
	}

	private Parameters query(long lastMinutes, int maxdocs) {
		Parameters p = query(maxdocs);
		p.setDateRange(new DateRange().from("now-" + lastMinutes + "m").to("now"));
		return p;
	}

	private Parameters query(long fromMinutes, long toMinutes, int maxdocs) {
		Parameters p = query(maxdocs);
		p.setDateRange(new DateRange().from("now-" + fromMinutes + "m").to("now-" + toMinutes + "m"));
		return p;
	}

	private Parameters queryDay(Date day, int maxdocs) {
		Calendar c = new GregorianCalendar();
		c.setTime(day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		ZonedDateTime start = c.toInstant().atZone(ZoneId.systemDefault());

		c.add(Calendar.DAY_OF_MONTH, 1);
		ZonedDateTime end = c.toInstant().atZone(ZoneId.systemDefault());

		ZonedDateTime now = ZonedDateTime.now();
		long fromMinutes = (long) Math.ceil(Duration.between(start, now).get(ChronoUnit.SECONDS) / 60.);
		long toMinutes = (long) Math.ceil(Duration.between(end, now).get(ChronoUnit.SECONDS) / 60.);

		return query(fromMinutes, toMinutes, maxdocs);
	}

	private Parameters queryProduct(Parameters p, String product) {
		FullParameter fp1 = new FullParameter();
		fp1.setName("product");
		fp1.setIn(new String[] { product });
		p.setQuery(fp1);
		return p;
	}

	public void warmupCache(int nbDaysAgo) throws IOException {
		if (nbDaysAgo <= 0) {
			logger.info("No cache warmup");
			return;
		}

		logger.info("Cache warmup " + nbDaysAgo + " days ago");
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		for (int d = 0; d < nbDaysAgo; d++) {
			File dir = getDayDirectory(c.getTime());
			logger.info("  - cache " + dir);
			if (dir.exists()) {
				for (File sd : getValidReplayDirectories(dir)) {
					logger.debug("      - " + sd);
					AFPGrabSession session = AFPDataGrabber.replay(sd);
					for (FullNewsDocument nd : session.getAllDocuments()) {
						cache.putInCache(nd.getFullUno());
					}
				}
			}

			c.add(Calendar.DAY_OF_MONTH, -1);
		}
	}
}
