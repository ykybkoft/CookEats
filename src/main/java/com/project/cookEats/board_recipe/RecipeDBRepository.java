package com.project.cookEats.board_recipe;

import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeDBRepository extends JpaRepository<RecipeDB, Long> {

    @Query("SELECT r FROM RecipeDB r WHERE r.RCP_PARTS_DTLS LIKE %:ingredientName% AND "
            + "(r.MANUAL01 IS NOT NULL AND r.MANUAL01 <> '' OR "
            + "(r.MANUAL02 IS NOT NULL AND r.MANUAL02 <> '' AND r.MANUAL01 IS NOT NULL AND r.MANUAL01 <> '') OR "
            + "(r.MANUAL03 IS NOT NULL AND r.MANUAL03 <> '' AND r.MANUAL02 IS NOT NULL AND r.MANUAL02 <> '') OR "
            + "(r.MANUAL04 IS NOT NULL AND r.MANUAL04 <> '' AND r.MANUAL03 IS NOT NULL AND r.MANUAL03 <> '') OR "
            + "(r.MANUAL05 IS NOT NULL AND r.MANUAL05 <> '' AND r.MANUAL04 IS NOT NULL AND r.MANUAL04 <> '') OR "
            + "(r.MANUAL06 IS NOT NULL AND r.MANUAL06 <> '' AND r.MANUAL05 IS NOT NULL AND r.MANUAL05 <> '') OR "
            + "(r.MANUAL07 IS NOT NULL AND r.MANUAL07 <> '' AND r.MANUAL06 IS NOT NULL AND r.MANUAL06 <> '') OR "
            + "(r.MANUAL08 IS NOT NULL AND r.MANUAL08 <> '' AND r.MANUAL07 IS NOT NULL AND r.MANUAL07 <> '') OR "
            + "(r.MANUAL09 IS NOT NULL AND r.MANUAL09 <> '' AND r.MANUAL08 IS NOT NULL AND r.MANUAL08 <> '') OR "
            + "(r.MANUAL10 IS NOT NULL AND r.MANUAL10 <> '' AND r.MANUAL09 IS NOT NULL AND r.MANUAL09 <> '') OR "
            + "(r.MANUAL11 IS NOT NULL AND r.MANUAL11 <> '' AND r.MANUAL10 IS NOT NULL AND r.MANUAL10 <> '') OR "
            + "(r.MANUAL12 IS NOT NULL AND r.MANUAL12 <> '' AND r.MANUAL11 IS NOT NULL AND r.MANUAL11 <> '') OR "
            + "(r.MANUAL13 IS NOT NULL AND r.MANUAL13 <> '' AND r.MANUAL12 IS NOT NULL AND r.MANUAL12 <> '') OR "
            + "(r.MANUAL14 IS NOT NULL AND r.MANUAL14 <> '' AND r.MANUAL13 IS NOT NULL AND r.MANUAL13 <> '') OR "
            + "(r.MANUAL15 IS NOT NULL AND r.MANUAL15 <> '' AND r.MANUAL14 IS NOT NULL AND r.MANUAL14 <> '') OR "
            + "(r.MANUAL16 IS NOT NULL AND r.MANUAL16 <> '' AND r.MANUAL15 IS NOT NULL AND r.MANUAL15 <> '') OR "
            + "(r.MANUAL17 IS NOT NULL AND r.MANUAL17 <> '' AND r.MANUAL16 IS NOT NULL AND r.MANUAL16 <> '') OR "
            + "(r.MANUAL18 IS NOT NULL AND r.MANUAL18 <> '' AND r.MANUAL17 IS NOT NULL AND r.MANUAL17 <> '') OR "
            + "(r.MANUAL19 IS NOT NULL AND r.MANUAL19 <> '' AND r.MANUAL18 IS NOT NULL AND r.MANUAL18 <> '') OR "
            + "(r.MANUAL20 IS NOT NULL AND r.MANUAL20 <> '' AND r.MANUAL19 IS NOT NULL AND r.MANUAL19 <> ''))")
    Page<RecipeDB> findByIngredientName(@Param("ingredientName") String ingredientName, Pageable pageable);

    // 제목에 키워드가 포함된 게시글을 조회수 기준으로 내림차순 정렬
    @Query("SELECT r FROM RecipeDB r WHERE r.RCP_NM LIKE %:keyword% ORDER BY r.CCOUNT DESC")
    List<RecipeDB> findByKeywordOrderByCountDesc(@Param("keyword") String keyword);

    // 제목에 키워드가 포함된 게시글을 추천수 기준으로 내림차순 정렬
    @Query("SELECT r FROM RecipeDB r WHERE r.RCP_NM LIKE %:keyword% ORDER BY r.LLIKE DESC")
    List<RecipeDB> findByTitleContainingOrderByLikesDesc(@Param("keyword") String keyword);

    // 제목에 키워드가 포함된 게시글을 작성일 기준으로 내림차순 정렬
    @Query("SELECT r FROM RecipeDB r WHERE r.RCP_NM LIKE %:keyword% ORDER BY r.SYSDATE DESC")
    List<RecipeDB> findByTitleContainingOrderBySysDateDesc(@Param("keyword") String keyword);

    // 총 레시피 수를 반환
    long count();

    //혜정
    List<RecipeDB> findTop5ByOrderByLLIKEDesc();
    List<RecipeDB> findAllByMember(Member member);

    @Query(nativeQuery = true, value = "select * from recipedb where rcp_nm like %?1% or manual like %?1% order by id desc limit 5")
    List<RecipeDB> findTotalSearch(String search);
}