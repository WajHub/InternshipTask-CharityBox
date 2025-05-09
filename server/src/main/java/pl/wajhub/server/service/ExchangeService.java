package pl.wajhub.server.service;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import pl.wajhub.server.model.MyCurrency;

import java.io.IOException;


@Service
public class ExchangeService {

    private String baseUrl;
    private String apiKey;

    public ExchangeService() {
        Dotenv dotenv = Dotenv.load();
        apiKey = dotenv.get("API_KEY");
        baseUrl = "https://api.currencyfreaks.com/v2.0/convert/latest?apikey="+apiKey + "&from=USD&to=PKR&amount=500";
    }

    public Double exchange(
            MyCurrency sourceCurrency,
            MyCurrency destinationCurrency,
            Double amount) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://v6.exchangerate-api.com/v6/"+apiKey+ "/latest/"+sourceCurrency)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        JSONObject root = new JSONObject(response.body().string());
        JSONObject rates = root.getJSONObject("conversion_rates");
        Double rate = rates.getDouble(destinationCurrency.toString());
        return rate*amount;
    }
}
