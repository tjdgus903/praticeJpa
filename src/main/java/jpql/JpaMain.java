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

            em.flush();
            em.clear();

            // concat 을 사용하지 않고 붙힐 수 있음
            // String query1 = "select m From Member m";
            String query2 = "select m From Member m join fetch m.team";
            // String query3 = "select t From Team t join fetch t.members";
            String query4 = "select distinct t From Team t join fetch t.members";

            List<Member> result1 = em.createQuery(query2, Member.class)
                            .getResultList();
            List<Team> result2 = em.createQuery(query4, Team.class)
                    .getResultList();

            System.out.println("result = "+result2.size());

            for (Member member : result1) {
                //System.out.println("member = "+member.getUsername()+", "+member.getTeam().getName());
                // query1 캐이스
                // member = 회원1, teamA - sql 처리
                // member = 회원2, teamA - 영속성 컨텍스트
                // member = 회원3, teamB - sql
                // 조회하고자 하는 팀이 달라서 각각의 회원의
                // 연관된 팀을 조회하기 위해서 데이터 sql 조회
                // query2 캐이스(join fetch)
                // 한번에 연관 테이블 같이 조회해서 다른 데이터 조회시 테이블 추가 조회 안해도 됨
            }
            for (Team team : result2) {
                // 기존 join 과 fetch join 차이의 이유는
                // 기존 join 이 jpa 를 통해 join select 를 할 때 영속성 컨텍스트에 데이터가 없으면
                // 바로 쿼리를 수행하여 조회를 하는데 이게 중복 데이터가 없으면 n+1 개의 데이터를 조회할 수 있음
                // 그러면 너무 sql 문 수행이 많아 져 db 성능 저하가 생길 수 있음
                // 이 때 fetch join을 사용하면 해당 데이터에 대하여 필요한 정보들을 미리
                // join fetch 를 통해 영속성 컨텍스트에 넣어놓아서 데이터를 조회할 때마다
                // 굳이 select 쿼리를 수행 안해도 됨
                System.out.println("team = "+team.getName() + " | members = "+team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> Member = "+member);
                }
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
