package com.project.cookEats.board_recipe;

import com.project.cookEats.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RecipeDBRepository extends JpaRepository<RecipeDB, Long> {



    // 총 레시피 수를 반환
    long count();


    //혜정 코드
    List<RecipeDB> findTop5ByOrderByLLIKEDesc();
    List<RecipeDB> findAllByMember(Member member);

    //혜정 코드
    @Query(nativeQuery = true, value = "select * from recipedb where rcp_nm like %?1% or rcp_parts_dtls like %?1% order by id desc limit 5")
    List<RecipeDB> findTotalSearch(String search);

    //혜정 코드
    @Query(nativeQuery = true, value="select * from recipedb where rcp_parts_dtls like %?1% order by rand() limit 1")
    Optional<RecipeDB> findRecommandRecipe(String ingredient);

    //혜정 코드
//    @Query(nativeQuery = true, value="select * from recipedb order by ?1 desc;")
//    Page<RecipeDB> findAllSort(String sortType);

    Page<RecipeDB> findAllByOrderByLLIKEDesc(Pageable pageable);

    @Query(value = "SELECT * FROM recipedb WHERE rcp_parts_dtls LIKE %?1% ",
            countQuery = "SELECT COUNT(*) FROM recipedb WHERE rcp_parts_dtls LIKE %?1%",
            nativeQuery = true)
    Page<RecipeDB> findAllSearch( String search, Pageable pageable);


}