package qwestgroup.service;

import qwestgroup.model.Purchare;

import java.util.List;

public interface ServiceINT {
    List<Purchare> allPurchare();
    List<Purchare> allPurchareBySection();
    List<Purchare> PurcharesByGroup(String code);
    List<Purchare> PurcharesByClass(String code);
    List<Purchare> PurcharesByCategory(String code);
    Purchare GetPurchareBySelection(String code);
    Purchare GetPurchareByGroup(String code);
    Purchare GetPurchareByClass(String code);
    void add(List<Purchare> purchares);
    void deleteAll();
    Purchare getById(int id);
}
