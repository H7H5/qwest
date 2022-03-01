package qwestgroup.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qwestgroup.dao.DaoINT;
import qwestgroup.dao.Dao;
import qwestgroup.model.Purchase;

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
    public List<Purchase> allPurchare() {
        return dao.allPurchare();
    }

    @Override
    public List<Purchase> allPurchareBySection() {       //  /parts" /loadParts"  purchares
        List<Purchase> allPurchases = allPurchare();
        List<Purchase> purcharesSection = new ArrayList<>();
        for(int i = 0; i< allPurchases.size(); i++){
            if(allPurchases.get(i).getGroup()==0){
                purcharesSection.add(allPurchases.get(i));
            }
        }
        Collections.sort(purcharesSection, new ComparatorString());
        return purcharesSection;
    }

    @Override
    public Optional<Purchase> GetPurchareBySelection(String code) {      // /group/
        int array[] = FindSection(code);
        List<Purchase> purchases = allPurchare();
        for(int i = 0; i< purchases.size(); i++){
            if(purchases.get(i).getSection()==array[0]&& purchases.get(i).getGroup()==0){
                return Optional.ofNullable(purchases.get(i));
            }
        }
        return null;
    }

    @Override
    public List<Purchase> PurchasesByGroup(String s) {
        int arr[] = FindSection(s);
        List<Purchase> allPurchases = allPurchare();
        List<Purchase> purcharesGroup = new ArrayList<>();
        for(int i = 0; i < allPurchases.size(); i++){
            if(allPurchases.get(i).getSection()==arr[0] && allPurchases.get(i).getClas()==0
                    && allPurchases.get(i).getGroup()!=0){
                purcharesGroup.add(allPurchases.get(i));
            }
        }
        Collections.sort(purcharesGroup, new ComparatorString());
        return purcharesGroup;
    }

    @Override
    public Optional<Purchase> GetPurchaseByGroup(String code) {
        int group = getGroup(code);
        int section = getSection(code);
        List<Purchase> purchases = PurchasesByGroup(code);
        return  purchases.stream()
                .filter(purchase -> purchase.getSection() == section && purchase.getGroup() == group)
                .findAny();
    }

    @Override
    public List<Purchase> PurchasesByClass(String s) {
        List<Purchase> purchases = allPurchare();
        List<Purchase> purcharesClasses = new ArrayList<>();
        int arr[] = FindSection(s);
        for(int i = 0; i< purchases.size(); i++){
            if(purchases.get(i).getSection()==arr[0] && purchases.get(i).getGroup()==arr[1]
                    && purchases.get(i).getClas()!=0&& purchases.get(i).getCategory()==0){
                purcharesClasses.add(purchases.get(i));
            }
        }
        Collections.sort(purcharesClasses, new ComparatorString());
        return purcharesClasses;
    }

    @Override
    public Optional<Purchase> GetPurchaseByClass(String code) {
        final int clazz = getClass(code);
        List<Purchase> purchases = PurchasesByClass(code);
        return  purchases.stream()
                .filter(purchase -> purchase.getClas() == clazz)
                .findAny();
    }

    @Override
    public List<Purchase> PurcharesByCategory(String s) {
        List<Purchase> purchases = allPurchare();
        List<Purchase> purcharesCategory = new ArrayList<>();
        int arr[] = FindSection(s);
        for(int i = 0; i< purchases.size(); i++){
            if(purchases.get(i).getSection()==arr[0] && purchases.get(i).getGroup()==arr[1]
                    && purchases.get(i).getClas()==arr[2] &&  purchases.get(i).getCategory()!=0){
                purcharesCategory.add(purchases.get(i));
            }
        }
        Collections.sort(purcharesCategory, new ComparatorString());
        return purcharesCategory;
    }

    @Override
    public void add(List<Purchase> purchases) {
       dao.add(purchases);
    }

    @Override
    public void deleteAll() {
       dao.deleteAll();
    }

    @Override
    public Purchase getById(int id) {
        return dao.getById(id);
    }

    public void parseJSON(String server) {
        List<Purchase> purchases = new ArrayList<>();
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
                        Purchase purchase = new Purchase();
                        purchase.setId(AUTO_ID.getAndIncrement());

                        purchase.setCode(currentDynamicKey);
                        purchase.setName(name);
                        int array[] = FindSection(currentDynamicKey);
                        purchase.setSection(array[0]);
                        purchase.setGroup(array[1]);
                        purchase.setClas(array[2]);
                        purchase.setCategory(array[3]);
                        purchases.add(purchase);

                    }
                    //System.out.println(purchases.size());
                    add(purchases);
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

    private int getSection(String code){
        int array[] = FindSection(code);
        return array[0];
    }
    private int getGroup(String code){
        int array[] = FindSection(code);
        return array[1];
    }
    private int getClass(String code){
        int array[] = FindSection(code);
        return array[2];
    }
    private int getCategory(String code){
        int array[] = FindSection(code);
        return array[3];
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

