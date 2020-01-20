package io.crypto.beer.telegram.bot.engine.entity.pagination;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationPageDto {

    private Integer totalPages;
    private List<IPaginationEntity> content;
}
