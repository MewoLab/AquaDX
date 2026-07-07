package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity(name = "ChusanUserVote")
@Table(name = "chusan_user_vote", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "vote_id"])])
class UserVote : Chu3UserEntity() {
    var voteId = 0

    var point = 0
    var totalPoint = 0

    @JsonProperty("isValid")
    var isValid = true
}
