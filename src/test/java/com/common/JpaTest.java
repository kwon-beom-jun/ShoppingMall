package com.common;

import com.shop.ShopWebApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.persistence.*;

public class JpaTest {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ShopWebApplication.class)
                .profiles("local")
                .run(args);

        JpaTestDto dto = new JpaTestDto();
        dto.setName("Kwon");
        dto.setAge(30);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpatest");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(dto);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
        }finally {
            em.close();
            emf.close();
        }
    }
}

