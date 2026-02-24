package com.example.vibeapp.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA EntityManager를 직접 사용하는 게시글 레포지토리
 * (Spring Data JPA의 JpaRepository는 사용하지 않음)
 */
@Repository
public class PostRepository {

    // 영속성 컨텍스트(EntityManager)를 주입 받음
    @PersistenceContext
    private EntityManager em;

    /**
     * 게시글 전체 목록 조회 (JPQL, 최신순 정렬)
     */
    public List<Post> findAll() {
        // JPQL: 엔티티 이름(Post)과 필드 이름(no)을 사용
        return em.createQuery("SELECT p FROM Post p ORDER BY p.no DESC", Post.class)
                 .getResultList();
    }

    /**
     * 게시글 단건 조회
     * em.find(): PK로 엔티티를 조회하며, 1차 캐시(영속성 컨텍스트)를 먼저 확인함
     */
    public Optional<Post> findById(Long no) {
        return Optional.ofNullable(em.find(Post.class, no));
    }

    /**
     * 게시글 전체 건수 조회
     */
    public int count() {
        Long cnt = em.createQuery("SELECT COUNT(p) FROM Post p", Long.class)
                     .getSingleResult();
        return cnt.intValue();
    }

    /**
     * 게시글 등록
     * em.persist(): 비영속 엔티티를 영속 상태로 전환 → INSERT 쿼리 실행
     * IDENTITY 전략이므로 persist() 호출 즉시 INSERT되어 ID(no)가 채워짐
     */
    public void insert(Post post) {
        em.persist(post);
    }

    /**
     * 게시글 수정
     * 서비스 계층이 @Transactional이면 findById()로 가져온 엔티티는 영속 상태이므로
     * 필드를 변경하기만 해도 트랜잭션 커밋 시 변경 감지(Dirty Checking)로 자동 UPDATE됨.
     * 이 메서드는 비영속 상태(detached) 엔티티를 병합(merge)할 때 사용할 수 있음.
     */
    public Post merge(Post post) {
        // em.merge(): 비영속 엔티티를 영속성 컨텍스트에 병합하여 변경 감지 대상으로 만듦
        return em.merge(post);
    }

    /**
     * 게시글 삭제
     * em.remove(): 영속 상태의 엔티티를 삭제 → DELETE 쿼리 실행
     */
    public void delete(Long no) {
        Post post = em.find(Post.class, no);
        if (post != null) {
            em.remove(post);
        }
    }

    /**
     * 게시글 조회수 증가 (JPQL UPDATE)
     * 변경 감지가 아닌 JPQL 벌크 연산으로 직접 UPDATE
     */
    public void updateViews(Long no) {
        em.createQuery("UPDATE Post p SET p.views = p.views + 1 WHERE p.no = :no")
          .setParameter("no", no)
          .executeUpdate();
    }
}
