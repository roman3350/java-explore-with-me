package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.ViewStatsDto;
import ru.practicum.ewm.model.EndPointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndPointHit, Long> {
    @Query("select new ru.practicum.ewm.ViewStatsDto(s.app, s.uri, count(distinct s.ip)) " +
            "from EndPointHit as s " +
            "where s.uri like concat(?3,'%') and s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc ")
    List<ViewStatsDto> getStatsUriAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm.ViewStatsDto(s.app, s.uri, count(s.ip)) " +
            "from EndPointHit as s " +
            "where s.uri like concat(?3,'%') and s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc ")
    List<ViewStatsDto> getStatsUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm.ViewStatsDto(s.app, s.uri, count(distinct s.ip)) " +
            "from EndPointHit as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc ")
    List<ViewStatsDto> getStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ewm.ViewStatsDto(s.app, s.uri, count(s.ip)) " +
            "from EndPointHit as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc ")
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end);
}