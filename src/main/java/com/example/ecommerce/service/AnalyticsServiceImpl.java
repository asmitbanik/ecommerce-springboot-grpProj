package com.example.ecommerce.service;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.model.ProductView;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.repository.ProductViewRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ProductViewRepository productViewRepository;
    private final PaymentRepository paymentRepository;
    private final MongoTemplate mongoTemplate;

    public AnalyticsServiceImpl(ProductViewRepository productViewRepository, PaymentRepository paymentRepository, MongoTemplate mongoTemplate) {
        this.productViewRepository = productViewRepository;
        this.paymentRepository = paymentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void incrementProductView(String productId) {
        Optional<ProductView> pv = productViewRepository.findById(productId);
        ProductView view = pv.orElseGet(() -> {
            ProductView p = new ProductView();
            p.setId(productId);
            p.setViews(0);
            return p;
        });
        view.setViews(view.getViews() + 1);
        productViewRepository.save(view);
    }

    @Override
    public long getProductViews(String productId) {
        return productViewRepository.findById(productId).map(ProductView::getViews).orElse(0L);
    }

    @Override
    public BigDecimal getSalesSum(Instant from, Instant to) {
        MatchOperation match = Aggregation.match(Criteria.where("status").is("PAID").and("createdAt").gte(from).lte(to));
        Aggregation agg = Aggregation.newAggregation(match, Aggregation.group().sum("amount").as("total"));
        AggregationResults<java.util.Map> res = mongoTemplate.aggregate(agg, Payment.class, java.util.Map.class);
        java.util.Map map = res.getUniqueMappedResult();
        if (map == null) return BigDecimal.ZERO;
        Object total = map.get("total");
        if (total instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
        return BigDecimal.ZERO;
    }
}