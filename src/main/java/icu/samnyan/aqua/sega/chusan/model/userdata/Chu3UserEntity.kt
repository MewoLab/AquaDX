package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import icu.samnyan.aqua.net.games.BaseEntity
import icu.samnyan.aqua.net.games.IUserEntity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
class Chu3UserEntity : BaseEntity(), IUserEntity<Chu3UserData> {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    override var user: Chu3UserData = Chu3UserData()
}