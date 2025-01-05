package icu.samnyan.aqua.sega.ongeki.dao.userdata;

import icu.samnyan.aqua.net.games.GenericUserMusicRepo;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserData;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserMusicDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Repository("OngekiUserMusicDetailRepository")
public interface UserMusicDetailRepository extends JpaRepository<UserMusicDetail, Long>, GenericUserMusicRepo<UserMusicDetail> {

    List<UserMusicDetail> findByUser_Card_ExtId(long userId);

    Page<UserMusicDetail> findByUser_Card_ExtId(long userId, Pageable page);

    List<UserMusicDetail> findByUser_Card_ExtIdAndMusicId(long userId, int id);

    Optional<UserMusicDetail> findByUserAndMusicIdAndLevel(UserData userData, int musicId, int level);

    @Transactional
    void deleteByUser(UserData user);
}
