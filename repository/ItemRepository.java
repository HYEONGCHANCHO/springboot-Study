package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item,Long>, QuerydslPredicateExecutor<Item> {



    /*
    List<Item> findByPrice(int i);
    List<Item> findByItemNm(String itemNm);
     */

     List<Item> findByItemNm(String itemNm);
//    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    //select * from item where ItemNm=#{첫번째 전달값} or ItemDetail = #{두번째 전달값}

    List<Item> findByPriceLessThan(Integer price);
    List<Item> findByPriceLessThanOrderByPriceDesc(int price);
//    List<Item> findByOrderByPriceDescPriceLessThan(int price);
    List<Item> findByIdGreaterThanOrderByIdDescPriceAsc(Long id, Pageable pg);

    List<Item> findByIdGreaterThan(Pageable pageable, Long id);

//    @Query("select i from Item i") //select * from Item

    @Query(ItemQueryString.findByItemDetail)
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value = ItemQueryString.findNativeAll,nativeQuery = true)
    List<Item> findNativeAll();

}
