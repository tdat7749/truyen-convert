package truyenconvert.server.modules.redis.service;

import org.springframework.stereotype.Service;

@Service
public interface RedisService {
    void evictCachePrefixAndSuffixUserId(String cacheValue,int userId);
}
