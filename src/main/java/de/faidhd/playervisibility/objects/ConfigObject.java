package de.faidhd.playervisibility.objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ConfigObject {

    @Builder.Default
    private String prefix = "&8[&cPlayerVisibility&8] &7";
    @Builder.Default
    @Setter
    private String spawnLocation = null;
    @Builder.Default
    private int radiusFromSpawn = 50;

}
