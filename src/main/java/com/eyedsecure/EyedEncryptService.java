package com.eyedsecure;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class EyedEncryptService {
	private ExecutorCompletionService<Response> completionService;

	/**
	 * Sets up thread pool for requests.
	 */
	public EyedEncryptService() {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(0, 50, 300L,
				TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
	    completionService = new ExecutorCompletionService<Response>(pool);
	}

	public Response fetch(List<String> urls, String userAgent, ResponseParser responseParser) throws RequestException {
	    List<Future<Response>> tasks = new ArrayList<Future<Response>>();
	    for(String url : urls) {
	    	tasks.add(completionService.submit(new ServerTask(url, userAgent, responseParser)));
	    }
	    Response response = null;
		try {
			int tasksDone = 0;
			Throwable savedException = null;
			Future<Response> futureResponse = completionService.poll(1L, TimeUnit.MINUTES);
			while(futureResponse != null) {
				try {
					tasksDone++;
					tasks.remove(futureResponse);
					response = futureResponse.get();

                } catch (CancellationException ignored) {
					tasksDone--;
				} catch (ExecutionException e) {
					savedException = e.getCause();
				}
				if(tasksDone >= urls.size()) {
					break;
				}
				futureResponse = completionService.poll(1L, TimeUnit.MINUTES);
			}
			if(futureResponse == null || response == null) {
				if(savedException != null) {
					throw new RequestException(
							"Exception while executing request.", savedException);
				} else {
					throw new RequestException("Request timeout.");
				}
			}
		} catch (InterruptedException e) {
			throw new RequestException("Request interrupted.", e);
		}

		for(Future<Response> task : tasks) {
			task.cancel(true);
		}

	    return response;
	}

	class ServerTask implements Callable<Response> {
		private final String url;
		private final String userAgent;
        private final ResponseParser responseParser;

		/**
		 * Set up a ServerTask
		 * @param url the url to be used
		 * @param userAgent sent to the server, or NULL to use default
		 */
		public ServerTask(String url, String userAgent, ResponseParser responseParser) {
			this.url = url;
			this.userAgent = userAgent;
            this.responseParser = responseParser;
		}

		/**
		 * Do the server query for given URL.
		 * @throws Exception
		 */
		public Response call() throws Exception {
			URL url = new URL(this.url);
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			if(userAgent == null) {
				conn.setRequestProperty("User-Agent", "eyedencrypt-java-client/" + Version.version());
			} else {
				conn.setRequestProperty("User-Agent", userAgent);
			}
			conn.setConnectTimeout(20000); // 20 second timeout
			conn.setReadTimeout(20000); // 20 second timeout for both read and connect
			return responseParser.parse(conn.getInputStream());
		}
		
	}
}