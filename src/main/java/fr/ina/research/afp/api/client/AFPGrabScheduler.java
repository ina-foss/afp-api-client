package fr.ina.research.afp.api.client;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;

import fr.ina.research.afp.api.model.extended.FullNewsDocument;
import fr.ina.research.afp.api.model.extended.LangEnum;

public class AFPGrabScheduler {
	private class AFPThreadFactory implements ThreadFactory {
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);

		public AFPThreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, "AFP-" + lang + "-" + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon()) {
				t.setDaemon(false);
			}
			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}

	}

	private int scheduleEvery;
	private boolean enableProxy;
	private boolean retrieveMedias;
	private String proxy;
	private int proxyPort;
	private Logger logger;
	private Map<String, String> authenticationProperties;
	private String datadir;
	private LangEnum lang;
	private String apiBasePath;

	private AFPDataGrabber afp;
	private ScheduledExecutorService taskScheduler;
	private Set<AFPDataListener> listeners;

	private Map<String, ZonedDateTime> lastRetrievedDocuments;

	public AFPGrabScheduler() {
		super();

		listeners = new HashSet<>();
		setLang(LangEnum.FR);
		setRetrieveMedias(false);
		lastRetrievedDocuments = new HashMap<>();
	}

	public void addListener(AFPDataListener e) {
		listeners.add(e);
	}

	private void callAFPApi() {
		try {
			int lastMinutes = scheduleEvery * 2;
			int maxDocs = lastMinutes * 50;

			ZonedDateTime minDate = getMinRetrievedDocumentDate();

			if (minDate != null) {
				ZonedDateTime now = ZonedDateTime.now();

				double secondsAgo = ChronoUnit.SECONDS.between(minDate, now);
				int minutesAgo = (int) Math.ceil(secondsAgo / 60);

				lastMinutes += minutesAgo;
				maxDocs = lastMinutes * 50;
			}

			AFPGrabSession gs = afp.grabSearchLastMinutes(true, retrieveMedias, lastMinutes, maxDocs);
			if (gs == null) {
				logger.error("Unable to call AFP api !");
				return;
			}
			for (AFPDataListener l : listeners) {
				l.newAPICall(lang, lastMinutes);
			}
			logger.info("Nb. new " + lang + " documents since " + lastMinutes + " min. : " + gs.getNewDocuments().size());

			for (FullNewsDocument nd : gs.getNewDocuments()) {
				logger.debug("    - " + nd.getFullUno());
				updateLastRetrievedDocument(nd.getProduct(), nd.getPublishedDate());
				for (AFPDataListener l : listeners) {
					l.newDocument(lang, nd, gs);
				}
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// ignore
			}
		} catch (Exception e) {
			logger.error("Global error", e);
		}
	}

	public String getApiBasePath() {
		return apiBasePath;
	}

	public Map<String, String> getAuthenticationProperties() {
		return authenticationProperties;
	}

	public String getDatadir() {
		return datadir;
	}

	public LangEnum getLang() {
		return lang;
	}

	public Logger getLogger() {
		return logger;
	}

	public ZonedDateTime getMinRetrievedDocumentDate() {
		ZonedDateTime min = null;

		for (ZonedDateTime aDate : lastRetrievedDocuments.values()) {
			// TODO a corriger si bug côté AFP
			if ((min == null) || aDate.isAfter(min)) {
				min = aDate;
			}
		}

		return min;
	}

	public String getProxy() {
		return proxy;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public int getScheduleEvery() {
		return scheduleEvery;
	}

	public AFPGrabSession grabSearchDay(boolean downloadXML, Date date, int maxdocs) {
		init();
		return afp.grabSearchDay(downloadXML, retrieveMedias, date, maxdocs);
	}

	public AFPGrabSession grabSearchMax(boolean downloadXML, int maxdocs) {
		init();
		return afp.grabSearchMax(downloadXML, retrieveMedias, maxdocs);
	}

	public AFPGrabSession grabSearchMaxProduct(boolean downloadXML, String product, int maxdocs) {
		init();
		return afp.grabSearchMaxProduct(downloadXML, retrieveMedias, product, maxdocs);
	}

	private void init() {
		if (afp == null) {
			Proxy prox = null;
			if (isEnableProxy()) {
				prox = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getProxy(), getProxyPort()));
			}
			afp = new AFPDataGrabber(lang, authenticationProperties, logger, new File(datadir), AFPDataGrabberCache.realCache(), getApiBasePath(), prox);
		}
	}

	public boolean isEnableProxy() {
		return enableProxy;
	}

	public void removeListener(AFPDataListener o) {
		listeners.remove(o);
	}

	public void setApiBasePath(String apiBasePath) {
		this.apiBasePath = apiBasePath;
	}

	public void setAuthenticationProperties(Map<String, String> authenticationProperties) {
		this.authenticationProperties = authenticationProperties;
	}

	public void setDatadir(String datadir) {
		this.datadir = datadir;
	}

	public void setEnableProxy(boolean enableProxy) {
		this.enableProxy = enableProxy;
	}

	public void setLang(LangEnum lang) {
		this.lang = lang;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public void setScheduleEvery(int scheduleEvery) {
		this.scheduleEvery = scheduleEvery;
	}

	public void start() throws IOException {
		init();

		taskScheduler = Executors.newSingleThreadScheduledExecutor(new AFPThreadFactory());
		afp.warmupCache(3);

		taskScheduler.scheduleAtFixedRate(() -> {
			callAFPApi();
		}, 0, scheduleEvery, TimeUnit.MINUTES);
	}

	public void stop() {
		if (taskScheduler != null) {
			taskScheduler.shutdownNow();
		}
	}

	public void updateLastRetrievedDocument(String product, ZonedDateTime aDate) {
		ZonedDateTime last = lastRetrievedDocuments.get(product);

		if ((last == null) || aDate.isAfter(last)) {
			lastRetrievedDocuments.put(product, aDate);
		}
	}

	public void setRetrieveMedias(boolean retrieveMedias) {
		this.retrieveMedias = retrieveMedias;
	}
}
