package io.crypto.beer.telegram.bot.engine.services.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import io.crypto.beer.telegram.bot.engine.entity.pagination.IPaginationEntity;
import io.crypto.beer.telegram.bot.engine.entity.pagination.IPaginationService;
import io.crypto.beer.telegram.bot.engine.entity.pagination.PaginationPageDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TestPaginationService implements IPaginationService {

    @Override
    public String getName() {
        return "paginationServiceExample";
    }

    List<IPaginationEntity> repositoryList = new ArrayList<>();

    @PostConstruct
    public void init() {
        repositoryList.add(TestPaginationEntity.builder().id(1).name("Andrii").phone("093").build());
        repositoryList.add(TestPaginationEntity.builder().id(2).name("Ivan").phone("094").build());
        repositoryList.add(TestPaginationEntity.builder().id(3).name("Vasyl").phone("053").build());
        repositoryList.add(TestPaginationEntity.builder().id(4).name("Some").phone("023").build());
        repositoryList.add(TestPaginationEntity.builder().id(5).name("Andrii").phone("193").build());
        repositoryList.add(TestPaginationEntity.builder().id(6).name("Lesyk").phone("293").build());
        repositoryList.add(TestPaginationEntity.builder().id(7).name("Telesyk").phone("393").build());
        repositoryList.add(TestPaginationEntity.builder().id(8).name("Bogdan").phone("593").build());
        repositoryList.add(TestPaginationEntity.builder().id(9).name("Someone").phone("231233").build());
        repositoryList.add(TestPaginationEntity.builder().id(10).name("hey").phone("093").build());
        repositoryList.add(TestPaginationEntity.builder().id(11).name("a").phone("493").build());
        repositoryList.add(TestPaginationEntity.builder().id(12).name("b").phone("095").build());
        repositoryList.add(TestPaginationEntity.builder().id(13).name("c").phone("092").build());
        repositoryList.add(TestPaginationEntity.builder().id(14).name("d").phone("091").build());
        repositoryList.add(TestPaginationEntity.builder().id(15).name("e").phone("096").build());
    }

    @Override
    public PaginationPageDto getByPagination(Integer page, Integer size, Map<String, String> params) {

        return PaginationPageDto.builder()
            .content(repositoryList.subList(page * size, page * size + size))
            .totalPages(repositoryList.size() / size)
            .build();
    }
}
