package truyenconvert.server.modules.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String,Object> redisTemplate;
    public RedisServiceImpl(
            RedisTemplate<String,Object> redisTemplate
    ){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void evictCachePrefixAndSuffixUserId(String cacheValue, int userId) {
        String pattern = cacheValue + "*" + "userId:" + userId + "*";
        Set<String> keys = redisTemplate.keys(pattern);

        if(keys != null && !keys.isEmpty()){
            redisTemplate.delete(keys);
        }
    }
}
