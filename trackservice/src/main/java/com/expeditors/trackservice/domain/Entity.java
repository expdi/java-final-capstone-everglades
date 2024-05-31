package com.expeditors.trackservice.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public abstract class Entity {
    private int id;

}
