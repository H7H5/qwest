package qwestgroup.service;

import qwestgroup.model.Purchare;

import java.util.Comparator;

public class ComparatorString implements Comparator<Purchare> {
    @Override
    public int compare(Purchare o1, Purchare o2) {
        return o1.getCode().compareTo(o2.getCode());
    }
}




