package io.crypto.beer.telegram.bot.engine.entity.pagination;

import java.util.Map;

public interface IPaginationService {

    String getName();
    PaginationPageDto getByPagination(Integer page, Integer size, Map<String, String> params);

}
