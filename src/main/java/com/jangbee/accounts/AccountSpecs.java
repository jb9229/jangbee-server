
package com.jangbee.accounts;

import com.jangbee.accounts.Account_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by jeong on 2015-12-28.
 */
public class AccountSpecs {

    public static Specification<Account> emailEqual(final String email) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(Account_.email), email);
            }
        };
    }
}