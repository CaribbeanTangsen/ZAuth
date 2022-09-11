package cn.lijilong.zauth.dto;

public interface TreeEntity<T,V> {


    V getValue();

    String getLabel();

    boolean isDisable();

    T getSuperId();

    T getId();

}
