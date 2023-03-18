import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String APPIDQUERY = "&APPID=";
    private static final String app_id="69d63ff2e9d76aeec91842f6081657db";

    public static URL buildURLFromString(String location){
        URL myURL = null;
        try{
            myURL = new URL(BASE_URL + location + APPIDQUERY + app_id);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return myURL;
    }

    public static URL buildURLFromCoords(String latitude, String longitude){
        URL myURL = null;
        try{
            myURL = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude +
                    "&lon=" + longitude + APPIDQUERY + app_id);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return myURL;
    }

    public static String getDataFromURL(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();

            //The scanner trick: search for the next "beginning" of the input stream
            //No need to user BufferedReader
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else{
                return null;
            }

        }finally {
            urlConnection.disconnect();
        }
    }
}