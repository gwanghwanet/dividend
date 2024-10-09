package com.zerobase.service;

import com.zerobase.exception.impl.NoCompanyException;
import com.zerobase.model.Company;
import com.zerobase.model.Dividend;
import com.zerobase.model.ScrapedResult;
import com.zerobase.model.constants.CacheKey;
import com.zerobase.persist.CompanyRepository;
import com.zerobase.persist.DividendRepository;
import com.zerobase.persist.entity.CompanyEntity;
import com.zerobase.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 캐싱을 사용할 때 고려해야할 점.
    // - 요청이 자주 들어오는 가?
    // - 자주 변경되는 테이터 인가?
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> {}", companyName);
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                                            .orElseThrow(() -> new NoCompanyException());

        // 2. 조회된 회사 ID로 배당금 정보 조회
        List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        // for 문을 활용한 방법
//        List<Dividend> dividends = new ArrayList<>();
//        for(var entity : dividendEntities) {
//            dividends.add(Dividend.builder()
//                                    .date(entity.getDate())
//                                    .dividend(entity.getDividend())
//                                    .build());
//        }

        // stream 을 활용한 방법
        List<Dividend> dividends = dividendEntities.stream()
                                                    .map(e -> new Dividend(e.getDate(), e.getDividend()))
                                                    .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(), company.getName()),
                                dividends);
    }
}
