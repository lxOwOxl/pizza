package com.example.pizza.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeService {

    private static final String API_KEY = "a92c8be550113c4645fff887"; // Thay YOUR_API_KEY bằng API key thực tế
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public BigDecimal convertVNDToUSD(BigDecimal amountVND) throws Exception {
        // Tạo URL API để lấy tỷ giá từ VND sang USD
        String urlString = BASE_URL + API_KEY + "/latest/VND"; // API lấy tỷ giá từ VND

        // Gửi yêu cầu GET tới API
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Đọc phản hồi từ API
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Phân tích JSON để lấy tỷ giá
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");

        // Lấy tỷ giá từ VND sang USD
        BigDecimal exchangeRateVNDToUSD = conversionRates.getBigDecimal("USD");
        System.out.println("exchangeRateVNDToUSD " + exchangeRateVNDToUSD);

        // Chuyển đổi số tiền từ VND sang USD
        BigDecimal amountInUSD = amountVND.multiply(exchangeRateVNDToUSD);
        return amountInUSD;
    }

}