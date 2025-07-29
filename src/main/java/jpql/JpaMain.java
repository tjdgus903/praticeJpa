package jpql;

import javax.persistence.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            // 클래스 전체 조회
            // 파라미터 바인딩 시 이름 기준으로 처리하기!
            Member query1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
            System.out.println("result = "+query1);

            /*  하나의 값 데이터 조회 - 단일 객체 반환
            Member result = query1.getSingleResult();
            System.out.println("member : "+result);
            */
/*            // String 하나 조회
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            // 클래스나 변수 하나로 조회 안될 때 query
            Query query3 = em.createQuery("select m.username, m.age from Member m", Query.class);*/



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
