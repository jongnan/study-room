package jpabook.start;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // 엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

        // 엔티티 매니저 팩토리를 통한 엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            logic(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void logic(EntityManager em) {
        String id = "jongnan";
        Member member = new Member();
        member.setId(id);
        member.setUserName("김종윤");
        member.setAge(29);

        // 등록
        em.persist(member);

        // 변경
        member.setAge(19);

        // 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember = " + findMember.getUserName() + ", age = " + findMember.getAge());

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("members.size = " + members.size());

        // 삭제
        em.remove(member);
    }
}
