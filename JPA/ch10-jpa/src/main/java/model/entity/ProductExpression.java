package model.entity;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.types.expr.BooleanExpression;

public class ProductExpression {
    @QueryDelegate(Product.class)
    public static BooleanExpression isExpensive(QProduct product, Integer price) {
        return product.price.gt(price);
    }
}
