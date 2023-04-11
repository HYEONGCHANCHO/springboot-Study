package com.shop.repository;

public interface ItemQueryString {
    String findByItemDetail="select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc";
    String findNativeAll="select * from item";
}
