package com.shop.repository;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
//@TestPropertySource(locations = "classpath:application-Test.properties")
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    
    @Test
    @DisplayName(value = "상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
        assertEquals(3,itemRepository.count());

//        itemRepository.delete(item);
//        assertEquals(0,itemRepository.count());
        List<Item> list= itemRepository.findByPrice(10000);
        for (Item getitem:list ){
            System.out.println("=============================="+getitem);
        }
    }
}