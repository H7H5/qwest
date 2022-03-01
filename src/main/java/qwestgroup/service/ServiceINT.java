package qwestgroup.service;

import qwestgroup.model.Purchase;

import java.util.List;
import java.util.Optional;

public interface ServiceINT {
    List<Purchase> allPurchare();
    List<Purchase> allPurchareBySection();
    List<Purchase> PurchasesByGroup(String code);
    List<Purchase> PurchasesByClass(String code);
    List<Purchase> PurcharesByCategory(String code);
    Optional<Purchase> GetPurchareBySelection(String code);
    Optional<Purchase> GetPurchaseByGroup(String code);
    Optional<Purchase> GetPurchaseByClass(String code);
    void add(List<Purchase> purchases);
    void deleteAll();
    Purchase getById(int id);
}
