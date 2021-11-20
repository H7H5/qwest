package qwestgroup.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qwestgroup.dao.DaoINT;
import qwestgroup.dao.Dao;
import qwestgroup.model.Purchare;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Services implements ServiceINT {

    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);

    private DaoINT dao;
    @Autowired
    public void setDao(Dao dao){
        this.dao = dao;
    }

    @Override
    public List<Purchare> allPurchare() {
        return dao.allPurchare();
    }

    @Override
    public List<Purchare> allPurchareBySection() {       //  /parts" /loadParts"  purchares
        List<Purchare> allPurchares = allPurchare();
        List<Purchare> purcharesSection = new ArrayList<>();
        for(int i = 0; i<allPurchares.size();i++){
            if(allPurchares.get(i).getGroup()==0){
                purcharesSection.add(allPurchares.get(i));
            }
        }
        Collections.sort(purcharesSection, new ComparatorString());
        return purcharesSection;
    }

    @Override
    public Purchare GetPurchareBySelection(String code) {      // /group/
        int array[] = FindSection(code);
        List<Purchare> purchares = allPurchare();
        for(int i = 0; i<purchares.size();i++){
            if(purchares.get(i).getSection()==array[0]&&purchares.get(i).getGroup()==0){
                return purchares.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Purchare> PurcharesByGroup(String s) {
        int arr[] = FindSection(s);
        List<Purchare> allPurchares = allPurchare();
        List<Purchare> purcharesGroup = new ArrayList<>();
        for(int i = 0; i < allPurchares.size();i++){
            if(allPurchares.get(i).getSection()==arr[0] && allPurchares.get(i).getClas()==0
                    && allPurchares.get(i).getGroup()!=0){
                purcharesGroup.add(allPurchares.get(i));
            }
        }
        Collections.sort(purcharesGroup, new ComparatorString());
        return purcharesGroup;
    }

    @Override
    public Purchare GetPurchareByGroup(String code) {
        int array[] = FindSection(code);
        List<Purchare> purchares = PurcharesByGroup(code);
        for(int i = 0; i<purchares.size();i++){
            if(purchares.get(i).getSection()==array[0]&&purchares.get(i).getGroup()==array[1]){
                return purchares.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Purchare> PurcharesByClass(String s) {
        List<Purchare> purchares = allPurchare();
        List<Purchare> purcharesClass = new ArrayList<>();
        int arr[] = FindSection(s);
        for(int i = 0; i<purchares.size();i++){
            if(purchares.get(i).getSection()==arr[0] && purchares.get(i).getGroup()==arr[1]
                    && purchares.get(i).getClas()!=0&& purchares.get(i).getCategory()==0){
                purcharesClass.add(purchares.get(i));
            }
        }
        Collections.sort(purcharesClass, new ComparatorString());
        return purcharesClass;
    }

    @Override
    public Purchare GetPurchareByClass(String code) {
        int array[] = FindSection(code);
        List<Purchare> purchares = PurcharesByClass(code);
        for(int i = 0; i<purchares.size();i++){
            if(purchares.get(i).getClas()==array[2]){
                return purchares.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Purchare> PurcharesByCategory(String s) {
        List<Purchare> purchares = allPurchare();
        List<Purchare> purcharesCategory = new ArrayList<>();
        int arr[] = FindSection(s);
        for(int i = 0; i<purchares.size();i++){
            if(purchares.get(i).getSection()==arr[0] && purchares.get(i).getGroup()==arr[1]
                    && purchares.get(i).getClas()==arr[2] &&  purchares.get(i).getCategory()!=0){
                purcharesCategory.add(purchares.get(i));
            }
        }
        Collections.sort(purcharesCategory, new ComparatorString());
        return purcharesCategory;
    }

    @Override
    public void add(List<Purchare> purchares) {
       dao.add(purchares);
    }

    @Override
    public void deleteAll() {
       dao.deleteAll();
    }

    @Override
    public Purchare getById(int id) {
        return dao.getById(id);
    }

    public void parseJSON(String server) {
        List<Purchare> purchares = new ArrayList<>();
        String answerHTTP;
        URL url;
        HttpsURLConnection urlConnection = null;
        try {
            url = new URL(server);
            urlConnection = (HttpsURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpsURLConnection.HTTP_OK){
                answerHTTP = convertInputStreamToString(urlConnection.getInputStream());
                try {
                    deleteAll();
                    JSONObject questionMark = new JSONObject(answerHTTP);
                    Iterator keys = questionMark.keys();
                    while(keys.hasNext()) {
                        String currentDynamicKey = (String)keys.next();
                        String name = questionMark.getString(currentDynamicKey);
                        Purchare purchare = new Purchare();
                        purchare.setId(AUTO_ID.getAndIncrement());

                        purchare.setCode(currentDynamicKey);
                        purchare.setName(name);
                        int array[] = FindSection(currentDynamicKey);
                        purchare.setSection(array[0]);
                        purchare.setGroup(array[1]);
                        purchare.setClas(array[2]);
                        purchare.setCategory(array[3]);
                        purchares.add(purchare);

                    }
                    //System.out.println(purchares.size());
                    add(purchares);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private String convertInputStreamToString(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in , "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    int[] FindSection(String s){
        int[] myArray = new int[4];
        int section;
        char c = s.charAt(0);
        int x0 = Integer.parseInt(c+"");
        c = s.charAt(1);
        int x1 = Integer.parseInt(c+"");
        if(x0==0){
            section = x1;
        }else {
            section = x0 * 10 + x1;
        }
        myArray[0] = section;

        for(int i =1; i< 4;i++){
            c = s.charAt(i+1);
            myArray[i] = Integer.parseInt(c+"");
        }
        return myArray;
    }
}

