package com.example.chackchack.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class SoftDeleteEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Soft delete 메서드
    public void delete() { this.deletedAt = LocalDateTime.now(); }

    // soft delete가 됐는지 확인
    public boolean isDeleted() { return this.deletedAt != null; }
}
