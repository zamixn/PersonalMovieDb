//package com.zkadisa.personalmoviedb.DataHandling;
//
//import org.json.JSONException;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class RequestOperator extends Thread{
//
//    public interface RequestOperatorListener{
//        void success (Entry publication);
//        void failed (int responseCode);
//    }
//
//    private RequestOperatorListener listener;
//    private int responseCode;
//
//    public void setListener(RequestOperatorListener listener) { this.listener = listener; }
//
//    @Override
//    public void run(){
//        super.run();
//        try{
//            Entry publication = request();
//
//            if(publication != null)
//                success(publication);
//            else
//                failed(responseCode);
//        }catch (IOException e) {
//            failed(-1);
//        }catch (JSONException e){
//            failed(-2);
//        }
//    }
//
//    private Entry request() throws IOException, JSONException{
//        URL obj = new URL("http://jsonplaceholder.typicode.com/posts/1");
//
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("GET");
//
//        con.setRequestProperty("Content-Type", "application/json");
//
//        responseCode = con.getResponseCode();
//
//        System.out.println("Response Code:" + responseCode);
//
//        InputStreamReader streamReader;
//
//        if(responseCode == 200){
//            streamReader = new InputStreamReader(con.getInputStream());
//        } else{
//            streamReader = new InputStreamReader(con.getErrorStream());
//        }
//
//        BufferedReader in = new BufferedReader(streamReader);
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while((inputLine = in.readLine()) != null){
//            response.append(inputLine);
//        }
//        in.close();
//
//        if(responseCode == 200)
//            return  parsingJsonObject(response.toString());
//        else
//            return  null;
//    }
//
//    public Entry parsingJsonObject(String response) throws JSONException{
//
//    }
//
//    private void failed(int code){
//        if(listener != null)
//            listener.failed(code);
//    }
//
//    private void success(Entry publication){
//        if(listener != null)
//            listener.success(publication);
//    }
//}
