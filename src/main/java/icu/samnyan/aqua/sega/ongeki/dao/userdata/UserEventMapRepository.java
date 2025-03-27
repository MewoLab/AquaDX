package icu.samnyan.aqua.sega.ongeki.dao.userdata;

import icu.samnyan.aqua.sega.ongeki.model.userdata.UserData;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserEventMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Repository("OngekiUserEventMapRepository")
public interface UserEventMapRepository extends JpaRepository<UserEventMap, Long> {

    Optional<UserEventMap> findByUser(UserData userData);

    Optional<UserEventMap> findByUser_Card_ExtId(long userId);

    @Transactional
    void deleteByUser(UserData user);
}
