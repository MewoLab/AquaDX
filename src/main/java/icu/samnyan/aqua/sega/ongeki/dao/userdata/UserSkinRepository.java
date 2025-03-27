package icu.samnyan.aqua.sega.ongeki.dao.userdata;

import icu.samnyan.aqua.sega.ongeki.model.userdata.UserData;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserSkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Repository("OngekiUserSkinRepository")
public interface UserSkinRepository extends JpaRepository<UserSkin, Long> {

    List<UserSkin> findByUser_Card_ExtId(long userId);

    @Transactional
    void deleteByUser(UserData user);
}
