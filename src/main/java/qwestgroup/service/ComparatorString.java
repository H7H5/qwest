package qwestgroup.service;

import qwestgroup.model.Purchase;

import java.util.Comparator;

public class ComparatorString implements Comparator<Purchase> {
    @Override
    public int compare(Purchase o1, Purchase o2) {
        return o1.getCode().compareTo(o2.getCode());
    }
}




