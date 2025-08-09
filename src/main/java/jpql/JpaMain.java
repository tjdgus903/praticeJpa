package jpql;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();

            String query1 = "select m.username, 'HELLO', TRUE from Member m" +
                        "       where m.type = jpql.MemberType.ADMIN";
            String query2 = "select m.username, 'HELLO', TRUE from Member m" +
                        "       where m.type = :userType";
            List<Object[]> result = em.createQuery(query2)
                            .setParameter("userType", MemberType.ADMIN)
                            .getResultList();

            for (Object[] objects : result) {
                System.out.println("member = "+objects[0]);
                System.out.println("member = "+objects[1]);
                System.out.println("member = "+objects[2]);
            }

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
