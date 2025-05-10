package pl.wajhub.server.service;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import java.io.IOException;


@Service
public class ExchangeService {

    private final String url;

    public ExchangeService() {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        url = "https://v6.exchangerate-api.com/v6/"+ apiKey +"/latest/";
    }

    public Double getRate(
            String sourceCurrencyCode,
            String destinationCurrencyCode) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url+sourceCurrencyCode)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        JSONObject root = new JSONObject(response.body().string());
        JSONObject rates = root.getJSONObject("conversion_rates");
        return rates.getDouble(destinationCurrencyCode);
    }
}
