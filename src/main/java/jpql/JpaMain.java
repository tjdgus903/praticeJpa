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
            member.setUsername("관리자");
            member.setAge(10);
            member.setTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();

            String query0 = "select " +
                        "         case when m.age <= 10 then '학생요금'"+
                        "              when m.age >= 10 then '경로요금'"+
                        "              else '일반요금'" +
                        "         end "+
                        "from Member m";
            String query1 = "select coalesce(m.username, '이름 없는 회원') from Member m";
            String query2 = "select nullif(m.username, '관리자') from Member m";
            List<String>result = em.createQuery(query1, String.class)
                            .getResultList();

            for (String s : result) {
                System.out.println("s = "+s);
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
