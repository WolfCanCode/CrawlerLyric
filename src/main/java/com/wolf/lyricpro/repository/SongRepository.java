package com.wolf.lyricpro.repository;

import com.wolf.lyricpro.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Song findById(Integer songID);

    @Query("select s  from Song s where s.name like %:songName ")
    List<Song> findByName(@Param("songName") String songName);
}
