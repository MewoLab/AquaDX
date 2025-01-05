package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity(name = "ChusanUserGeneralData")
@Table(name = "chusan_user_general_data")
class UserGeneralData(
    @Column(columnDefinition = "TEXT")
    var propertyValue: String = "",
    var propertyKey: String = "",
) : Chu3UserEntity()
