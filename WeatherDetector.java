import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherDetector {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();
        scanner.close();

        String apiKey = "YOUR_API_KEY_HERE"; // Replace with your OpenWeatherMap API key
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                            + city + "&appid=" + apiKey + "&units=metric";

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Convert to String
                String json = response.toString();

                // Extract key values manually
                String temp = extractValue(json, "\"temp\":", ",");
                String humidity = extractValue(json, "\"humidity\":", ",");
                String description = extractValue(json, "\"description\":\"", "\"");

                System.out.println("\n🌍 Weather in " + city + ":");
                System.out.println("🌡 Temperature: " + temp + "°C");
                System.out.println("💧 Humidity: " + humidity + "%");
                System.out.println("🌤 Condition: " + description);
            } else {
                System.out.println("❌ City not found or API error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract simple JSON values
    private static String extractValue(String json, String key, String endChar) {
        try {
            int start = json.indexOf(key);
            if (start == -1) return "N/A";
            start += key.length();
            int end = json.indexOf(endChar, start);
            return json.substring(start, end).replace("\"", "").trim();
        } catch (Exception e) {
            return "N/A";
        }
    }
}
