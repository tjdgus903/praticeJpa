package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);


            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            // 전체 값을 셋팅하는 것이 벌크 연산
            // 벌크연산은 영속성컨텍스트를 무시하고 db에 직접 쿼리
            // 자동 flush 호출
            // 벌크연산이 있으면 쿼리를 수행한 뒤 영속성컨텍스트 처리가 됨
            // 벌크연산 사용 뒤에는 영속성 컨텍스트를 초기화 하자
            int resultCount = em.createQuery("update Member m set m.age = 20")
                            .executeUpdate();

            em.clear();

            Member findMember = em.find(Member.class, member1.getId());

            System.out.println("result = "+findMember);

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
