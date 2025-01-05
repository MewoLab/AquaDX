package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity(name = "ChusanUserItem")
@Table(name = "chusan_user_item", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "item_id", "item_kind"])])
class UserItem(
    var itemKind: Int = 0,
    var itemId: Int = 0,
    var stock: Int = 1,
    @JsonProperty("isValid")
    var isValid: Boolean = true
) : Chu3UserEntity()
