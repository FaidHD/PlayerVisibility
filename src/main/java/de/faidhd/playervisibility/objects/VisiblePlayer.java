package de.faidhd.playervisibility.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class VisiblePlayer {

    private UUID uuid;
    @Setter
    private VisibilityType visibilityType;

}
