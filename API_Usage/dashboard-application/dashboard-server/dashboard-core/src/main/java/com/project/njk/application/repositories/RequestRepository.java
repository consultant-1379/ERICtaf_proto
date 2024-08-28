package com.project.njk.application.repositories;

import com.project.njk.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by ekiajen on 16/06/2016.
 */
public interface RequestRepository extends CrudRepository<TeamMethod, Long> {

    /*@Query("count m from AnnotatedMethod where m.api_id")

    @Query("select m from TeamMethod tm where m.annotatedClasses.value = :productName")
    List<TeamMethod> findTeamClassByAnnotatedMethod(@Param("annotatedMethod") AnnotatedMethod annotatedMethod);*/

    @Query("select a.id from API a where a.value = :apiValue")
    int getApiId(@Param("apiValue") String apiValue);

    @Query("select count(m) from AnnotatedMethod m where m.api.id = :apiId")
    int countAnnotatedMethodsByApi(@Param("apiId") long apiId);

    @Query("select m from API m")
    List<API> getAllApi();

    @Query("select m from AnnotatedMethod m")
    List<AnnotatedMethod> getAllAnnotatedMethods();

    @Query("select m.name from AnnotatedClass m where m.api.value = :apiValue")
    List<AnnotatedClass> getAnnotatedClassByApi(@Param("apiValue") String apiValue);

    /*@Query("select c.name from TeamClass c where m.api.value = :apiValue")
    List<TeamClass> getTeamClassesByApi(@Param("apiValue") String apiValue);*/

    @Query("select c.name from TeamClass c where c.name != :apiValue")
    List<TeamClass> getTeamClassesByApi(@Param("apiValue") String apiValue);

    /*@Query("select m.name from TeamMethod m where m.api.value = :apiValue")
    List<TeamMethod> getTeamMethodsByApi(@Param("apiValue") String apiValue);*/

    @Query("select m.name from TeamMethod m where m.name != :apiValue")
    List<TeamMethod> getTeamMethodsByApi(@Param("apiValue") String apiValue);
}
