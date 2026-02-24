package com.example.vibeapp.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA EntityManager를 직접 사용하는 게시글 태그 레포지토리
 * (Spring Data JPA의 JpaRepository는 사용하지 않음)
 */
@Repository
public class PostTagRepository {

    // 영속성 컨텍스트(EntityManager)를 주입 받음
    @PersistenceContext
    private EntityManager em;

    /**
     * 태그 저장
     * em.persist(): 새 태그 엔티티를 영속 상태로 전환 → INSERT 쿼리 실행
     */
    public void save(PostTag postTag) {
        em.persist(postTag);
    }

    /**
     * 태그 ID로 단건 삭제
     */
    public void deleteById(Long id) {
        PostTag tag = em.find(PostTag.class, id);
        if (tag != null) {
            // em.remove(): 영속 상태의 엔티티를 삭제 상태로 전환 → DELETE 쿼리 실행
            em.remove(tag);
        }
    }

    /**
     * 특정 게시글의 태그 전체 삭제 (JPQL 벌크 DELETE)
     * 게시글 수정 시 기존 태그를 모두 지우고 새로 입력하는 방식에 사용
     */
    public void deleteByPostNo(Long postNo) {
        // JPQL 벌크 연산: 조건에 맞는 레코드를 한 번에 삭제
        em.createQuery("DELETE FROM PostTag pt WHERE pt.postNo = :postNo")
          .setParameter("postNo", postNo)
          .executeUpdate();
    }

    /**
     * 특정 게시글의 태그 목록 조회 (JPQL)
     */
    public List<PostTag> findByPostNo(Long postNo) {
        return em.createQuery("SELECT pt FROM PostTag pt WHERE pt.postNo = :postNo", PostTag.class)
                 .setParameter("postNo", postNo)
                 .getResultList();
    }
}
