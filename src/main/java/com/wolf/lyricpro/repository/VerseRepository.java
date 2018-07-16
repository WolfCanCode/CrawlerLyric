package com.wolf.lyricpro.repository;

import com.wolf.lyricpro.model.Song;
import com.wolf.lyricpro.model.Verse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerseRepository extends JpaRepository<Verse, Long> {
    @Query("select v from Verse v where v.song = :song")
    List<Verse> findAllBySong(@Param("song") Song song);
}
