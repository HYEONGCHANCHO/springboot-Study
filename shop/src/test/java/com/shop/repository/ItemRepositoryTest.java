package com.shop.repository;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;


import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-Test.properties")
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;
    
    @Test
    @DisplayName(value = "상품 저장 테스트")
    public void createItemTest(){
        for (int i = 1; i<6; i=i+1) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName(value = "상품 조회 테스트")
    public void findByItemNmTest(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        Iterator<Item> iter = itemList.iterator();
        while(iter.hasNext()){
            System.out.println(iter.next());
        }
    }
    @Test
    @DisplayName(value = "가격 less than 테스트")
    public void findByPriceLessThanTest(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName(value = "가격 less than OrderByPriceDesc테스트")
    public void findByPriceLessThanOrderByPriceDescTest(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }
    @Test
    @DisplayName(value = "id 역정렬(2개의 컬럼) 테스트")
    public void findByIdGreaterThanOrderByIdDescPriceAscTest(){
        this.createItemTest();
        Pageable paging = PageRequest.of(1,5);
        List<Item> itemList = itemRepository.findByIdGreaterThanOrderByIdDescPriceAsc(5L,paging);
        //select * from item where id>5L
        for (Item item : itemList){
            System.out.println("====2page 자료 ==> " +item.toString());
        }
    }
    @Test
    @DisplayName(value = "pageable을 이용한 단일컬럼 정렬 테스트")
    public void findByIdGreaterThanTest() {
        this.createItemTest();
        Pageable paging = PageRequest.of(0,5, Sort.Direction.DESC,"id");
        List<Item> itemList = itemRepository.findByIdGreaterThan(paging,5L);
        for (Item item : itemList){
            System.out.println("====1page 자료 ==> " +item.toString());
        }
    }


    @Test
    @DisplayName(value = "pageable을 이용한 다중컬럼 정렬 테스트")
    public void findByIdGreaterThanTest2() {
        this.createItemTest();
        Pageable paging = PageRequest.of(0,5,
                Sort.by(new Sort.Order(Sort.Direction.DESC,"id"),
                        new Sort.Order(Sort.Direction.ASC,"price")) //여러개의 경우
            );
        List<Item> itemList = itemRepository.findByIdGreaterThan(paging,5L);
        for (Item item : itemList){
            System.out.println("====1page 자료 ==> " +item.toString());
            }
}
    @Test
    @DisplayName(value = "Query Annotation 테스트 ")
    public void findByItemDetailTest(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetail("상품 상세");
        for (Item item : itemList){
            System.out.println("====1page 자료 ==> " +item.toString());
        }
    }

    @Test
    @DisplayName(value = "Query Annotation native 테스트 ")
    public void findNativeAllTest(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findNativeAll();
        for (Item item : itemList){
            System.out.println("====1page 자료 ==> " +item.toString());
        }
    }
    @Test
    @DisplayName("Querydsl을 이용한 조회테스트1")
    public void querydslTest(){
        this.createItemTest();
        JPAQueryFactory queryFactory =new JPAQueryFactory(em);
        QItem qItem=QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"상품 상세"+"%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList=query.fetch();

        for (Item item : itemList){
            System.out.println("====1page 자료 ==> " +item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl을 이용한 id값을 이용해서 조회테스트2")
    public void querydslSelectOneTest(){
        this.createItemTest();
        JPAQueryFactory queryFactory =new JPAQueryFactory(em);
        QItem qItem=QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem) //여기까지 고정
                .where(qItem.id.eq(3L));
        Item itemOne=query.fetchOne();
            System.out.println("====1page 자료 ==> " +itemOne.toString());
        }

    public void createItemList2(){
        for(int i=1;i<11; i=i+1){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            if (i>5){
                item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
                item.setStockNumber(0);
            }
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);

        }
    }
    
    @Test
    @DisplayName("predicate 매개값을 활용한 querydsl 상품조회 테스트")
    public void queryDslTest2(){
        createItemTest();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;

        String itemDetail="상품 상세";
        int price =10003;
        String itemSellStat = "SELL";
        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+'%'));
        System.out.println("1차 판단: " +booleanBuilder.toString());
        booleanBuilder.and(item.price.gt(price));
        System.out.println("2차 판단: " +booleanBuilder.toString());
        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPageResult =itemRepository.findAll(booleanBuilder,pageable);

        System.out.println("total elements : " + itemPageResult);
        System.out.println("total elements  개수: " + itemPageResult.getTotalElements());
        List<Item> itemList = itemPageResult.getContent();
        for(Item ri : itemList){
            System.out.println("===>"+ri);

        }

    }

}





