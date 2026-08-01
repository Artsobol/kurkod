package io.github.artsobol.kurkod.feature.auth.refreshtoken.repository;

import io.github.artsobol.kurkod.feature.auth.refreshtoken.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<RefreshToken> findByTokenHash(String tokenHash);

  @Query("""
            select t
            from RefreshToken t
            where t.user.id=:userId
            and t.sessionId=:sessionId
            and t.expiresAt > CURRENT_TIMESTAMP
            and t.revokedAt is null
            """)
  List<RefreshToken> findActiveByUserIdAndSessionId(@Param("userId") Long userId, @Param("sessionId") UUID sessionId);

  @Query("""
            select count(*)
            from RefreshToken t
            where t.user.id=:userId
            and t.sessionId=:sessionId
            and t.expiresAt > CURRENT_TIMESTAMP
            and t.revokedAt is null
            """)
  long countActiveByUserIdAndSessionId(@Param("userId") Long userId, @Param("sessionId") UUID sessionId);

  @Query("""
            select count(*)
            from RefreshToken t
            where t.user.id=:userId
            and t.expiresAt> CURRENT_TIMESTAMP
            and t.revokedAt is null
            """)
  long countActiveSessions(@Param("userId") Long userId);

  @Query("""
                select t
                    from RefreshToken t
                        where t.user.id=:userId
                            and t.expiresAt > CURRENT_TIMESTAMP
                                and t.revokedAt is null
                                         order by t.lastUsedAt asc
            """)
  List<RefreshToken> findOldestActiveSessions(@Param("userId") Long userId, Pageable pageable);

  @Modifying
  @Query("""
                    update RefreshToken t
                                set t.revokedAt = CURRENT_TIMESTAMP
                    where t.user.id=:userId
                    and t.sessionId=:sessionId
                    and t.expiresAt > CURRENT_TIMESTAMP
                    and t.revokedAt is null
            """)
  void revokeSessionByUserIdAndSessionId(@Param("userId") Long userId,@Param("sessionId") UUID sessionId);
}
