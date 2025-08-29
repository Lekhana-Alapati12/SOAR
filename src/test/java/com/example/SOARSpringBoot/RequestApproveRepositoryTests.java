package com.example.SOARSpringBoot;

import com.example.SOARSpringBoot.entity.RequestApprove;
import com.example.SOARSpringBoot.repository.RequestApproveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RequestApproveRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RequestApproveRepository repository;


    @Test
    public void testSaveRequestApprove()
    {
//        RequestApprove req=new RequestApprove();
//        CardItem cardItem=entityManager.find(CardItem.class,1l);
//        //CardItem cardItem2=entityManager.find(CardItem.class,5l);
//        Set<CardItem> items=new HashSet<>();
//        items.add(cardItem);
//        req.setCardItems(items);
//        req.setStatus("Pending");
//        //req.setDate(new Date());
//        req.setRemarks_dev("approve the request");
//
//       RequestApprove requestApprove= repository.save(req);
        RequestApprove approve=repository.findById(5l).get();
        approve.setTotalItems(2);
        approve.setTotalCost(1200l);
        RequestApprove requestApprove= repository.save(approve);
        assertTrue(requestApprove.getCardItems().size()>0);
    }

//    @Test
//    public void testFindRequestApproveByList()
//    {
//        List<RequestApprove> list=repository.findById(Arrays.asList(new Long[]{3l}));
//        assertTrue(list.size()>0);
//    }
}
