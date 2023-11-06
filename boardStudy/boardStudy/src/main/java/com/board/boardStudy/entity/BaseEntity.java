package com.board.boardStudy.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false) // 업데이트할때 관여하지 않겠다
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(insertable = false) // 인설트할때 관여하지 않겠다
    private LocalDateTime updatedTime;
}
