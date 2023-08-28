import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    private static final String POST_URL = "http://localhost:8080/loginValidate";
    private static final String POST_PARAMS = "username=dude&password=dude";
    private static String sendPOST() throws IOException {
        URL url = new URL(POST_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");

        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(POST_PARAMS.getBytes());
        outputStream.flush();
        outputStream.close();

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("POST Response Code ::" + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            if (response.toString().contains("welcome")){
                System.out.println("success");
                return "success";
            }else {
                System.out.println("denied");
                return "denied";
            }
            //System.out.println(response);
        } else {
            System.out.println("POST request did not worked");
        }
        return "wtf happened?";
    }

    private static void MrBrute(){
        
    }

    public static void main(String[] args) throws IOException {
        sendPOST();
    }
}