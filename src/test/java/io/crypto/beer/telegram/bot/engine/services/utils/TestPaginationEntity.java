package io.crypto.beer.telegram.bot.engine.services.utils;

import io.crypto.beer.telegram.bot.engine.entity.pagination.IPaginationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestPaginationEntity implements IPaginationEntity {

    private Integer id;
    private String name;
    private String phone;

    public Object[] getButtonTextParams() {
        return new Object[] { id, name };
    }
}
