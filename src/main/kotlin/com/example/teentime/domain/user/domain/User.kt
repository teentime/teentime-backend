package com.example.teentime.domain.user.domain

import com.example.teentime.global.entity.BaseUUIDEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity

@Entity(name = "tbl_user")
class User(
    id: UUID?,

    @Column(name = "accountId", nullable = false, unique = true)
    val accountId: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "phone_number" ,nullable = false, columnDefinition = "CHAR(11)")
    val phoneNumber: String,

    @Column(name = "gender", nullable = false)
    val gender: String,

    @Column(name = "birth_year", nullable = false)
    val birthYear: String,

    @Column(name = "birthday", nullable = false)
    val birthday: String,

    @Column(name = "email", nullable = true)
    val email: String,

    @Column(name = "school_name")
    val schoolName: String,

    @Column(name = "school_major")
    val schoolMajor: String,

    @Column(name = "grade")
    val grade: String

): BaseUUIDEntity(id)