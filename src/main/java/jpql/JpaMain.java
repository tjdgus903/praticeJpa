package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            // inner 조인
            String query1 = "select m from Member m inner join m.team t";
            // outer 조인
            String query2 = "select m from Member m left outer join m.team t";
            // 세타조인(막조인)
            String query3 = "select m from Member m, Team t where m.username = t.name";
            // 조인 대상 필터링(jpql)
            String query4 = "select m from Member m left join m.team t on t.name = 'teamA'";
            // 조인 대상 필터링(sql)
            String query5 = "select m from Member m left join Team t on m.username = t.name";
            List<Member> result = em.createQuery(query5, Member.class)
                            .setFirstResult(1)
                            .setMaxResults(10)
                            .getResultList();

            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }

        emf.close();

    }
}
