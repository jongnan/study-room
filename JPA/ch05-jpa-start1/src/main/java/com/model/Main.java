package com.model;

import javax.persistence.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            testSave(em);
            updateRelation(em);
            biDirection(em);
            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        EntityManager em2 = emf.createEntityManager();
        EntityTransaction tx2 = em2.getTransaction();
        try {
            tx2.begin();
            biDirection(em2);
            tx2.commit();
        }catch (Exception e) {
            tx2.rollback();
        }finally {
            em2.close();
        }

        emf.close();
    }

    public static void testSave(EntityManager em) {
        Team t1 = new Team("t1", "team1");
        em.persist(t1);
        Member m1 = new Member("m1", "jong");
        m1.setTeam(t1);
        em.persist(m1);

        Member m2 = new Member("m2", "yoon");
        m2.setTeam(t1);
        em.persist(m2);
    }

    public static void queryLogicJoin(EntityManager em) {
        String jpql = "select m from Member m join m.team t where t.name=:teamName";
        List<Member> resultList = em.createQuery(jpql, Member.class)
            .setParameter("teamName", "team1")
            .getResultList();
        for (Member m : resultList) {
            System.out.println(m.getUsername());
        }
    }

    public static void updateRelation(EntityManager em) {
        Team t2 = new Team("t2", "team2");
        em.persist(t2);

        Member m = em.find(Member.class, "m1");
        m.setTeam(t2);
    }

    public static void deleteRelation(EntityManager em) {
        Member m = em.find(Member.class, "m1");
        m.setTeam(null);
    }

    public static void removeEntity(EntityManager em) {
        Team t1 = em.find(Team.class, "t1");
        em.remove(t1);
    }

    public static void biDirection(EntityManager em) {
        Team team = em.find(Team.class, "t1");
        List<Member> members = team.getMembers();
        for(Member m : members) {
            System.out.println(m.getUsername());
        }
    }
}
