package com.example.SOARSpringBoot;

import com.example.SOARSpringBoot.entity.User;
import com.example.SOARSpringBoot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    public void testCreateUser()
    {
        User user=new User();
        user.setUsername("sravya");
        user.setPassword("P@ssword1");
        user.setEmailId("bcd@gmail.com");
        user.setRole("Manager");
        User savedUser=repository.save(user);
        User existsUser=entityManager.find(User.class,savedUser.getId());
        assertEquals(user.getEmailId(),existsUser.getEmailId());
    }

    @Test
    public void testFindUserByEmailId()
    {
        String emailId="abc@gmail.com";
       User user= repository.findByEmailId(emailId);
       assertNotNull(user);
    }
}
