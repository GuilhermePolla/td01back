package org.gy.back.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class DefaultEntity {
    @Id
    private String id;
    private Boolean deleted = false;

    public DefaultEntity(String id){
        this.id = id;
    }
}

