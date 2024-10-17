package tn.wincom.winroue.interfaces;

import java.util.List;

public interface InterfaceCRUD<C> {
    void add(C c);
    void delete(C c);
    void update(C c);
    boolean search(C c);
    List<C> getAll();
    List<C> tri(List<C> list, String criteria);
}

