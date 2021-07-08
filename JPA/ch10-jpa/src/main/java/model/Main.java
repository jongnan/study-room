package model;

import com.mysema.query.jpa.impl.JPAQuery;
import model.entity.Member;
import model.entity.QMember;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

        EntityManager em = emf.createEntityManager();

        JPAQuery query = new JPAQuery(em);
        // 별칭이 m인 멤버
        QMember qMember = new QMember("m");
        List<Member> members = query.from(qMember).where(qMember.name.eq("회원1")).orderBy(qMember.name.desc()).list(qMember);

        em.createQuery("").executeUpdate();
    }
}
