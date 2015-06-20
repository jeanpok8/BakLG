import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class OpenhabHttpClient {
	private HttpClient client;
	private StringBuilder url;

	public OpenhabHttpClient(String hostAddress, int port) {
		url = new StringBuilder();
		url.append("http://" + hostAddress + ":").append(port);
	}

	// take item name and value , push it to openhab.

	public int pushItemValue(String itemName, String value)
			throws ClientProtocolException, IOException {
		client = HttpClients.custom().build();
		url.append("/rest/items/").append(itemName);

		StringEntity stringEntity = new StringEntity(value);
		HttpUriRequest post = RequestBuilder.post().setUri(url.toString())
				.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
				.setEntity(stringEntity).build();
		HttpResponse response = client.execute(post);
		System.out.println(response.getStatusLine().getStatusCode());
		return response.getStatusLine().getStatusCode();
	}


	// takes item name and print its value
	public String pullItemValue(String itemName)
			throws ClientProtocolException, IOException {
		client = HttpClients.custom().build();
		url.append("/rest/items/").append(itemName);
		HttpUriRequest get = RequestBuilder.get().setUri(url.toString())
				.setHeader("Accept", "application/json").build();

		HttpResponse response = client.execute(get);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(result.toString());
		return result.toString();
	}
}
