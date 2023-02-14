package com.example.distributedlockstudy.global.config

import org.redisson.Redisson
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableConfigurationProperties(RedisProperties::class)
class RedisConfig {

    @Autowired
    private lateinit var redisProperties: RedisProperties

    /**
     * @Bean(destroyMethod = "shutdown")은 빈이 소멸될 때 호출할 메서드를 지정하는 어노테이션입니다. 이를 사용하면, 빈이 소멸될 때 리소스를 해제하거나 후처리 작업을 수행할 수 있습니다.
     *
     * Redisson은 빈이 소멸될 때 shutdown 메서드를 호출하여 Redisson 클라이언트를 종료하도록 되어 있습니다.
     * Redisson 클라이언트는 내부적으로 Netty와 같은 리소스를 사용하기 때문에, 사용이 끝나면 반드시 클라이언트를 종료해주어야 합니다.
     * 이를 위해, RedissonClient 빈을 생성할 때 @Bean(destroyMethod = "shutdown")을 사용하여 RedissonClient가 소멸될 때 shutdown 메서드를 호출하도록 설정해주는 것이 좋습니다.
     *
     * @author jeong-gumin
     * @since 2023/02/15
     * */
    @Bean(destroyMethod = "shutdown")
    fun redissonClient(): RedissonClient {
        val config = Config()

        config.useSingleServer()
            .run {
                this.address = "redis://${redisProperties.host}:${redisProperties.port}"
                this.password = redisProperties.password
            }

        return Redisson.create(config)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.setDefaultSerializer(StringRedisSerializer())
        redisTemplate.hashValueSerializer = GenericToStringSerializer(Any::class.java)
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }
}
