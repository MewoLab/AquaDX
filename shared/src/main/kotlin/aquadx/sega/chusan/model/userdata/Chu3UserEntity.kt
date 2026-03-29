package aquadx.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import aquadx.net.games.BaseEntity
import aquadx.net.games.IUserEntity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
open class Chu3UserEntity : BaseEntity(), IUserEntity<Chu3UserData> {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    override var user: Chu3UserData = Chu3UserData()
}