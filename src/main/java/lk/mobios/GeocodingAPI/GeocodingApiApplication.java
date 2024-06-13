package lk.mobios.GeocodingAPI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class GeocodingApiApplication {

	private static final String API_KEY = "Your API KEY";

	public static void main(String[] args) {
		double latitude = 6.9383467;
		double longitude = 80.0232167;

		String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s",
				latitude, longitude, API_KEY);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			JSONObject json = new JSONObject(response.body());
			JSONArray results = json.getJSONArray("results");
			String longName = "";
			for (int i = 0; i < results.length(); i++) {
				JSONObject result = results.getJSONObject(i);
				JSONArray addressComponents = result.getJSONArray("address_components");

				for (int j = 0; j < addressComponents.length(); j++) {
					JSONObject component = addressComponents.getJSONObject(j);
					JSONArray types = component.getJSONArray("types");

					for (int k = 0; k < types.length(); k++) {
						if (types.getString(k).equals("administrative_area_level_2")) {
							longName = component.getString("long_name");
						}
					}
				}
			}
			System.out.println(longName);
		}catch (IOException | InterruptedException e){
			e.printStackTrace();
		}
//		SpringApplication.run(GeocodingApiApplication.class, args);
	}

}
