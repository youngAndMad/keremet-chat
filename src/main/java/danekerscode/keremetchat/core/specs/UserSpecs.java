package danekerscode.keremetchat.core.specs;

import danekerscode.keremetchat.model.dto.request.UsersCriteria;
import danekerscode.keremetchat.model.entity.AuthType;
import danekerscode.keremetchat.model.entity.BaseEntity;
import danekerscode.keremetchat.model.entity.SecurityRole;
import danekerscode.keremetchat.model.entity.User;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * Utility class to generate specifications from passed criteria
 * @author youngAndMad
 */
@UtilityClass
public class UserSpecs {

    /**
     * @param criteria criteria for users filtering
     * @return Specification to filter users by passes criteria
     * @see org.springframework.data.jpa.domain.Specification
     * @see danekerscode.keremetchat.model.dto.request.UsersCriteria
     * @author youngAndMad
     */
    public static Specification<User> fromUsersCriteria(
            UsersCriteria criteria
    ) {
        return ((root, q, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (Boolean.TRUE.equals(criteria.isActive())) {
                predicates.add(cb.equal(root.get(User.Fields.isActive), criteria.isActive()));
            }

            if (StringUtils.hasText(criteria.keyword())) {
                var keywordPredicated = new ArrayList<Predicate>();

                keywordPredicated.add(cb.like(root.get(User.Fields.username), criteria.keyword()));
                keywordPredicated.add(cb.like(root.get(User.Fields.email), criteria.keyword()));

                predicates.addAll(keywordPredicated);
            }

            if (StringUtils.hasText(criteria.authType())) {
                var authTypeJoin = root.join(User.Fields.authType, JoinType.LEFT);
                predicates.add(cb.equal(authTypeJoin.get(AuthType.Fields.name), criteria.authType()));
            }

            if (criteria.registeredTimeFrom() != null){
                predicates.add(cb.greaterThanOrEqualTo(root.get(BaseEntity.Fields.createdDate),criteria.registeredTimeTo()));
            }

            if (criteria.registeredTimeTo() != null){
                predicates.add(cb.lessThanOrEqualTo(root.get(BaseEntity.Fields.createdDate),criteria.registeredTimeTo()));
            }

            if (criteria.role() != null){
                var securityRoleJoin = root.join(User.Fields.roles, JoinType.LEFT);
                predicates.add(cb.equal(securityRoleJoin.get(SecurityRole.Fields.type), criteria.role()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
